package com.alchitry.labs.project

import com.alchitry.labs.project.files.ConstraintFile
import com.alchitry.labs.project.files.FileProvider
import com.alchitry.labs.project.files.SourceFile

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