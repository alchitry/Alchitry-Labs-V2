package com.alchitry.labs2.project.files

import com.alchitry.labs2.project.Project
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

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
        val path: Path
    ) : FileProvider() {
        constructor(path: String) : this(Paths.get(path))

        override val name: String get() = path.name
        val file: File
            get() {
                val projectPath =
                    Project.current?.path ?: error("DiskFile.file can only be accessed when a project is open!")
                return projectPath.resolve(path).toFile()
            }

        override fun isValid(): Boolean = file.run { exists() && canRead() }

        private var cachedContents: String? = null
        private var readTime: Long = 0

        override fun readText(): String {
            val file = file
            cachedContents?.let {
                if (readTime == file.lastModified())
                    return it
            }
            readTime = file.lastModified()
            return file.readText().also { cachedContents = it }
        }

        override fun writeText(text: String) {
            val file = file
            if (text == cachedContents && readTime == file.lastModified())
                return

            file.writeText(text)
            cachedContents = text
            readTime = file.lastModified()
        }

        companion object : KSerializer<DiskFile> {
            override val descriptor: SerialDescriptor = buildClassSerialDescriptor("DiskFile") {
                element("path", PrimitiveSerialDescriptor("path", PrimitiveKind.STRING))
            }

            override fun deserialize(decoder: Decoder): DiskFile {
                return decoder.decodeStructure(descriptor) {
                    val idx = decodeElementIndex(descriptor)
                    check(idx == 0) { "Unknown index: $idx" }
                    val path = decodeStringElement(descriptor, 0)
                    check(decodeElementIndex(descriptor) == CompositeDecoder.DECODE_DONE) { "Unexpected number of elements!" }

                    DiskFile(Paths.get(path))
                }

            }

            override fun serialize(encoder: Encoder, value: DiskFile) {
                encoder.encodeStructure(descriptor) {
                    encodeStringElement(descriptor, 0, value.path.toString())
                }

            }
        }
    }

    @Serializable
    @SerialName("StringFile")
    data class StringFile(
        override val name: String,
        val contents: String
    ) : FileProvider() {
        override fun isValid() = true

        override fun readText(): String = contents

        override fun writeText(text: String) {
            error("String files are read-only!")
        }
    }
}

