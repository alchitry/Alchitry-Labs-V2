package com.alchitry.labs2.ui.code_editor.styles

import com.alchitry.labs2.parsers.grammar.BracketLexer
import com.alchitry.labs2.parsers.grammar.BracketParser
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.TokenStream
import org.antlr.v4.kotlinruntime.tree.ParseTree

/**
 * Interface for line indenters.
 */
interface LineIndenter {

    /**
     * Returns the indent prefix string for the specified line number.
     *
     * @param line The line number for which the indent string is required.
     * @return The indent prefix string for the specified line number.
     */
    fun getIndentFor(line: Int): String

    /**
     * Indents every line using the line indenter.
     *
     * @return The indented text.
     */
    fun indentAll(): String

    companion object {
        const val INDENT_STRING = "    "

        /**
         * Returns a string consisting of a specified number of indents.
         *
         * @param indents The number of indents to include in the indent string.
         * @return The indent string with the specified number of indents.
         */
        fun indentString(indents: Int): String = INDENT_STRING.repeat(indents.coerceAtLeast(0))

        fun whitespaceStarting(string: String): Int {
            var counter = 0
            for (c in string.toCharArray()) {
                if (c.isWhitespace()) {
                    counter++
                } else {
                    break
                }
            }
            return counter
        }
    }
}


/**
 * A basic implementation of the [LineIndenter] interface that simply provides the previous line's indent count.
 *
 * @property codeEditorState The state of the code editor.
 */
class BasicIndenter(private val codeEditorState: CodeEditorState) : LineIndenter {
    override fun getIndentFor(line: Int): String {
        val previousLine = codeEditorState.lines.getOrNull(line - 1)?.text?.text ?: return ""
        return LineIndenter.indentString(
            LineIndenter.whitespaceStarting(previousLine) / LineIndenter.INDENT_STRING.length
        )
    }

    override fun indentAll(): String = codeEditorState.getText()
}


class BracketIndenter(private val codeEditorState: CodeEditorState) : LineIndenter {
    private fun findFinalNode(parseTree: ParseTree, tokenStream: TokenStream, location: Int): ParseTree {
        for (i in 0..<parseTree.childCount) {
            val child = parseTree.getChild(i) ?: error("getChild() failed for a child that should exist!")
            val interval = child.sourceInterval
            val startOffset: Int
            val endOffset: Int
            when (child) {
                is BracketParser.ParenBlockContext, is BracketParser.CurlyBlockContext, is BracketParser.SquareBlockContext -> {
                    startOffset = tokenStream[interval.a].startIndex + 1
                    endOffset = tokenStream[interval.b].stopIndex - 1
                }

                is BracketParser.CommentBlockContext -> {
                    startOffset = tokenStream[interval.a].startIndex + 2
                    endOffset = tokenStream[interval.b].stopIndex - 2
                }

                else -> {
                    startOffset = tokenStream[interval.a].startIndex
                    endOffset = tokenStream[interval.b].stopIndex
                }
            }
            val adjEndOffset = if (endOffset < 0) Int.MAX_VALUE else endOffset
            if (location in startOffset..adjEndOffset) {
                return findFinalNode(child, tokenStream, location)
            }
        }
        if (parseTree.childCount > 0 && parseTree is BracketParser.BlockContext) {
            return parseTree.readParent() ?: parseTree
        }
        return parseTree
    }

    private inline fun <reified T : ParseTree> countTypeInHierarchy(parseTree: ParseTree): Int {
        var current: ParseTree? = parseTree
        var count = 0
        while (current != null) {
            if (current is T) {
                count += 1
            }
            current = current.readParent()
        }
        return count
    }

    override fun getIndentFor(line: Int): String {
        val text = codeEditorState.getText()
        val lineOffset = codeEditorState.lines.subList(0, line).sumOf { it.text.length + 1 }
        val tokenStream = CommonTokenStream(BracketLexer(CharStreams.fromString(text)))
        val source = BracketParser(tokenStream).source()
        val node = findFinalNode(source, tokenStream, lineOffset)
        val indents = countTypeInHierarchy<BracketParser.BlockContext>(node)
        return LineIndenter.indentString(indents)
    }

    override fun indentAll(): String {
        val text = codeEditorState.getText()
        val tokenStream = CommonTokenStream(BracketLexer(CharStreams.fromString(text)))
        val source = BracketParser(tokenStream).source()
        var lineOffset = 0
        return buildString {
            codeEditorState.lines.forEachIndexed { idx, line ->
                val node =
                    findFinalNode(source, tokenStream, lineOffset + LineIndenter.whitespaceStarting(line.text.text))
                lineOffset += line.text.length + 1
                val indents = countTypeInHierarchy<BracketParser.BlockContext>(node)

                val lineText = line.text.text.trim()
                if (lineText.isNotBlank()) {
                    append(LineIndenter.indentString(indents))
                    append(lineText)
                }
                if (idx != codeEditorState.lines.size - 1)
                    appendLine()
            }
        }
    }
}