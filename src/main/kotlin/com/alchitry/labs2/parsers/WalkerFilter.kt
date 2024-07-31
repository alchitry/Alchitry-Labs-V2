package com.alchitry.labs2.parsers

import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.RuleNode

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
    }
}