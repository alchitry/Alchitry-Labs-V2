package com.alchitry.labs2.ui.code_editor.autocomplete

import com.alchitry.labs2.parsers.findFinalNode
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.hdl.types.Function
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.ModuleInstanceArray
import com.alchitry.labs2.parsers.hdl.types.TestOrModuleInstance
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
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
    private fun getContextType(context: ParseTree): ContextType {
        if (context is TerminalNode && context.symbol.type == LucidLexer.Tokens.FUNCTION_ID) {
            return ContextType.FUNCTION
        }

        val nameContext = context.getParent() as? LucidParser.NameContext ?: return ContextType.INVALID
        val parent = nameContext.getParent()
        if (parent is LucidParser.SignalContext) {
            return when (parent.getParent()) {
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

    override suspend fun getPossible(offset: Int): List<String>? {
        val file = state.file

        if (file !is SourceFile || file.language != Languages.Lucid) {
            reset()
            return null
        }

        val text = state.getText()
        return withContext(Dispatchers.Default) {
            ensureActive()
            val tokenStream =
                CommonTokenStream(LucidLexer(CharStreams.fromString(text)).apply { removeErrorListeners() })
            val tree = LucidParser(tokenStream).apply { removeErrorListeners() }.source()
            ensureActive()
            val node = tree.findFinalNode(tokenStream, offset)
            val project = Project.current?.getTypesForLucid(file, tree) ?: return@withContext null
            val type = getContextType(node)
            ensureActive()

            val thisModule: TestOrModuleInstance =
                project.top?.firstInstanceOfFile(state.file) ?: project.getTestBenches()
                    .firstOrNull { it.sourceFile == state.file } ?: return@withContext emptyList()

            return@withContext when (type) {
                ContextType.INVALID -> emptyList()
                ContextType.READ_SIG -> {
                    val list = mutableListOf<String>()
                    list.addAll(
                        thisModule.context.types.resolveLocalSignals(node).mapNotNull {
                            if (it.direction.canRead) it.name else null
                        }
                    )
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
                    list.addAll(
                        thisModule.context.types.resolveLocalSignals(node).mapNotNull {
                            if (it.direction.canWrite) it.name else null
                        }
                    )
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
                    project.getModules().mapNotNull {
                        if (thisModule !is ModuleInstance || it.name != thisModule.module.name)
                            it.name else null
                    }
                }

                ContextType.FUNCTION -> Function.builtIn.map { "$" + it.label }
            }
        }
    }
}