package com.alchitry.labs2.project.files

import com.alchitry.labs2.camelToSnakeCase
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.project.Language
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.library.ComponentLibrary
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json

@Serializable
@SerialName("ComponentDescriptor")
private data class ComponentDescriptor(
    val name: String,
    val description: String,
    val supportedBoards: Set<Board>? = null,
    val skipDependencyCheck: Boolean = false,
)

@Serializable(with = Component.Companion::class)
data class Component(
    override val name: String,
    val componentName: String,
    val description: String,
    val supportedBoards: Set<Board>? = null,
    val skipDependencyCheck: Boolean = false,
    val categories: List<String>,
    val content: String,
) : FileProvider() {
    /**
     * Direct dependencies of this component. Doesn't include their sub-dependencies.
     */
    var dependencies = listOf<Component>()

    override fun isValid(): Boolean = true
    val path: String = categories.toMutableList().apply { add(name) }.joinToString("/")
    override fun readText(): String = content
    override fun writeText(text: String) {
        error("Components are read-only!")
    }

    /**
     * Returns all of this component's dependencies and all of their sub-dependencies
     */
    fun allDependencies(): List<Component> =
        (dependencies.flatMap { it.allDependencies() } + dependencies).distinct()

    val language = Language.fromExt(name.split(".").last()) ?: error("Unknown language of component: $name")

    companion object : KSerializer<Component> {
        fun fromResource(resourcePath: String): Component {
            val pieces = resourcePath.split("/")
            val fileName = pieces.last()
            val categories = pieces.subList(0, pieces.size - 1)
            val path = "${Locations.components}/$resourcePath"
            val fileContents = this::class.java.getResourceAsStream(path)?.let { String(it.readAllBytes()) }
            requireNotNull(fileContents) { "Failed to read resource stream: $path" }

            if (!fileContents.startsWith("/**"))
                error("Component library file $fileName doesn't start with \"/**\"!")
            val header = fileContents.substringAfter("/**", "").substringBefore("**/", "")
            if (header.isEmpty())
                error("Component library file's header was empty or missing a closing \"**/\"!")
            val descriptor = Json.decodeFromString<ComponentDescriptor>("{ $header }")
            val content = fileContents.substringAfter("**/").trim()
            return Component(
                name = fileName,
                componentName = descriptor.name,
                description = descriptor.description.trimIndent(),
                supportedBoards = descriptor.supportedBoards,
                skipDependencyCheck = descriptor.skipDependencyCheck,
                categories = categories,
                content = content
            )
        }

        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Component") {
            element("path", PrimitiveSerialDescriptor("path", PrimitiveKind.STRING))
        }

        override fun deserialize(decoder: Decoder): Component {
            return decoder.decodeStructure(descriptor) {
                val index = decodeElementIndex(descriptor)
                if (index != 0)
                    error("Unexpected index: $index")
                val resourcePath = decodeStringElement(descriptor, 0).replace(Regex("/([a-zA-Z0-9]+)\\.")) {
                    "/${it.groupValues[1].camelToSnakeCase()}."
                }

                ComponentLibrary.findByPath(resourcePath) ?: error("Failed to find component: $resourcePath")
            }
        }

        override fun serialize(encoder: Encoder, value: Component) {
            encoder.encodeStructure(descriptor) {
                encodeStringElement(descriptor, 0, value.path)
            }
        }
    }
}