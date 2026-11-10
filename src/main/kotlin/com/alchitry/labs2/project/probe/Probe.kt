package com.alchitry.labs2.project.probe

import com.alchitry.hardware.Board
import com.alchitry.labs2.Log
import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.ProjectMode
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.hdl.types.LocalSignal
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.ModuleInstanceArray
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.parsers.notations.NotationManager
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.Project.Companion.parseAll
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.project.library.ComponentLibrary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

class Probe(private val project: Project) {
    private var projectContext: ProjectContext? = null
    var selectedSignals: List<SignalNode>? = null
        private set
    var sampleDepth: Int = 0
        private set
    var clock: SignalNode? = null
        private set

    suspend fun buildSignalTree(): SignalTree = withContext(Dispatchers.Default) {
        val notationManager = NotationManager()
        projectContext = project.buildContext(notationManager, mode = ProjectMode.Probe)
        val topModule = projectContext?.top

        if (projectContext == null || topModule == null) {
            error("Failed to parse project!")
        }

        fun buildTree(moduleInstance: ModuleInstance, parent: SignalTree?): SignalTree =
            SignalTree(moduleInstance, parent) { currentTree ->
                val children = mutableListOf<SignalTreeNode>()
                moduleInstance.internal.values.forEach {
                    if (it.direction.canRead) children.add(
                        SignalNode(
                            it,
                            currentTree
                        )
                    )
                }
                moduleInstance.context.types.dffs.values.forEach { children.add(SignalNode(it.q, currentTree)) }
                moduleInstance.context.types.sigs.values.forEach { children.add(SignalNode(it, currentTree)) }
                moduleInstance.context.types.moduleInstances.values.forEach { submoduleInstance ->
                    when (submoduleInstance) {
                        is ModuleInstance -> {
                            submoduleInstance.external.values.forEach {
                                if (it.direction.canRead) children.add(
                                    SignalNode(it, currentTree)
                                )
                            }
                            children.add(buildTree(submoduleInstance, currentTree))
                        }

                        is ModuleInstanceArray -> {
                            submoduleInstance.external.values.forEach {
                                if (it.direction.canRead) children.add(
                                    SignalNode(it, currentTree)
                                )
                            }
                            // TODO: Add support for inside module arrays
                        }
                    }
                }
                return@SignalTree children
            }

        return@withContext buildTree(topModule, null)
    }

    fun setProbeData(selectedSignals: List<SignalNode>, sampleDepth: Int, clock: SignalNode) {
        this.selectedSignals = selectedSignals
        this.sampleDepth = sampleDepth
        this.clock = clock
    }

    suspend fun buildProbeProject() {
        val context = projectContext ?: error("buildSignalTree() must be called before buildProbeProject()!")
        val top = context.top ?: error("Top level module missing from project context!")
        val selectedSignals = selectedSignals ?: error("setProbeData() must be called before buildProbeProject()!")
        val sampleDepth = sampleDepth
        val clock = clock ?: error("setProbeData() must be called before buildProbeProject()!")

        val probedSignalTree = buildSignalTree(selectedSignals)
        val debugModules = mutableListOf<Pair<SourceFile, LucidParser.SourceContext>>()

        val probeComponentName = when (project.data.board) {
            Board.AlchitryAu, Board.AlchitryAuPlus, Board.AlchitryAuV2T35, Board.AlchitryAuV2T50, Board.AlchitryPtV2T100, Board.AlchitryPtV2T200 -> "xilinx_probe"
            Board.AlchitryCu, Board.AlchitryCuV2 -> error("Cu is currently unsupported!")
        }

        fun addDebugFileForModule(currentTree: SignalTree) {
            val instance = currentTree.module
            val context = instance.moduleContext.deepCopy() as? LucidParser.ModuleContext
                ?: error("Probed module context wasn't a Lucid module!")
            val probedSignals = currentTree.children.filterIsInstance<SignalNode>()
            val probedModules = currentTree.children.filterIsInstance<SignalTree>()

            // Replace module name
            context.children!![context.children!!.indexOfFirst { it is LucidParser.NameContext }] =
                parseLucid(instance.probeName, { it.name() })

            val totalBits = totalProbedBitsFor(currentTree)

            if (instance !== top) {
                val portTree = parseLucid("output probe_signals[$totalBits]") { it.portDec() }
                context.portList()!!.children!!.add(1, portTree)

                val alwaysBlock = parseLucid(buildString {
                    append("always probe_signals = c{")
                    probedSignals.forEachIndexed { index, node ->
                        if (index != 0)
                            append(", ")
                        append($$"$flatten(")
                        append(node.signal.qualifiedNameInInstance(instance))
                        append(")")
                    }
                    append("};")
                }) { it.stat() }

                val moduleBodyChildren = context.moduleBody()!!.children!!
                moduleBodyChildren.add(moduleBodyChildren.size - 1, alwaysBlock)
            } else {
                val probeModuleInstance = parseLucid(buildString {
                    append("$probeComponentName lucid_probe ")
                    append("(#CAPTURE_DEPTH(")
                    append(sampleDepth)
                    append("), #DATA_WIDTH(")
                    append(totalBits)
                    append("), .clk(")
                    append(clock.signal.qualifiedNameInInstance(instance))
                    append("), .data(c{")
                    val signals =
                        probedSignals.map { it.signal.qualifiedNameInInstance(instance) } + probedModules.map { "${it.module.name}.probe_signals" }
                    signals.forEachIndexed { index, node ->
                        if (index != 0)
                            append(", ")
                        append($$"$flatten(")
                        append(node)
                        append(")")
                    }
                    appendLine("}))")
                }) { it.stat() }
                val moduleBodyChildren = context.moduleBody()!!.children!!
                moduleBodyChildren.add(moduleBodyChildren.size - 1, probeModuleInstance)
            }

            ParseTreeMultiWalker.walk(
                listOf(object : ParseTreeListener {
                    override fun visitTerminal(node: TerminalNode) {}
                    override fun visitErrorNode(node: ErrorNode) {}
                    override fun enterEveryRule(ctx: ParserRuleContext) {}
                    override fun exitEveryRule(ctx: ParserRuleContext) {
                        if (ctx is LucidParser.ModuleInstContext) {
                            val children = ctx.children!!
                            val instanceName = children[children.indexOf(ctx.name(1) as ParseTree)].text
                            val branch = probedModules.firstOrNull { it.module.name == instanceName } ?: return
                            children[0] = parseLucid(branch.module.probeName) { it.name() }
                            addDebugFileForModule(branch)
                        }
                    }
                }),
                context
            )

            val sourceContext = LucidParser.SourceContext().apply { children = mutableListOf(context) }

            debugModules.add(
                SourceFile(
                    FileProvider.StringFile(instance.probeName + ".luc", ""),
                    top = instance == top
                ) to sourceContext
            )
        }

        addDebugFileForModule(probedSignalTree)

        val probeComponent = ComponentLibrary.components.first { it.name == "$probeComponentName.luc" }
        val requiredComponents = probeComponent.allDependencies() + listOf(probeComponent)
        val componentSourceFiles = requiredComponents.map { SourceFile(it) }

        val sourceFiles = (project.data.sourceFiles + project.ipCoreStubs + componentSourceFiles).toMutableSet()
        sourceFiles.removeIf { it.top }

        val notationManager = NotationManager()
        // reverse and add debug modules first to make the top module first
        val parsedTrees = debugModules.asReversed() + (parseAll(sourceFiles, notationManager)
            ?: error("Failed to parse project's files!"))

        val projectContext = project.buildContext(
            notationManager,
            parsedTrees
        )
        if (projectContext == null) {
            Log.println(notationManager.getReport())
            error("Failed to build project context!")
        }
        project.build(projectContext)
    }

}


