package com.alchitry.labs.parsers.lucidv2.parsers

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
                return (
                        (child is BlockContext && parent !is RepeatBlockContext)
                                || child is CaseBlockContext
                                || child is RepeatBlockContext
                        )
                        && parent !is AlwaysBlockContext
                        && parent !is TestBlockContext
            }
        }

        val SkipModuleBodies = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                return child is ModuleBodyContext
            }
        }

        val GlobalsOnly = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                return child is ModuleContext || child is TestBenchContext
            }
        }

        val TestBenchesOnly = object : WalkerFilter {
            override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
                return child is ModuleContext || child is GlobalContext
            }
        }
    }
}