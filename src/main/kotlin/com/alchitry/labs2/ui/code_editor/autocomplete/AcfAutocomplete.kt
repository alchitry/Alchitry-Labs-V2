package com.alchitry.labs2.ui.code_editor.autocomplete

import com.alchitry.labs2.parsers.findFinalNode
import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.parsers.grammar.AcfParser
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

class AcfAutocomplete(state: CodeEditorState) : Autocomplete(state) {

    override suspend fun getPossible(offset: Int): List<String>? {
        val text = state.getText()
        return withContext(Dispatchers.Default) {
            ensureActive()
            val tokenStream = CommonTokenStream(AcfLexer(CharStreams.fromString(text)))
            val tree = AcfParser(tokenStream).source()
            ensureActive()
            val node = tree.findFinalNode(tokenStream, offset)

            val nameContext = node.getParent() as? AcfParser.NameContext ?: return@withContext null
            val parent = nameContext.getParent()
            if (parent is AcfParser.PortNameContext) {
                ensureActive()
                val topFile = Project.current?.top ?: return@withContext null
                val lucidStream = CommonTokenStream(LucidLexer(CharStreams.fromString(topFile.readText())))
                val lucidTree = LucidParser(lucidStream).source()
                ensureActive()
                val project = Project.current?.getTypesForLucid(topFile, lucidTree) ?: return@withContext null

                return@withContext project.top?.ports?.let { ports ->
                    ports.values.map { it.name }
                }
            }

            return@withContext null
        }
    }
}