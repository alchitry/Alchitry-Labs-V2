package com.alchitry.labs2.ui.code_editor.styles

import com.alchitry.labs2.ui.code_editor.CodeEditorState

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

    companion object {
        const val INDENT_STRING = "    "

        /**
         * Returns a string consisting of a specified number of indents.
         *
         * @param indents The number of indents to include in the indent string.
         * @return The indent string with the specified number of indents.
         */
        fun indentString(indents: Int): String = INDENT_STRING.repeat(indents)

        fun countIndentsIn(string: String): Int {
            var counter = 0
            for (c in string.toCharArray()) {
                if (c.isWhitespace()) {
                    counter++
                } else {
                    break
                }
            }
            return counter / INDENT_STRING.length
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
        return LineIndenter.indentString(LineIndenter.countIndentsIn(previousLine))
    }
}