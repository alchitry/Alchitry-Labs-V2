package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.grammar.LucidParser.ModuleContext
import com.alchitry.labs2.parsers.hdl.types.ports.Port
import com.alchitry.labs2.project.files.SourceFile

data class Module(
    val name: String,
    val parameters: Map<String, Parameter>,
    val ports: Map<String, Port>,
    val context: ModuleContext,
    val sourceFile: SourceFile
)

