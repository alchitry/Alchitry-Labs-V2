package com.alchitry.labs2.project

import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.ui.tabs.Workspace

fun Project.removeFile(file: ProjectFile, delete: Boolean) {
    if (delete && !file.isReadOnly && !file.isLibFile && file.file is FileProvider.DiskFile) {
        file.file.file.delete()
    }
    Workspace.closeFile(file, false)
    val newSourceFiles = sourceFiles.toMutableSet().apply { remove(file) }
    val newConstraintFiles = constraintFiles.toMutableSet().apply { remove(file) }
    val newProj = copy(sourceFiles = newSourceFiles, constraintFiles = newConstraintFiles)
    newProj.saveXML()
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
                    out = 0;
                  }
                }
            """.trimIndent()
)

fun Project.addLucidTestBench(name: String) = addSourceFile(
    name = "$name.${Languages.Lucid.extension}",
    contents = """
        testBench $name {
          sig clk

          fun tickClock() {
            clk = 1
            ${"$"}silentTick() // tick without capturing signals
            clk = 0
            ${"$"}tick()
          }

          test myTest {
            clk = 0 // initialize the value
            ${"$"}tick() // capture initial state

            // test goes here
          }
        }
    """.trimIndent()
)

fun Project.addAlchitryConstraint(name: String) = addConstraintFile("$name.${Languages.ACF.extension}")

private fun Project.addSourceFile(name: String, contents: String): SourceFile {
    val newFile = sourceDirectory.resolve(name)
    newFile.writeText(contents)
    val newSourceFile = SourceFile(FileProvider.DiskFile(newFile))
    val newSourceFiles = sourceFiles.toMutableSet().apply {
        add(newSourceFile)
    }
    val newProj = copy(sourceFiles = newSourceFiles)
    newProj.saveXML()
    Project.open(newProj)
    return newSourceFile
}

private fun Project.addConstraintFile(name: String, contents: String = ""): ConstraintFile {
    val newFile = constraintDirectory.resolve(name)
    newFile.writeText(contents)
    val newConstraintFile = ConstraintFile(FileProvider.DiskFile(newFile))
    val newConstraintFiles = constraintFiles.toMutableSet().apply {
        add(newConstraintFile)
    }
    val newProj = copy(constraintFiles = newConstraintFiles)
    newProj.saveXML()
    Project.open(newProj)
    return newConstraintFile
}