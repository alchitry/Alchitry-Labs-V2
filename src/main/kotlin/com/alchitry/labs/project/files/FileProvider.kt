package com.alchitry.labs.project.files

import java.io.File
import java.io.InputStream

sealed class FileProvider {
    abstract val name: String
    abstract fun inputStream(): InputStream
    abstract fun isValid(): Boolean
    abstract val path: String

    val extension: String get() = name.split('.').last()

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
    }

    fun readText(): String {
        return inputStream().use {
            String(it.readAllBytes())
        }
    }

    fun writeText(text: String) {
        when (this) {
            is DiskFile -> file.writeText(text)
            is ResourceFile -> error("Resource files are read-only!")
        }
    }
}

