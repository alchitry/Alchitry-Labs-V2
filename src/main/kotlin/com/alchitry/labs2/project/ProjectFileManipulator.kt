package com.alchitry.labs2.project

import com.alchitry.labs2.Log
import com.alchitry.labs2.parsers.basicOffsetProvider
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.VerilogLexer
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.project.files.*
import com.alchitry.labs2.ui.tabs.FileTab
import com.alchitry.labs2.ui.tabs.Workspace
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import java.io.File
import kotlin.io.path.createParentDirectories

fun Project.removeFile(file: ProjectFile, delete: Boolean) {
    if (delete && !file.isReadOnly && !file.isLibFile && file.file is FileProvider.DiskFile) {
        file.file.file.delete()
    }
    Workspace.closeFile(file, false)
    val newSourceFiles = data.sourceFiles.toMutableSet().apply { remove(file) }
    val newConstraintFiles = data.constraintFiles.toMutableSet().apply { remove(file) }
    val newProj = copy(data = data.copy(sourceFiles = newSourceFiles, constraintFiles = newConstraintFiles))
    newProj.save()
    Project.open(newProj)
}

fun Project.renameModule(file: SourceFile, newName: String): SourceFile? {
    try {
        val diskFile = file.file as? FileProvider.DiskFile ?: run {
            Log.error("Cannot rename non-disk file ${file.name}")
            return null
        }
        val oldFile = path.resolve(diskFile.path).toFile()
        if (!oldFile.exists()) {
            Log.error("File does not exist: ${oldFile.path}")
            return null
        }

        val wasOpen = Workspace.getTabs().any { it is FileTab && it.file == file }
        Workspace.closeFile(file, save = true)

        val oldContent = oldFile.readText()
        val newContent = renameModuleInContent(oldContent, newName, file.language, oldFile.nameWithoutExtension)

        val extension = oldFile.extension
        val newFileName = "$newName.$extension"
        val newFile = File(oldFile.parentFile, newFileName)

        if (newFile.exists() && newFile.canonicalPath != oldFile.canonicalPath) {
            Log.error("File ${newFile.name} already exists!")
            return null
        }

        newFile.writeText(newContent)
        if (newFile.canonicalPath != oldFile.canonicalPath) {
            oldFile.delete()
        }

        val newRelativePath = path.relativize(newFile.toPath())
        val newSourceFile = SourceFile(FileProvider.DiskFile(newRelativePath), top = file.top)
        val newSourceFiles = data.sourceFiles.toMutableSet().apply {
            remove(file)
            add(newSourceFile)
        }
        val newProj = copy(data = data.copy(sourceFiles = newSourceFiles))
        newProj.save()
        Project.open(newProj)

        if (wasOpen) {
            Workspace.openFile(newSourceFile)
        }

        return newSourceFile
    } catch (e: Exception) {
        Log.showError("Failed to rename module!", e)
    }
    return null
}

fun renameModuleInContent(
    content: String,
    newName: String,
    language: Language,
    oldModuleName: String? = null
): String {
    return when (language) {
        Languages.Lucid -> renameLucidModuleInContent(content, newName, oldModuleName)
        Languages.Verilog -> renameVerilogModuleInContent(content, newName, oldModuleName)
        else -> content
    }
}

private fun renameLucidModuleInContent(content: String, newName: String, oldModuleName: String?): String {
    try {
        val lexer = LucidLexer(CharStreams.fromString(content, "rename_module")).apply {
            removeErrorListeners()
        }
        val tokenStream = CommonTokenStream(lexer)
        val parser = LucidParser(tokenStream).apply {
            removeErrorListeners()
        }
        val source = parser.source()
        val moduleCtx = if (oldModuleName != null) {
            source.module().firstOrNull { it.name()?.text == oldModuleName } ?: source.module().firstOrNull()
        } else {
            source.module().firstOrNull()
        }
        val testBenchCtx = if (oldModuleName != null) {
            source.testBench().firstOrNull { it.name()?.text == oldModuleName } ?: source.testBench().firstOrNull()
        } else {
            source.testBench().firstOrNull()
        }
        val nameCtx = moduleCtx?.name() ?: testBenchCtx?.name()
        if (nameCtx != null) {
            val range = basicOffsetProvider(nameCtx, tokenStream)
            if (range != null) {
                return content.replaceRange(range, newName)
            }
        }
    } catch (_: Exception) {
    }

    val regex = if (oldModuleName != null) {
        Regex("""\b(module|testbench)\s+($oldModuleName|\w+)""")
    } else {
        Regex("""\b(module|testbench)\s+([a-zA-Z0-9_]+)""")
    }
    val match = regex.find(content) ?: return content
    return content.replaceRange(match.range, "${match.groupValues[1]} $newName")
}

