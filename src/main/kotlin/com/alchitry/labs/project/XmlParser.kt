package com.alchitry.labs.project

import com.alchitry.labs.PathUtil
import com.alchitry.labs.project.files.ConstraintFile
import com.alchitry.labs.project.files.FileProvider
import com.alchitry.labs.project.files.IPCore
import com.alchitry.labs.project.files.SourceFile
import org.jdom2.*
import org.jdom2.input.SAXBuilder
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths

const val PROJECT_VERSION = 4
const val MIN_PROJECT_VERSION = 4

fun Project.Companion.openXml(xmlFile: File): Project {
    if (!xmlFile.isFile || xmlFile.extension != "alp") {
        error("The file ${xmlFile.path} is not an Alchitry Labs project file (.alp)!")
    }

    val builder = SAXBuilder()
    val folder = xmlFile.parentFile
    val document: Document = try {
        builder.build(xmlFile) as Document
    } catch (e: JDOMException) {
        throw Exception(e.message)
    }
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
    val ipCores = HashSet<IPCore>()

    var version = 0
    val versionAttr = projectXml.getAttribute(Tags.Attributes.version)
    if (versionAttr != null) try {
        version = versionAttr.intValue
        if (version > PROJECT_VERSION) error("Project file is from a future version!")
        if (version < MIN_PROJECT_VERSION) error("Project file is from an unsupported past version!")
    } catch (e: DataConversionException) {
        error("Invalid version ID!")
    }

    val sourceFolder = PathUtil.assembleFile(folder, Locations.Project.sourceFolder)
    val constraintFolder = PathUtil.assembleFile(folder, Locations.Project.constraintFolder)

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
                            val fullFile = FileProvider.DiskFile(PathUtil.assembleFile(sourceFolder, file.text))

                            if (!fullFile.isValid()) {
                                error("Missing file: ${fullFile.path}")
                            }

                            sourceFiles.add(SourceFile(fullFile, isTop))
                        }

                        Tags.constraint -> {
                            val isLib = file.getAttribute(Tags.Attributes.library)?.value == "true"
                            val cstFile = if (isLib) {
                                FileProvider.ResourceFile(file.text, "${Locations.components}/${file.text}")
                            } else {
                                FileProvider.DiskFile(PathUtil.assembleFile(constraintFolder, file.text))
                            }

                            if (!cstFile.isValid()) {
                                error("Missing file: ${cstFile.path}")
                            }

                            constraintFiles.add(ConstraintFile(cstFile))
                        }

                        Tags.component -> {
                            val resourceFile =
                                FileProvider.ResourceFile(file.text, "${Locations.components}/${file.text}")
                            if (!resourceFile.isValid())
                                error("Resource invalid: ${resourceFile.resourcePath}")
                            sourceFiles.add(SourceFile(resourceFile))
                        }

                        Tags.core -> {
                            val cFiles = file.children
                            val coreName = file.getAttributeValue(Tags.Attributes.name)
                                ?: error("Missing core name")
                            val coreDir = PathUtil.assemblePath(folder, Locations.Project.ipCoresFolder, coreName)
                            val sFiles = cFiles.mapNotNull { cFile ->
                                if (cFile.name == Tags.source) {
                                    File(PathUtil.assemblePath(coreDir, cFile.text))
                                } else null
                            }.toSet()
                            val stub = cFiles.firstNotNullOfOrNull { cFile ->
                                if (cFile.name == Tags.stub) {
                                    SourceFile(FileProvider.DiskFile(PathUtil.assembleFile(coreDir, cFile.text)))
                                } else null
                            }
                            ipCores.add(IPCore(coreName, stub, sFiles))
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
        // TODO: Upgrade old projects
        error("Incompatible version ID!")
    }

    val topCt = sourceFiles.count { it.top }
    if (topCt > 1)
        error("Project contains more than one top file!")
    if (topCt < 1)
        error("Project does not contain a top file!")

    return Project(projectName, folder, board, sourceFiles, constraintFiles, ipCores)
}

fun Project.saveXML(file: File = projectFile) {
    val project = Element(Tags.project)
    project.setAttribute(Attribute(Tags.Attributes.name, projectName))
    project.setAttribute(Attribute(Tags.Attributes.board, board.name))
    project.setAttribute(Attribute(Tags.Attributes.version, PROJECT_VERSION.toString()))
    val doc = Document(project)
    val source = Element(Tags.files)
    for (sourceFile in sourceFiles) {
        if (sourceFile.isLibFile) {
            source.addContent(Element(Tags.component).setText(sourceFile.name))
        } else {
            val ele = Element(Tags.source).setText(sourceFile.name)
            if (sourceFile == top) ele.setAttribute(Attribute(Tags.Attributes.top, "true"))
            source.addContent(ele)
        }
    }
    for (ucfFile in constraintFiles) {
        val ele = Element(Tags.constraint).setText(ucfFile.name)
        if (ucfFile.isLibFile) ele.setAttribute(Attribute(Tags.Attributes.library, "true"))
        source.addContent(ele)
    }
    for (core in ipCores) {
        val coreElement = Element(Tags.core).setAttribute(Tags.Attributes.name, core.name)
        val corePath = Paths.get(
            File(
                PathUtil.assemblePath(
                    projectFolder,
                    Locations.Project.ipCoresFolder,
                    core.name
                )
            ).absolutePath
        )
        for (coreFile in core.files) {
            println("Original file ${coreFile.absolutePath}")
            val p = corePath.relativize(Paths.get(coreFile.absolutePath)).toString()
            println("Relative file $p")
            coreElement.addContent(Element(Tags.source).setText(p))
        }
        if (core.stub != null && core.stub.file is FileProvider.DiskFile) {
            val p = corePath.relativize(Paths.get(core.stub.file.file.absolutePath)).toString()
            coreElement.addContent(Element(Tags.stub).setText(p))
        }
        source.addContent(coreElement)
    }
    project.addContent(source)
    val xmlOutput = XMLOutputter()
    xmlOutput.format = Format.getPrettyFormat()
    xmlOutput.output(doc, FileWriter(file))
}