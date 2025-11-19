package com.alchitry.labs2.ui.code_editor.styles.lucid

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import com.alchitry.labs2.ui.code_editor.styles.CodeFormatter
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.ParseTreeWalker
import org.antlr.v4.kotlinruntime.tree.TerminalNode

class LucidFormatter(private val codeEditorState: CodeEditorState) : CodeFormatter {
    override fun getIndentFor(line: Int): String {
        //val listener = ConsoleErrorListener.INSTANCE
        val lexer = LucidLexer(CharStreams.fromString(codeEditorState.getText())).apply { removeErrorListeners() }
        val tokens = lexer.allTokens
        lexer.reset()
        val parser = LucidParser(CommonTokenStream(lexer)).apply { removeErrorListeners() }
        val tree = parser.source()

        val indents = buildIndentList(tokens, tree)

        val firstDefaultTokenIdx =
            tokens.indexOfFirst { it.channel == LucidLexer.Channels.DEFAULT_TOKEN_CHANNEL && it.line == line + 1 }
        if (firstDefaultTokenIdx == -1) {
            val previousLine = codeEditorState.lines.getOrNull(line - 1)?.text?.text ?: return ""
            return " ".repeat(CodeFormatter.countStartingWhitespace(previousLine))
        }
        return CodeFormatter.indentString(indents[firstDefaultTokenIdx])
    }

    override fun formatAll(): String {
        val cleanedText = removeSemicolons(codeEditorState.getText())

        val lexer = LucidLexer(CharStreams.fromString(cleanedText)).apply { removeErrorListeners() }
        val tokens = lexer.allTokens
        lexer.reset()
        val parser = LucidParser(CommonTokenStream(lexer)).apply { removeErrorListeners() }
        val tree = parser.source()

        val indents = buildIndentList(tokens, tree)
        val tokenText = tokens.map { it.text }.toMutableList()

        codeEditorState.lines.indices.forEach { idx ->
            val firstTokenIdx = tokens.indexOfFirst { it.line == idx + 1 }
            val firstDefaultTokenIdx =
                tokens.indexOfFirst { it.channel == LucidLexer.Channels.DEFAULT_TOKEN_CHANNEL && it.line == idx + 1 }
            if (firstTokenIdx == -1 || firstDefaultTokenIdx == -1)
                return@forEach
            val firstToken = tokens[firstTokenIdx]
            if (firstToken.type == LucidLexer.Tokens.WS) {
                tokenText[firstTokenIdx] = CodeFormatter.indentString(indents[firstDefaultTokenIdx])
            } else {
                tokenText[firstTokenIdx] =
                    CodeFormatter.indentString(indents[firstDefaultTokenIdx]) + tokenText[firstTokenIdx]
            }
        }

        return tokenText.joinToString(separator = "")
    }

    private fun List<Token>.nextFromChannel(start: Int, channel: Int): Token? {
        return subList(start + 1, size).firstOrNull { it.channel == channel }
    }

    private fun List<Token>.previousFromChannel(start: Int, channel: Int): Token? {
        return subList(0, start).lastOrNull { it.channel == channel }
    }

    private fun removeSemicolons(text: String): String {
        val lexer = LucidLexer(CharStreams.fromString(text)).apply { removeErrorListeners() }
        val tokens = lexer.allTokens
        val tokenText = tokens.map { it.text }.toMutableList()

        tokens.forEachIndexed { idx, it ->
            when (it.type) {
                LucidLexer.Tokens.SEMICOLON -> {
                    if (tokens.previousFromChannel(
                            idx,
                            LucidLexer.Channels.DEFAULT_TOKEN_CHANNEL
                        )?.type == LucidLexer.Tokens.NL ||
                        tokens.nextFromChannel(
                            idx,
                            LucidLexer.Channels.DEFAULT_TOKEN_CHANNEL
                        )?.type == LucidLexer.Tokens.NL
                    ) {
                        tokenText[idx] = ""
                    }
                }

                LucidLexer.Tokens.NL -> {
                    tokenText[idx] = "\n"
                }
            }
        }

        return tokenText.joinToString(separator = "")
    }