private fun renameVerilogModuleInContent(content: String, newName: String, oldModuleName: String?): String {
    try {
        val lexer = VerilogLexer(CharStreams.fromString(content, "rename_module")).apply {
            removeErrorListeners()
        }
        val tokenStream = CommonTokenStream(lexer)
        val parser = VerilogParser(tokenStream).apply {
            removeErrorListeners()
        }
        val source = parser.source_text()
        val moduleDeclarations = source.description().mapNotNull { it.module_declaration() }
        val moduleDecl = if (oldModuleName != null) {
            moduleDeclarations.firstOrNull { it.module_identifier()?.text == oldModuleName }
                ?: moduleDeclarations.firstOrNull()
        } else {
            moduleDeclarations.firstOrNull()
        }
        val idCtx = moduleDecl?.module_identifier()
        if (idCtx != null) {
            val range = basicOffsetProvider(idCtx, tokenStream)
            if (range != null) {
                return content.replaceRange(range, newName)
            }
        }
    } catch (_: Exception) {
    }

    val regex = if (oldModuleName != null) {
        Regex("""\b(module)\s+($oldModuleName|\w+)""")
    } else {
        Regex("""\b(module)\s+([a-zA-Z0-9_]+)""")
    }
    val match = regex.find(content) ?: return content
    return content.replaceRange(match.range, "${match.groupValues[1]} $newName")
}

fun Project.addLucidModule(moduleName: String) = addSourceFile(
    name = "$moduleName.${Languages.Lucid.extension}",
    contents = """
        module $moduleName (
            input clk,  // clock
            input rst,  // reset
            output out
        ) {
            always {
                out = 0
            }
        }
    """.trimIndent()
)

fun Project.addLucidTestBench(name: String) = addSourceFile(
    name = "$name.${Languages.Lucid.extension}",
    contents = """
        testbench $name {
            sig clk
        
            fun tick_clock() {
                clk = 1
                ${'$'}silent_tick() // tick without capturing signals
                clk = 0
                ${'$'}tick()
            }
        
            test myTest {
                clk = 0 // initialize the value
                ${'$'}tick() // capture initial state
        
                // test goes here
            }
        }
    """.trimIndent()
)

fun Project.addVerilogModule(moduleName: String) = addSourceFile(
    name = "$moduleName.${Languages.Verilog.extension}",
    contents = """
        module $moduleName (
            input clk,  // clock
            input rst,  // reset
            output reg out
        );
            always @* begin
                out = 0;
            end
        endmodule
    """.trimIndent()
)

fun Project.addAlchitryConstraint(name: String) = addConstraintFile(
    name = "$name.${Languages.ACF.extension}",
    contents = """
        STANDARD(LVCMOS33) { // standard 3.3V IO standard
            // add pin constraints here
            // pin SIGNAL PORT OPTIONAL_ATTRIBUTES
        }
    """.trimIndent()
)

private fun Project.addSourceFile(name: String, contents: String): SourceFile? {
    try {
        val newFilePath = sourceDirectory.resolve(name)
        newFilePath.createParentDirectories()
        newFilePath.toFile().writeText(contents)
        val newSourceFile = SourceFile(FileProvider.DiskFile(path.relativize(newFilePath)))
        val newSourceFiles = data.sourceFiles.toMutableSet().apply {
            add(newSourceFile)
        }
        val newProj = copy(data = data.copy(sourceFiles = newSourceFiles))
        newProj.save()
        Project.open(newProj)
        return newSourceFile
    } catch (e: Exception) {
        Log.showError("Failed to create source file!", e)
    }
    return null
}

private fun Project.addConstraintFile(name: String, contents: String = ""): ConstraintFile? {
    try {
        val newFilePath = constraintDirectory.resolve(name)
        newFilePath.createParentDirectories()
        newFilePath.toFile().writeText(contents)
        val newConstraintFile = ConstraintFile(FileProvider.DiskFile(path.relativize(newFilePath)))
        val newConstraintFiles = data.constraintFiles.toMutableSet().apply {
            add(newConstraintFile)
        }
        val newProj = copy(data = data.copy(constraintFiles = newConstraintFiles))
        newProj.save()
        Project.open(newProj)
        return newConstraintFile
    } catch (e: Exception) {
        Log.showError("Failed to create constraint file!", e)
    }
    return null
}

fun Project.addComponents(components: List<Component>) {
    val newSourceFiles = data.sourceFiles.toMutableSet()
    val newConstraintFiles = data.constraintFiles.toMutableSet()

    components.filter { component -> !this.components.contains(component) }.forEach { component ->
        when (component.language) {
            Languages.ACF, Languages.PCF, Languages.SDC, Languages.XDC ->
                newConstraintFiles.add(ConstraintFile(component))

            Languages.Lucid, Languages.Verilog -> newSourceFiles.add(SourceFile(component))
        }
    }

    val newProj = copy(data = data.copy(constraintFiles = newConstraintFiles, sourceFiles = newSourceFiles))
    newProj.save()
    Project.open(newProj)
}

fun Project.replaceIpCores(cores: Set<IPCore>) {
    val newProj = copy(data = data.copy(ipCores = cores.toSet()))
    newProj.save()
    Project.open(newProj)
}