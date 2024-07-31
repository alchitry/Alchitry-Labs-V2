package com.alchitry.labs2.project.library

import com.alchitry.labs2.JarUtils
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidGlobalContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidModuleTypeContext
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.notations.NotationManager
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.Component
import com.alchitry.labs2.project.files.SourceFile
import kotlinx.coroutines.runBlocking
import java.util.*

object ComponentLibrary {
    val components: List<Component>
    val library: LibrarySection

    init {
        val components = mutableListOf<Component>()
        try {
            JarUtils.traverserResourceRecursively(Locations.components) { relativePath, isDirectory ->
                if (isDirectory)
                    return@traverserResourceRecursively
                val nameSections = relativePath.split("/")
                check(nameSections.size >= 2) { "All library files should be in a category!" }
                components.add(Component.fromResource(relativePath))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // build dependency trees for lucid components
        // run blocking ok as it will never suspend
        runBlocking {
            val lucidFiles = components.filter { it.language is Languages.Lucid }.map { SourceFile(it) }
            val notationManager = NotationManager()

            // parseAll only suspends for file reads and all files are components
            val trees = Project.parseAll(lucidFiles, notationManager) ?: error(notationManager.getReport().toString())

            notationManager.assertNoErrors()

            val projectContext = ProjectContext(notationManager)

            trees.forEach {
                LucidGlobalContext(projectContext, it.first).walk(it.second) // not simulation so won't suspend
            }

            notationManager.assertNoErrors()

            val modules = trees.flatMap {
                LucidModuleTypeContext(projectContext, it.first).extract(it.second)
            }

            notationManager.assertNoErrors()

            modules.forEach { module ->
                ModuleInstance(
                    module.name,
                    projectContext,
                    null,
                    module,
                    mapOf(),
                    mapOf(),
                    testing = true
                ).apply {
                    initialWalk()
                    notationManager.assertNoErrors()
                    (module.sourceFile.file as Component).dependencies =
                        getModuleDependents().map { it.sourceFile.file as Component }
                }
            }

            notationManager.assertNoErrors()
        }

        this.components = components
        this.library = LibrarySection.fromComponents(components)
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
    override val subSections: SortedMap<String, MutableLibrarySection> = sortedMapOf()
) : LibrarySection {
    fun selectSubSection(list: List<String>): MutableLibrarySection {
        if (list.isEmpty())
            return this
        val key = list.first()
        val newList = list.subList(1, list.size)
        return subSections.getOrPut(key) { MutableLibrarySection(key) }.selectSubSection(newList)
    }
    fun sort() {
        components.sortBy { it.componentName }
        subSections.forEach { it.value.sort() }
    }
}

interface LibrarySection {
    val name: String
    val components: List<Component>
    val subSections: Map<String, LibrarySection>

    companion object {
        fun fromComponents(components: List<Component>): LibrarySection {
            val rootSection = MutableLibrarySection("Components")
            components.forEach { component ->
                rootSection.selectSubSection(component.categories).components.add(component)
            }
            rootSection.sort()
            return rootSection
        }
    }
}