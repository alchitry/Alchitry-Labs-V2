package com.alchitry.labs2.ui.alchitry_text_field.autocomplete

import com.alchitry.labs2.parsers.findFinalNode
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.hdl.types.Function
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.ModuleInstanceArray
import com.alchitry.labs2.parsers.hdl.types.TestOrModuleInstance
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.values.StructWidth
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.files.SourceFile
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
    CASE_LABEL_OR_SIG,
    MODULE_INST,
    FUNCTION
}


class LucidAutocomplete(val file: SourceFile) : Autocomplete() {
    init {
        check(file.language == Languages.Lucid) { "LucidAutocomplete only works on Lucid files." }
    }
    private fun getContextType(context: ParseTree): ContextType {
        if (context is TerminalNode && context.symbol.type == LucidLexer.Tokens.FUNCTION_ID) {
            return ContextType.FUNCTION
        }

        val nameContext = if (context is TerminalNode && context.getParent() is LucidParser.SignalContext) {
            // this implies we are at the "." after a signal like "signal."
            (context.getParent() as LucidParser.SignalContext).name(0)
        } else {
            context.getParent() as? LucidParser.NameContext
        } ?: return ContextType.INVALID
        val parent = nameContext.getParent()
        if (parent is LucidParser.SignalContext) {
            return when (val grandparent = parent.getParent()) {
                is LucidParser.AssignStatContext -> ContextType.WRITE_SIG
                is LucidParser.ExprContext -> {
                    val ggp = grandparent.getParent()
                    if (ggp is LucidParser.CaseElemContext && ggp.getChild(1)?.text != ":")
                        ContextType.CASE_LABEL_OR_SIG // ambiguous at this point
                    else
                        ContextType.READ_SIG
                }

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

    private fun nameOrExpand(name: String, width: SignalWidth): List<String> {
        val list = mutableListOf(name)
        if (width is StructWidth) {
            list.addAll(width.type.keys.map { element -> "${name}.$element" })
        }
        return list
    }

    override suspend fun getPossible(source: AutocompleteSource, offset: Int): List<String>? {
        val text = source.getText()
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
                project.top?.firstInstanceOfFile(file) ?: project.getTestBenches()
                    .firstOrNull { it.sourceFile == file } ?: return@withContext emptyList()

            fun getConstants(): List<String> {
                val list = mutableListOf<String>()
                thisModule.context.project.getGlobals().values.forEach { global ->
                    list.addAll(global.constants.values.flatMap { nameOrExpand("${global.name}.${it.name}", it.width) })
                }
                list.addAll(thisModule.context.constant.localConstants.values.flatMap {
                    nameOrExpand(
                        it.name,
                        it.width
                    )
                })
                list.addAll(thisModule.context.enum.localEnumType.values.flatMap { enum -> enum.members.map { "${enum.name}.$it" } })
                return list
            }

            fun getReadableSignals(): List<String> {
                val list = mutableListOf<String>()
                list.addAll(
                    thisModule.context.types.resolveLocalSignals(node).flatMap {
                        if (it.direction.canRead) nameOrExpand(it.name, it.width) else emptyList()
                    }
                )
                list.addAll(thisModule.context.types.sigs.values.flatMap {
                    nameOrExpand(it.name, it.width)
                })
                list.addAll(thisModule.context.types.dffs.values.flatMap {
                    nameOrExpand("${it.name}.q", it.q.width)
                })
                if (thisModule is ModuleInstance) {
                    list.addAll(
                        thisModule.module.ports.values.flatMap {
                            if (it.direction.canRead) nameOrExpand(it.name, it.width) else emptyList()
                        }
                    )
                    list.addAll(
                        thisModule.context.types.moduleInstances.flatMap { inst ->
                            inst.value.external.values.flatMap {
                                if (it.direction.canRead) nameOrExpand(
                                    "${inst.value.name}.${it.name}",
                                    it.width
                                ) else emptyList()
                            }
                        }
                    )
                }
                return list
            }

            fun getWritableSignals(): List<String> {
                val list = mutableListOf<String>()
                list.addAll(
                    thisModule.context.types.resolveLocalSignals(node).flatMap {
                        if (it.direction.canWrite) nameOrExpand(it.name, it.width) else emptyList()
                    }
                )
                list.addAll(thisModule.context.types.sigs.values.flatMap {
                    nameOrExpand(it.name, it.width)
                })
                list.addAll(thisModule.context.types.dffs.values.flatMap {
                    nameOrExpand("${it.name}.d", it.d.width)
                })
                if (thisModule is ModuleInstance) {
                    list.addAll(
                        thisModule.module.ports.values.flatMap {
                            if (it.direction.canWrite) nameOrExpand(it.name, it.width) else emptyList()
                        }
                    )
                    list.addAll(
                        thisModule.context.types.moduleInstances.flatMap { inst ->
                            inst.value.external.values.flatMap {
                                if (it.direction.canWrite) nameOrExpand(
                                    "${inst.value.name}.${it.name}",
                                    it.width
                                ) else emptyList()
                            }
                        }
                    )
                }
                return list
            }

            return@withContext when (type) {
                ContextType.INVALID -> emptyList()
                ContextType.READ_SIG -> getReadableSignals() + getConstants()
                ContextType.WRITE_SIG -> getWritableSignals()
                ContextType.CASE_LABEL_OR_SIG -> getWritableSignals() + getConstants()

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