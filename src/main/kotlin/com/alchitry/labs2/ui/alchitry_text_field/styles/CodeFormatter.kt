package com.alchitry.labs2.ui.alchitry_text_field.styles

import com.alchitry.labs2.parsers.findFinalNode
import com.alchitry.labs2.parsers.grammar.BracketLexer
import com.alchitry.labs2.parsers.grammar.BracketParser
import com.alchitry.labs2.project.Language
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.ui.alchitry_text_field.styles.acf.AcfFormatter
import com.alchitry.labs2.ui.alchitry_text_field.styles.lucid.LucidFormatter
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.TokenStream
import org.antlr.v4.kotlinruntime.tree.ParseTree

/**
 * Interface for line indenters.
 */
interface CodeFormatter {

    /**
     * Returns the indent prefix string for the specified line number.
     *
     * @param line The line number for which the indent string is required.
     * @return The indent prefix string for the specified line number.
     */
    fun getIndentFor(textProvider: TextProvider, line: Int): String

    /**
     * Formats the entire file.
     *
     * @return The formatted text.
     */
    fun formatAll(textProvider: TextProvider): String

    companion object {
        const val INDENT_STRING = "    "

        /**
         * Returns a string consisting of a specified number of indents.
         *
         * @param indents The number of indents to include in the indent string.
         * @return The indent string with the specified number of indents.
         */
        fun indentString(indents: Int): String = INDENT_STRING.repeat(indents.coerceAtLeast(0))

        fun countStartingWhitespace(string: String): Int {
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

        fun forLanguage(language: Language): CodeFormatter {
            return when (language) {
                Languages.Lucid -> LucidFormatter()
                Languages.ACF -> AcfFormatter()
                Languages.Verilog -> BracketIndenter()
                else -> BasicIndenter()
            }
        }
    }
}

interface TextProvider {
    fun getText(): String
    fun getLine(line: Int): String?
    fun getLineCount(): Int
}


/**
 * A basic implementation of the [CodeFormatter] interface that simply provides the previous line's indent count.
 *
 * @property codeEditorState The state of the code editor.
 */
class BasicIndenter : CodeFormatter {
    override fun getIndentFor(textProvider: TextProvider, line: Int): String {
        val previousLine = textProvider.getLine(line - 1) ?: return ""
        return CodeFormatter.indentString(
            CodeFormatter.countStartingWhitespace(previousLine) / CodeFormatter.INDENT_STRING.length
        )
    }

    override fun formatAll(textProvider: TextProvider): String = textProvider.getText()
}


open class BracketIndenter : CodeFormatter {
    private fun bracketOffsetProvider(parseTree: ParseTree, tokenStream: TokenStream): IntRange {
        val interval = parseTree.sourceInterval
        val startOffset: Int
        val endOffset: Int
        when (parseTree) {
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
        return startOffset..endOffset
    }

    private fun findFinalNode(parseTree: ParseTree, tokenStream: TokenStream, location: Int): ParseTree {
        val tree = parseTree.findFinalNode(tokenStream, location, ::bracketOffsetProvider)
        if (tree.childCount > 0 && tree is BracketParser.BlockContext) {
            return tree.getParent() ?: tree
        }
        return tree
    }

    private inline fun <reified T : ParseTree> countTypeInHierarchy(parseTree: ParseTree): Int {
        var current: ParseTree? = parseTree
        var count = 0
        while (current != null) {
            if (current is T) {
                count += 1
            }
            current = current.getParent()
        }
        return count
    }

    override fun getIndentFor(textProvider: TextProvider, line: Int): String {
        val text = textProvider.getText()
        val lineOffset = (0..<line).sumOf { (textProvider.getLine(it)?.length ?: 0) + 1 }
        val lineText = textProvider.getLine(line) ?: return ""
        val tokenStream = CommonTokenStream(BracketLexer(CharStreams.fromString(text)).apply { removeErrorListeners() })
        val source = BracketParser(tokenStream).apply { removeErrorListeners() }.source()
        val node = findFinalNode(source, tokenStream, lineOffset + CodeFormatter.countStartingWhitespace(lineText))
        val indents = countTypeInHierarchy<BracketParser.BlockContext>(node)
        return CodeFormatter.indentString(indents)
    }

    override fun formatAll(textProvider: TextProvider): String {
        val text = textProvider.getText()
        val tokenStream = CommonTokenStream(BracketLexer(CharStreams.fromString(text)).apply { removeErrorListeners() })
        val source = BracketParser(tokenStream).apply { removeErrorListeners() }.source()
        var lineOffset = 0
        return buildString {
            for (idx in 0..textProvider.getLineCount()) {
                val line = textProvider.getLine(idx) ?: continue
                val node =
                    findFinalNode(
                        source,
                        tokenStream,
                        lineOffset + CodeFormatter.countStartingWhitespace(line)
                    )
                lineOffset += line.length + 1
                val indents = countTypeInHierarchy<BracketParser.BlockContext>(node)

                val lineText = line.trim()
                if (lineText.isNotBlank()) {
                    append(CodeFormatter.indentString(indents))
                    append(lineText)
                }
                if (idx != textProvider.getLineCount() - 1)
                    appendLine()
            }
        }
    }
}