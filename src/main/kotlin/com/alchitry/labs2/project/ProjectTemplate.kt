package com.alchitry.labs2.project

import com.alchitry.labs2.JarUtils
import com.alchitry.labs2.Log
import com.alchitry.labs2.project.library.VivadoIP
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.io.path.absolutePathString

@Serializable
@SerialName("Template")
data class ProjectTemplate(
    val name: String,
    val description: String,
    val boards: List<Board>,
    val priority: Int,
    val requiresMig: Boolean = false
) {
    fun instantiate(projName: String, workspace: File, board: Board) {
        val sourceURL = this::class.java.getResource(Locations.lucidProjects + "/" + name)
            ?: error("Failed to find project template \"$name\"")
        val project = ProjectCreator.clone(sourceURL, "$name.alp", projName, workspace, board)
        Project.open(project)
        if (board is Board.XilinxBoard) {
            project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
                Project.withBuildLock {
                    if (requiresMig) {
                        try {
                            VivadoIP.generateMigCore(Project.current!!)
                        } catch (e: Exception) {
                            Log.error("Failed to add required MIG IP core!", e)
                        }
                    }
                    try {
                        Project.current!!.runInitialTclScript()
                    } catch (e: Exception) {
                        Log.error("Failed to run required TCL scripts!", e)
                    }
                }
            }
        }
    }

    private suspend fun Project.runInitialTclScript() {
        val initialTcl = ipCoreDirectory.resolve("initial.tcl").toFile()
        if (!initialTcl.exists()) return
        VivadoIP.createCoreProjectIfNeeded(this)
        val coresDirString = ipCoreDirectory.absolutePathString().replace("\\", "/")
        val coresEscaped = coresDirString.replace(" ", "\\ ")
        initialTcl.writeText(
            initialTcl.readText()
                .replace("\$CORE_DIR_ESC", coresEscaped)
                .replace("\$CORE_DIR", coresDirString)
        )
        VivadoIP.runTclScript(initialTcl.toPath())
        VivadoIP.checkForNewCores(this)
        Log.success("Done.")
    }

    companion object {
        fun getTemplates(): List<ProjectTemplate> {
            val templates = mutableListOf<ProjectTemplate>()
            JarUtils.traverserResourceRecursively(Locations.lucidProjects) { relativePath, isDirectory ->
                if (!isDirectory && relativePath.endsWith(".alp")) {
                    val stream = this::class.java.getResourceAsStream("${Locations.lucidProjects}/$relativePath")
                        ?: error("Failed to open stream for: \"${Locations.lucidProjects}/$relativePath\"")
                    val contents = String(stream.readAllBytes())
                    val template = Json.decodeFromString<AlchitryLabsProjectData>(contents).template
                        ?: error("Template JSON missing from template project: $relativePath")
                    templates.add(template)
                }
            }
            return templates.sortedWith(compareBy({ it.priority }, { it.name }))
        }
    }
}