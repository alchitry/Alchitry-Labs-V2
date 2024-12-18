package com.alchitry.labs2.project

import com.alchitry.labs2.PathUtil
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.project.library.ComponentLibrary
import org.jdom2.DataConversionException
import org.jdom2.Document
import org.jdom2.JDOMException
import org.jdom2.input.SAXBuilder
import java.io.File
import java.nio.file.Paths

const val PROJECT_VERSION = 4
const val MIN_PROJECT_VERSION = 4

fun Project.Companion.isXmlProject(file: File): Boolean {
    try {
        getXmlDocument(file)
    } catch (e: Exception) {
        return false
    }
    return true
}

private fun getXmlDocument(file: File): Document {


    val builder = SAXBuilder()
    return try {
        builder.build(file) as Document
    } catch (e: JDOMException) {
        throw Exception(e.message)
    }
}

fun Project.Companion.openXml(xmlFile: File): ProjectData1V0 {
    val document: Document = getXmlDocument(xmlFile)
    val folder = xmlFile.parentFile
    val projectXml = document.rootElement
    if (projectXml.name != Tags.project) {
        error("Root element not project tag")
    }
    val projName = projectXml.getAttribute(Tags.Attributes.name)
        ?: error("Project name is missing")
    val projectName = projName.value
    val brdType = projectXml.getAttribute(Tags.Attributes.board)
        ?: error("Board type is missing")
    val board = Board.fromName(brdType.value) ?: error("Unknown board type: " + brdType.value)

    val sourceFiles = HashSet<SourceFile>()
    val constraintFiles = HashSet<ConstraintFile>()

    var version = 0
    val versionAttr = projectXml.getAttribute(Tags.Attributes.version)
    if (versionAttr != null) try {
        version = versionAttr.intValue
        if (version > PROJECT_VERSION) error("Project file is from a future version!")
        if (version < MIN_PROJECT_VERSION) error("Project file is from an unsupported past version!")
    } catch (e: DataConversionException) {
        error("Invalid version ID!")
    }

    val sourceFolder = PathUtil.assembleFile(folder, "source")
    val constraintFolder = PathUtil.assembleFile(folder, "constraint")

    val list = projectXml.children
    for (i in list.indices) {
        val node = list[i]
        when (node.name) {
            Tags.files -> {
                val files = node.children
                var j = 0
                while (j < files.size) {
                    val file = files[j]
                    when (file.name) {
                        Tags.source -> {
                            val isTop = file.getAttribute(Tags.Attributes.top)?.value == "true"
                            val fullFile = FileProvider.DiskFile(Paths.get(sourceFolder.name, file.text))

                            sourceFiles.add(SourceFile(fullFile, isTop))
                        }

                        Tags.constraint -> {
                            val isLib = file.getAttribute(Tags.Attributes.library)?.value == "true"
                            if (isLib) {
                                val component = ComponentLibrary.components.firstOrNull { it.name == file.text }
                                    ?: error("Failed to find a component of name \"${file.text}\"")
                                constraintFiles.add(ConstraintFile(component))
                            } else {
                                val cstFile = FileProvider.DiskFile(Paths.get(constraintFolder.name, file.text))

                                constraintFiles.add(ConstraintFile(cstFile))
                            }
                        }

                        Tags.component -> {
                            val component = ComponentLibrary.components.firstOrNull { it.name == file.text }
                                ?: error("Failed to find a component of name \"${file.text}\"")
                            sourceFiles.add(SourceFile(component))
                        }

                        Tags.core -> {
                            error("IP Cores aren't supported in JSON conversion.")
//                            val cFiles = file.children
//                            val coreName = file.getAttributeValue(Tags.Attributes.name)
//                                ?: error("Missing core name")
//                            val coreDir = PathUtil.assemblePath(folder, "cores", coreName)
//                            val sFiles = cFiles.mapNotNull { cFile ->
//                                if (cFile.name == Tags.source) {
//                                    File(PathUtil.assemblePath(coreDir, cFile.text))
//                                } else null
//                            }.toSet()
//                            val stub = cFiles.firstNotNullOfOrNull { cFile ->
//                                if (cFile.name == Tags.stub) {
//                                    SourceFile(FileProvider.DiskFile(PathUtil.assembleFile(coreDir, cFile.text)))
//                                } else null
//                            }
//                            ipCores.add(IPCore(coreName, stub, sFiles))
                        }

                        else -> error("Unknown tag " + file.name)
                    }
                    j++
                }
            }

            else -> error("Unknown tag " + node.name)
        }
    }
    if (version != PROJECT_VERSION) {
        error("Incompatible version ID!")
    }

    val topCt = sourceFiles.count { it.top }
    if (topCt > 1)
        error("Project contains more than one top file!")
    if (topCt < 1)
        error("Project does not contain a top file!")

    return ProjectData1V0(projectName, board, sourceFiles, constraintFiles)
}