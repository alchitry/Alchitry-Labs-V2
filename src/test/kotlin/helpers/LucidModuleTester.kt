package helpers

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidLexer
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.SourceContext
import com.alchitry.labs.parsers.lucidv2.parsers.ParseStage
import com.alchitry.labs.parsers.lucidv2.signals.ModuleInstance
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.test.assertNotNull

class LucidModuleTester(val text: String) {
    val project = ProjectContext()

    fun parseText(errorCollector: ErrorCollector): SourceContext {
        val parser = LucidParser(
            CommonTokenStream(
                LucidLexer(
                    CharStreams.fromString(text)
                ).also { it.removeErrorListeners() })
        ).apply {
            (tokenStream.tokenSource as LucidLexer).addErrorListener(errorCollector)
            removeErrorListeners()
            addErrorListener(errorCollector)
        }

        return parser.source()
    }

    fun fullParse(): LucidModuleContext {
        val errorCollector = ErrorCollector()
        val tree = parseText(errorCollector)
        assert(errorCollector.hasNoSyntaxIssues)

        val globalContext = LucidGlobalContext(project, errorCollector)
        globalContext.walk(tree)

        assert(errorCollector.hasNoIssues)

        val moduleTypeContext = LucidModuleTypeContext(project, errorCollector)
        val module = moduleTypeContext.extract(tree)

        assert(errorCollector.hasNoIssues)
        assertNotNull(module)

        val moduleInstance = ModuleInstance(project, "top", module, mapOf())

        val moduleContext =
            LucidModuleContext(project, ParseStage.ModuleInternals, moduleInstance, null, errorCollector)

        val stages = listOf(
            ParseStage.ModuleInternals,
            ParseStage.Drivers
        )

        stages.forEach {
            moduleContext.stage = it
            moduleContext.walk(tree)

            assert(errorCollector.hasNoIssues)
        }

        return moduleContext
    }
}