private fun parseLucid(lucid: String, runRule: (LucidParser) -> ParserRuleContext): ParserRuleContext {
    val notationCollector = NotationCollector(SourceFile(FileProvider.StringFile("probe.luc", "")))
    val context = LucidParser(
        CommonTokenStream(
            LucidLexer(
                CharStreams.fromString(lucid, "probe_parser")
            ).apply {
                removeErrorListeners()
                addErrorListener(notationCollector)
            })
    ).apply {
        removeErrorListeners()
        addErrorListener(notationCollector)
    }.run { runRule(this) }
    require(notationCollector.hasNoErrors) {
        notationCollector.getReport()?.text ?: "Probe generated code contained errors!"
    }
    return context
}

private fun buildSignalTree(selectedSignals: List<SignalNode>): SignalTree {
    require(selectedSignals.isNotEmpty()) { "selectedSignals must not be empty" }

    // Find the root of the original tree
    var root: SignalTree = selectedSignals.first().parent
    while (root.parent != null) root = root.parent

    // Collect all SignalTree nodes that are ancestors of selected signals
    val requiredTrees = mutableSetOf<SignalTree>()
    for (signal in selectedSignals) {
        var current: SignalTree? = signal.parent
        while (current != null) {
            if (!requiredTrees.add(current)) break
            current = current.parent
        }
    }

    fun rebuildTree(original: SignalTree, newParent: SignalTree?): SignalTree =
        SignalTree(original.module, newParent) { currentTree ->
            val children = mutableListOf<SignalTreeNode>()
            for (child in original.children) {
                when (child) {
                    is SignalNode -> {
                        if (child in selectedSignals) {
                            children.add(SignalNode(child.signal, currentTree))
                        }
                    }

                    is SignalTree -> {
                        if (child in requiredTrees) {
                            children.add(rebuildTree(child, currentTree))
                        }
                    }
                }
            }
            children
        }

    return rebuildTree(root, null)
}

private fun totalProbedBitsFor(tree: SignalTree): Int {
    return tree.children.sumOf {
        when (it) {
            is SignalNode -> it.signal.width.bitCount ?: error("Bit count was undefined for ${it.signal}")
            is SignalTree -> totalProbedBitsFor(it)
        }
    }
}

private val ModuleInstance.probeName: String
    get() {
        if (module.name == "alchitry_top")
            return module.name
        return "${module.name}_${hashCode().toHexString()}_probe"
    }

fun Signal.qualifiedNameInInstance(moduleInstance: ModuleInstance): String = buildString {
    if (parent != null && parent !is LocalSignal && parent != moduleInstance) {
        append(parent!!.name)
        append(".")
    }
    append(name)
}