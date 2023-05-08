package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode

interface WalkerFilter {
    fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean

    companion object {
        fun join(vararg filters: WalkerFilter): WalkerFilter {
            return object : WalkerFilter {
                override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                    return filters.any { it.shouldSkip(parent, child) }
                }
            }
        }

        val None = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean = false
        }

        val SkipGlobals = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                return child is GlobalContext
            }
        }

        val SkipControlBlocks = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                return (child is LucidParser.BlockContext || child is LucidParser.CaseBlockContext)
                        && parent !is LucidParser.AlwaysBlockContext
            }
        }

        val SkipModuleBodies = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                return child is ModuleBodyContext
            }
        }

        val GlobalsOnly = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                return child is ModuleContext
            }
        }
    }
}