package com.alchitry.labs2.ui.code_editor.autocomplete

import com.alchitry.labs2.Log
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.findFinalNode
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.types.Function
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstanceArray
import com.alchitry.labs2.parsers.lucidv2.types.TestOrModuleInstance
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import kotlinx.coroutines.*
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.TerminalNode

private enum class ContextType {
    INVALID,
    READ_SIG,
    WRITE_SIG,
    MODULE_INST,
    FUNCTION
}

class LucidAutocomplete(state: CodeEditorState) : Autocomplete(state) {
    private var context: ContextType? = null
    private var updateJob: Job? = null

    override fun reset() {
        super.reset()
        state.scope.launch {
            updateJob?.cancelAndJoin()
            context = null
        }
    }

    private fun String.getRelevantText(): String {
        for (i in length - 1 downTo 0) {
            if (!this[i].isLetterOrDigit() && this[i] != '_' && this[i] != '.' && this[i] != '$') {
                return substring(i + 1)
            }
        }
        return this
    }

    private fun getContextType(context: ParseTree): ContextType {
        if (context is TerminalNode && context.symbol?.type == LucidLexer.Tokens.FUNCTION_ID.id) {
            return ContextType.FUNCTION
        }

        val nameContext = context.readParent() as? LucidParser.NameContext ?: return ContextType.INVALID
        val parent = nameContext.readParent()
        if (parent is LucidParser.SignalContext) {
            return when (parent.readParent()) {
                is LucidParser.AssignStatContext -> ContextType.WRITE_SIG
                is LucidParser.ExprContext -> ContextType.READ_SIG
                else -> ContextType.INVALID
            }
        }

        if (parent is LucidParser.ModuleInstContext) {
            if (parent.children?.getOrNull(0) === nameContext) {
                return ContextType.MODULE_INST
            }
            return ContextType.INVALID
        }

        return ContextType.INVALID
    }

    private fun ModuleInstance.firstInstanceOfFile(file: ProjectFile): ModuleInstance? {
        if (this.module.sourceFile == file)
            return this
        this.context.types.moduleInstances.values.forEach {
            when (it) {
                is ModuleInstance -> it
                is ModuleInstanceArray -> it.modules.first()
            }.firstInstanceOfFile(file)?.let { moduleInstance -> return moduleInstance }
        }
        return null
    }

    private fun getPossible(contextType: ContextType, projectContext: ProjectContext): List<String> {
        val thisModule: TestOrModuleInstance =
            projectContext.top?.firstInstanceOfFile(state.file) ?: projectContext.getTestBenches()
                .firstOrNull { it.sourceFile == state.file } ?: return emptyList()

        return when (contextType) {
            ContextType.INVALID -> emptyList()
            ContextType.READ_SIG -> {
                val list = mutableListOf<String>()
                list.addAll(thisModule.context.types.sigs.keys)
                list.addAll(thisModule.context.types.dffs.keys.map { "$it.q" })
                if (thisModule is ModuleInstance) {
                    list.addAll(
                        thisModule.module.ports.values.mapNotNull {
                            if (it.direction.canRead) it.name else null
                        }
                    )
                    list.addAll(
                        thisModule.context.types.moduleInstances.flatMap { inst ->
                            inst.value.external.values.mapNotNull {
                                if (it.direction.canRead) "${inst.value.name}.${it.name}" else null
                            }
                        }
                    )
                }
                list
            }

            ContextType.WRITE_SIG -> {
                val list = mutableListOf<String>()
                list.addAll(thisModule.context.types.sigs.keys)
                list.addAll(thisModule.context.types.dffs.keys.map { "$it.d" })
                if (thisModule is ModuleInstance) {
                    list.addAll(
                        thisModule.module.ports.values.mapNotNull {
                            if (it.direction.canWrite) it.name else null
                        }
                    )
                    list.addAll(
                        thisModule.context.types.moduleInstances.flatMap { inst ->
                            inst.value.external.values.mapNotNull {
                                if (it.direction.canWrite) "${inst.value.name}.${it.name}" else null
                            }
                        }
                    )
                }
                list
            }

            ContextType.MODULE_INST -> {
                projectContext.getModules().mapNotNull {
                        if (thisModule !is ModuleInstance || it.name != thisModule.module.name)
                            it.name else null
                    }
            }

            ContextType.FUNCTION -> Function.builtIn.map { "$" + it.label }
        }
    }

    private suspend fun buildTree() = coroutineScope {
        val caret = state.selectionManager.caret
        val lineOffset = state.lines.subList(0, caret.line).sumOf { it.text.length + 1 }
        val offset = lineOffset + caret.offset - 1

        val text = state.getText()
        val context = context ?: withContext(Dispatchers.Default) {
            ensureActive()
            val tokenStream = CommonTokenStream(LucidLexer(CharStreams.fromString(text)))
            val source = LucidParser(tokenStream).source()
            ensureActive()
            getContextType(source.findFinalNode(tokenStream, offset))
        }
        ensureActive()
        this@LucidAutocomplete.context = context

        val file = state.file

        if (file !is SourceFile || file.language != Languages.Lucid) {
            reset()
            return@coroutineScope
        }
        val projectContext = Project.current?.getTypesForLucidFile(file)
        if (projectContext == null) {
            reset()
            return@coroutineScope
        }

        val line = state.lines.getOrNull(caret.line)?.text?.text ?: return@coroutineScope
        val relevantText = line.substring(0, caret.offset).getRelevantText()
        if (relevantText.isBlank()) {
            reset()
            return@coroutineScope
        }
        val pieces = relevantText.split('.')

        val start = caret.copy(offset = caret.offset - relevantText.length)
        val end = caret
        val possible = getPossible(context, projectContext)

        active = true

        this@LucidAutocomplete.suggestions = possible.mapNotNull {
            val diff = calculateEditDistance(it, pieces.last()) ?: return@mapNotNull null
            Suggestion(it, diff, start..<end)
        }.sortedBy { it.quality }

        overlayPosition = state.textPositionToScreenOffset(caret.copy(offset = caret.offset - relevantText.length))
    }

    override fun updateSuggestions() {
        updateJob?.cancel()
        updateJob = state.scope.launch {
            try {
                buildTree()
            } catch (e: Exception) {
                if (e is CancellationException) return@launch
                Log.exception(e)
            }
        }
    }
}