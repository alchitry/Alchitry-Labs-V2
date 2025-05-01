package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.WalkerFilter
import com.alchitry.labs2.parsers.grammar.LucidParser
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.RuleNode

object LucidWalkerFilters {
    val SkipGlobals = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return child is LucidParser.GlobalContext
        }
    }

    val SkipControlBlocks = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return (
                    (child is LucidParser.BlockContext && parent !is LucidParser.RepeatBlockContext)
                            || child is LucidParser.CaseBlockContext
                            || child is LucidParser.RepeatBlockContext
                    )
                    && parent !is LucidParser.AlwaysBlockContext
                    && parent !is LucidParser.TestBlockContext
        }
    }

    val SkipRepeatBlocks = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return child is LucidParser.RepeatBlockContext
        }
    }

    val SkipModuleBodies = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return child is LucidParser.ModuleBodyContext
        }
    }

    val GlobalsOnly = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return child is LucidParser.ModuleContext || child is LucidParser.TestBenchContext
        }
    }

    val ModulesOnly = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return child is LucidParser.GlobalContext || child is LucidParser.TestBenchContext
        }
    }

    val TestBenchesOnly = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return child is LucidParser.ModuleContext || child is LucidParser.GlobalContext
        }
    }


}