    private fun buildIndentList(tokens: List<Token>, tree: ParseTree): List<Int> {
        val indents = MutableList(tokens.size) { 0 }

        ParseTreeWalker.DEFAULT.walk(object : LucidBaseListener() {
            private fun indent(from: Int, to: Int) {
                for (i in from..to) {
                    indents[i] += 1
                }
            }

            private fun indent(ctx: ParserRuleContext) {
                val start = ctx.start ?: return
                val stop = ctx.stop ?: return
                indent(start.tokenIndex, stop.tokenIndex)
            }

            private fun indentBetween(ctx: ParserRuleContext, start: String, end: String) {
                val children = ctx.children ?: return
                val startToken = (children.firstOrNull { it.text == start } as? TerminalNode)?.symbol ?: return
                val stopToken = (children.lastOrNull { it.text == end } as? TerminalNode)?.symbol ?: ctx.stop ?: return
                indent(startToken.tokenIndex + 1, stopToken.tokenIndex - 1)
            }

            private fun indentExcludeEnds(ctx: ParserRuleContext) {
                val start = ctx.start ?: return
                val stop = ctx.stop ?: return
                indent(start.tokenIndex + 1, stop.tokenIndex - 1)
            }

            override fun exitBlock(ctx: LucidParser.BlockContext) {
                val start = ctx.start ?: return
                val stop = ctx.stop ?: return

                if (start.text == "{") {
                    indent(start.tokenIndex + 1, stop.tokenIndex - 1)
                } else {
                    val stats = ctx.alwaysStat()
                    if (stats.size == 1 && stats.first() is LucidParser.AlwaysIfContext) {
                        val parent = ctx.getParent()
                        if (parent is LucidParser.ElseStatContext) {
                            if (parent.start?.line == start.line) // don't indent "else if"
                                return
                        }
                    }

                    indent(start.tokenIndex, stop.tokenIndex)
                }
            }

            override fun exitModuleBody(ctx: LucidParser.ModuleBodyContext) {
                indentExcludeEnds(ctx)
            }

            override fun exitAssignBlock(ctx: LucidParser.AssignBlockContext) {
                indentBetween(ctx, "{", "}")
            }

            override fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext) {
                indentBetween(ctx, "(", ")")
            }


            override fun exitGlobal(ctx: LucidParser.GlobalContext) {
                indentBetween(ctx, "{", "}")
            }

            override fun exitParamList(ctx: LucidParser.ParamListContext) {
                indentBetween(ctx, "#(", ")")
            }

            override fun exitPortList(ctx: LucidParser.PortListContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitArraySize(ctx: LucidParser.ArraySizeContext) {
                indentExcludeEnds(ctx)
            }

            override fun exitStructType(ctx: LucidParser.StructTypeContext) {
                indentExcludeEnds(ctx)
            }

            override fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitStructConst(ctx: LucidParser.StructConstContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitSigCon(ctx: LucidParser.SigConContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitParamCon(ctx: LucidParser.ParamConContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitEnumDec(ctx: LucidParser.EnumDecContext) {
                indentBetween(ctx, "{", "}")
            }

            override fun exitInstCons(ctx: LucidParser.InstConsContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitStructDec(ctx: LucidParser.StructDecContext) {
                indentBetween(ctx, "{", "}")
            }

            override fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext) {
                indentBetween(ctx, "[", "]")
            }

            override fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) {
                indentExcludeEnds(ctx)
            }

            override fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) {
                indentExcludeEnds(ctx)
            }

            override fun exitCaseStat(ctx: LucidParser.CaseStatContext) {
                indentBetween(ctx, "(", ")")
                indentBetween(ctx, "{", "}")
            }

            override fun exitCaseBlock(ctx: LucidParser.CaseBlockContext) {
                indent(ctx)
            }

            override fun exitIfStat(ctx: LucidParser.IfStatContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitRepeatStat(ctx: LucidParser.RepeatStatContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitFunction(ctx: LucidParser.FunctionContext) {
                indentBetween(ctx, "(", ")")
            }

            override fun exitExprGroup(ctx: LucidParser.ExprGroupContext) {
                indentExcludeEnds(ctx)
            }

            override fun exitExprConcat(ctx: LucidParser.ExprConcatContext) {
                indentExcludeEnds(ctx)
            }

            override fun exitExprDup(ctx: LucidParser.ExprDupContext) {
                indentBetween(ctx, "x{", "}")
            }

            override fun exitExprArray(ctx: LucidParser.ExprArrayContext) {
                indentExcludeEnds(ctx)
            }
        }, tree)

        return indents
    }
}