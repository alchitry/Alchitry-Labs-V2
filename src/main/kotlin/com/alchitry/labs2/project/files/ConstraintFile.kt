package com.alchitry.labs2.project.files

import com.alchitry.labs2.project.ConstraintLang
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

@Serializable(with = ConstraintFile.Companion::class)
class ConstraintFile(
    file: FileProvider
) : ProjectFile(file) {
    @Transient
    override val language =
        ConstraintLang.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")

    override fun hashCode(): Int = file.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConstraintFile) return false

        return file == other.file
    }

    companion object : KSerializer<ConstraintFile> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ConstraintFile") {
            element("file", FileProvider.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): ConstraintFile {
            return decoder.decodeStructure(descriptor) {
                val index = decodeElementIndex(descriptor)
                if (index != 0)
                    error("Unexpected index: $index")
                ConstraintFile(decodeSerializableElement(descriptor, 0, FileProvider.serializer()))
            }
        }

        override fun serialize(encoder: Encoder, value: ConstraintFile) {
            encoder.encodeStructure(descriptor) {
                encodeSerializableElement(descriptor, 0, FileProvider.serializer(), value.file)
            }
        }
    }
}
