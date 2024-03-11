package com.alchitry.labs2

import org.apache.commons.lang3.StringUtils
import java.io.*
import java.net.JarURLConnection
import java.net.URL
import java.net.URLConnection
import java.nio.file.Files
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.io.path.absolutePathString
import kotlin.io.path.createParentDirectories
import kotlin.io.path.toPath

object JarUtils {
    fun traverserResourceRecursively(
        resourceFolderPath: String,
        onEach: (relativePath: String, isDirectory: Boolean) -> Unit,
    ) {
        val resourceFolder =
            this::class.java.getResource(resourceFolderPath) ?: error("Invalid resource path: $resourceFolderPath")
        val urlConnection = resourceFolder.openConnection()

        if (urlConnection is JarURLConnection) {
            val jarFile: JarFile = urlConnection.jarFile
            val e: Enumeration<JarEntry> = jarFile.entries()
            while (e.hasMoreElements()) {
                val entry: JarEntry = e.nextElement()
                if (entry.name.startsWith(urlConnection.entryName)) {
                    val relativePath: String = StringUtils.removeStart(
                        entry.name,
                        urlConnection.entryName
                    )
                    val trimmedPath = if (relativePath.startsWith("/"))
                        relativePath.substring(1) // remove first "/"
                    else
                        relativePath
                    onEach(trimmedPath, entry.isDirectory)
                }
            }
        } else {
            val basePath = resourceFolder.toURI().toPath()
            Files.walk(basePath).forEach { p ->
                if (p != null && (Files.isDirectory(p) || Files.isRegularFile(p))) {
                    val relativePath = StringUtils.removeStart(
                        p.absolutePathString(),
                        basePath.absolutePathString()
                    ).replace("\\", "/")
                    val trimmedPath = if (relativePath.startsWith("/"))
                        relativePath.substring(1) // remove first "/"
                    else
                        relativePath
                    onEach(trimmedPath, Files.isDirectory(p))
                }
            }
        }
    }

    fun copyResourcesRecursively(
        originUrl: URL, destination: File
    ): Boolean {
        val urlConnection: URLConnection = originUrl.openConnection()
        return if (urlConnection is JarURLConnection) {
            copyJarResourcesRecursively(
                destination,
                urlConnection
            )
        } else {
            File(originUrl.toURI()).copyRecursively(destination)
        }
    }

    @Throws(IOException::class)
    private fun copyJarResourcesRecursively(
        destDir: File?,
        jarConnection: JarURLConnection
    ): Boolean {
        val jarFile: JarFile = jarConnection.jarFile
        val e: Enumeration<JarEntry> = jarFile.entries()
        while (e.hasMoreElements()) {
            val entry: JarEntry = e.nextElement()
            if (entry.name.startsWith(jarConnection.entryName)) {
                val filename: String = StringUtils.removeStart(
                    entry.name,
                    jarConnection.entryName
                )
                val f = File(destDir, filename)
                if (!entry.isDirectory) {
                    // required as the directory entry doesn't always come before a file inside it
                    f.toPath().createParentDirectories()
                    val entryInputStream = jarFile.getInputStream(entry)
                    copyStream(entryInputStream, FileOutputStream(f))
                    entryInputStream.close()
                } else {
                    if (!ensureDirectoryExists(f)) {
                        throw IOException(
                            "Could not create directory: "
                                    + f.absolutePath
                        )
                    }
                }
            }
        }
        return true
    }

    private fun copyStream(iStream: InputStream, oSteam: OutputStream) {
        val buf = ByteArray(1024)
        var len: Int
        while ((iStream.read(buf).also { len = it }) > 0) {
            oSteam.write(buf, 0, len)
        }
        iStream.close()
        oSteam.close()
    }

    private fun ensureDirectoryExists(f: File): Boolean {
        return f.exists() || f.mkdir()
    }
}