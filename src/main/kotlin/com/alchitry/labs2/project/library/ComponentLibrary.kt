package com.alchitry.labs2.project.library

import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.files.Component
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.net.JarURLConnection
import java.nio.file.Files
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.io.path.absolutePathString

object ComponentLibrary {
    val components: List<Component>

    init {
        val componentsFolder =
            this::class.java.getResource(Locations.components) ?: error("Failed to find components directory!")

        val urlConnection = componentsFolder.openConnection()
        if (urlConnection is JarURLConnection) {
            components = buildLibFromJar(urlConnection)
        } else {
            components = buildLibFromDir(File(componentsFolder.toURI()))
        }
    }

    private fun buildLibFromJar(jarConnection: JarURLConnection): List<Component> {
        val jarFile: JarFile = jarConnection.jarFile
        val e: Enumeration<JarEntry> = jarFile.entries()
        val components = mutableListOf<Component>()
        while (e.hasMoreElements()) {
            val entry: JarEntry = e.nextElement()
            if (entry.name.startsWith(jarConnection.entryName) && !entry.isDirectory) {
                val filename: String = StringUtils.removeStart(
                    entry.name,
                    jarConnection.entryName
                )
                val nameSections = filename.split("/")
                check(nameSections.size >= 2) { "All library files should be in a category!" }

                components.add(Component.fromResource(filename))
            }
        }
        return components
    }

    private fun buildLibFromDir(componentDir: File): List<Component> {
        val components = mutableListOf<Component>()
        Files.walk(componentDir.toPath()).forEach { p ->
            if (p != null && Files.isRegularFile(p)) {
                val filename = StringUtils.removeStart(
                    p.absolutePathString(),
                    componentDir.absolutePath
                ).replace("\\", "/").substring(1) // remove first "/"
                val nameSections = filename.split("/")
                check(nameSections.size >= 2) { "All library files should be in a category!" }

                components.add(Component.fromResource(filename))
            }
        }
        return components
    }
}

private data class MutableLibrarySection(
    override val name: String,
    override val components: MutableList<Component> = mutableListOf(),
    override val subSections: MutableMap<String, MutableLibrarySection> = mutableMapOf()
) : LibrarySection {
    fun selectSubSection(list: List<String>): MutableLibrarySection {
        if (list.isEmpty())
            return this
        val key = list.first()
        val newList = list.subList(1, list.size)
        return subSections.getOrPut(key) { MutableLibrarySection(key) }.selectSubSection(newList)
    }
}

interface LibrarySection {
    val name: String
    val components: List<Component>
    val subSections: Map<String, LibrarySection>
}