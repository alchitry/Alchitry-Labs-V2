package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode

interface WalkerFilter {
    fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean

    companion object {
        val None = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean = false
        }

        val SkipControlBlocks = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                return (child is LucidParser.BlockContext || child is LucidParser.CaseBlockContext)
                        && parent !is LucidParser.AlwaysBlockContext
            }
        }
    }
}