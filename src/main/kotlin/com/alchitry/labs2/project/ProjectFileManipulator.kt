package com.alchitry.labs2.project

import com.alchitry.labs2.Log
import com.alchitry.labs2.project.files.*
import com.alchitry.labs2.ui.tabs.Workspace
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