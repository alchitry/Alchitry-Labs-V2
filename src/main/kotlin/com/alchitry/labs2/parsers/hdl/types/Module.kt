package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.hdl.types.ports.Port
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class Module(
    val name: String,
    val parameters: Map<String, Parameter>,
    val ports: Map<String, Port>,
    val context: ParserRuleContext,
    val sourceFile: SourceFile
)

