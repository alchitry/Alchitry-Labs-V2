package com.alchitry.labs2.parsers.hdl.verilog.parsers

import com.alchitry.labs2.parsers.WalkerFilter
import com.alchitry.labs2.parsers.grammar.VerilogParser
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.RuleNode

object VerilogWalkerFilters {
    val SkipModuleBodies = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return child is VerilogParser.Module_itemContext
        }
    }

    val ModulesOnly = object : WalkerFilter {
        override fun shouldSkip(parent: RuleNode, child: ParseTree): Boolean {
            return child is VerilogParser.Udp_declarationContext || child is VerilogParser.Config_declarationContext
        }
    }
}