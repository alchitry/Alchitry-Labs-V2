package helpers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidLexer
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.parsers.ParseStage
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

class LucidModuleTester(val text: String) {
    val project = ProjectContext()
    var context = LucidModuleContext(project, ParseStage.Globals, null)

    val hasNoIssues: Boolean get() = context.errorCollector.hasNoIssues
    val hasNoErrors: Boolean get() = context.errorCollector.hasNoErrors
    val hasNoWarnings: Boolean get() = context.errorCollector.hasNoWarnings
    val hasNoSyntaxIssues: Boolean get() = context.errorCollector.hasNoSyntaxIssues
    val hasErrors: Boolean get() = context.errorCollector.hasErrors
    val hasWarnings: Boolean get() = context.errorCollector.hasWarnings
    val hasSyntaxIssues: Boolean get() = context.errorCollector.hasSyntaxIssues

    fun parseText(): ParseTree {
        val parser = LucidParser(
            CommonTokenStream(
                LucidLexer(
                    CharStreams.fromString(text)
                ).also { it.removeErrorListeners() })
        ).apply {
            (tokenStream.tokenSource as LucidLexer).addErrorListener(this@LucidModuleTester.context.errorCollector)
            removeErrorListeners()
            addErrorListener(this@LucidModuleTester.context.errorCollector)
        }

        return parser.source()
    }

    fun fullParse() {
        val tree = parseText()
        assert(hasNoSyntaxIssues)

        val stages = listOf(
            ParseStage.Globals,
            ParseStage.Modules,
            ParseStage.ModuleInternals,
            ParseStage.Drivers
        )

        stages.forEach {
            context.stage = it
            context.walk(tree)

            if (it == ParseStage.Modules)
                context = context.instantiate("testingInstance", mapOf())

            assert(hasNoIssues)
        }
    }
}