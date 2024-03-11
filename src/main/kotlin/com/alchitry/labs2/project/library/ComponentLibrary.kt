package com.alchitry.labs2.project.library

import com.alchitry.labs2.JarUtils
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.files.Component

object ComponentLibrary {
    val components: List<Component>

    init {
        val components = mutableListOf<Component>()
        JarUtils.traverserResourceRecursively(Locations.components) { relativePath, isDirectory ->
            if (isDirectory)
                return@traverserResourceRecursively
            val nameSections = relativePath.split("/")
            check(nameSections.size >= 2) { "All library files should be in a category!" }
            components.add(Component.fromResource(relativePath))
        }
        this.components = components
    }

    /**
     * Finds a component by its path (categories and filename) or just filename if no exact matches are found.
     *
     * @param path The path of the component, including the categories and the name (e.g., "category1/category2/componentName").
     * @return The component found by the path, or null if no component is found.
     */
    fun findByPath(path: String): Component? {
        val parts = path.split("/")
        val name = parts.last()
        val categories = parts.subList(0, parts.size - 1).map { it.lowercase() }
        return components.firstOrNull { component ->
            (component.categories.map { it.lowercase() } == categories) && (component.name == name)
        } ?: components.firstOrNull { it.name == name }
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