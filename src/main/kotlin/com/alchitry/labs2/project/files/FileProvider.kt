package com.alchitry.labs2.project.files

import java.io.File
import java.io.InputStream

sealed class FileProvider {
    abstract val name: String
    abstract fun inputStream(): InputStream
    abstract fun isValid(): Boolean
    abstract val path: String

    val extension: String get() = name.split('.').last()

    abstract fun readText(): String
    abstract fun writeText(text: String)

    /**
     * A standard file on disk.
     */
    data class DiskFile(
        val file: File
    ) : FileProvider() {
        constructor(path: String) : this(File(path))

        override val name: String get() = file.name
        override val path: String get() = file.path
        override fun inputStream() = file.inputStream()

        override fun isValid(): Boolean = file.exists() && file.canRead()

        private var cachedContents: String? = null
        private var readTime: Long = 0

        override fun readText(): String {
            cachedContents?.let {
                if (readTime == file.lastModified())
                    return it
            }
            readTime = file.lastModified()
            return file.readText().also { cachedContents = it }
        }

        override fun writeText(text: String) {
            if (text == cachedContents && readTime == file.lastModified())
                return

            file.writeText(text)
            cachedContents = text
            readTime = file.lastModified()
        }
    }

    /**
     * A file stored in the jar. These can't be accessed via a File() like normal files can.
     */
    data class ResourceFile(
        override val name: String,
        val resourcePath: String
    ) : FileProvider() {
        override val path: String get() = resourcePath

        override fun isValid(): Boolean =
            this::class.java.getResource(resourcePath) != null

        override fun inputStream(): InputStream =
            this::class.java.getResourceAsStream(resourcePath) ?: error("Invalid resource path: $resourcePath")

        override fun readText(): String = inputStream().use { String(it.readAllBytes()) }

        override fun writeText(text: String) {
            error("Resource files are read-only!")
        }
    }

    data class StringFile(
        override val name: String,
        override val path: String = name,
        val contents: String
    ) : FileProvider() {
        override fun inputStream() = contents.byteInputStream()
        override fun isValid() = true

        override fun readText(): String = contents

        override fun writeText(text: String) {
            error("String files are read-only!")
        }
    }
}

