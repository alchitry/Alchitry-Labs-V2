package com.alchitry.labs2.project.files

import com.alchitry.labs2.project.HDL
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*

@Serializable(with = SourceFile.Companion::class)
class SourceFile(
    file: FileProvider,
    val top: Boolean = false
) : ProjectFile(file) {
    @Transient
    override val language =
        HDL.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")

    override fun hashCode(): Int = 31 * super.hashCode() + top.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SourceFile) return false

        if (file != other.file) return false
        if (top != other.top) return false

        return true
    }

    companion object : KSerializer<SourceFile> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SourceFile") {
            element("file", FileProvider.serializer().descriptor)
            element("top", PrimitiveSerialDescriptor("top", PrimitiveKind.BOOLEAN), isOptional = true)
        }

        override fun deserialize(decoder: Decoder): SourceFile {
            return decoder.decodeStructure(descriptor) {
                var file: FileProvider? = null
                var top = false
                while (true) {
                    when (val index = decodeElementIndex(descriptor)) {
                        0 -> file = decodeSerializableElement(descriptor, 0, FileProvider.serializer())
                        1 -> top = decodeBooleanElement(descriptor, 1)
                        CompositeDecoder.DECODE_DONE -> break
                        else -> error("Unexpected index: $index")
                    }
                }
                check(file != null) { "Missing file entry for SourceFile!" }
                SourceFile(file, top)
            }
        }

        override fun serialize(encoder: Encoder, value: SourceFile) {
            encoder.encodeStructure(ConstraintFile.descriptor) {
                encodeSerializableElement(descriptor, 0, FileProvider.serializer(), value.file)
                if (value.top)
                    encodeBooleanElement(descriptor, 1, true)
            }
        }
    }


}
