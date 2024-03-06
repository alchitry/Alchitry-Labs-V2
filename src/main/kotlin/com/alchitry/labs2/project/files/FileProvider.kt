package com.alchitry.labs2.project.files

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.File

@Serializable
@SerialName("FileProvider")
sealed class FileProvider {
    /**
     * Name of the file including the extension.
     */
    abstract val name: String

    /**
     * Returns true if this is a valid file (exists, can be read, etc.)
     */
    abstract fun isValid(): Boolean

    /**
     * Path to the file including the file name.
     */
    abstract val path: String

    /**
     * True if the file can't be written.
     */
    val readOnly: Boolean get() = this !is DiskFile || !this.file.canWrite()

    val extension: String get() = name.split('.').last()

    abstract fun readText(): String
    abstract fun writeText(text: String)

    /**
     * A standard file on disk.
     */
    @Serializable(with = DiskFile.Companion::class)
    @SerialName("DiskFile")
    data class DiskFile(
        val file: File
    ) : FileProvider() {
        constructor(path: String) : this(File(path))

        override val name: String get() = file.name
        override val path: String get() = file.path

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

        companion object : KSerializer<DiskFile> {
            override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DiskFile", PrimitiveKind.STRING)

            override fun deserialize(decoder: Decoder): DiskFile {
                return DiskFile(File("source").resolve(decoder.decodeString()))
            }

            override fun serialize(encoder: Encoder, value: DiskFile) {
                encoder.encodeString(value.name)
            }
        }
    }

    /**
     * A file stored in the jar. These can't be accessed via a File() like normal files can.
     */
    @Serializable
    @SerialName("ResourceFile")
    data class ResourceFile(
        override val name: String,
        val resourcePath: String
    ) : FileProvider() {
        override val path: String get() = resourcePath

        override fun isValid(): Boolean =
            this::class.java.getResource(resourcePath) != null

        override fun readText(): String {
            val stream =
                this::class.java.getResourceAsStream(resourcePath) ?: error("Invalid resource path: $resourcePath")
            return stream.use {
                String(it.readAllBytes())
            }
        }

        override fun writeText(text: String) {
            error("Resource files are read-only!")
        }
    }

    @Serializable
    @SerialName("StringFile")
    data class StringFile(
        override val name: String,
        override val path: String = name,
        val contents: String
    ) : FileProvider() {
        override fun isValid() = true

        override fun readText(): String = contents

        override fun writeText(text: String) {
            error("String files are read-only!")
        }
    }
}

