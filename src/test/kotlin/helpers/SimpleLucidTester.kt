package helpers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.grammar.LucidLexer
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.parsers.ParseStage
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class SimpleLucidTester(text: String, localSignalResolver: SignalResolver? = null) :
    LucidParser(
        CommonTokenStream(
            LucidLexer(
                CharStreams.fromString(text)
            ).also { it.removeErrorListeners() })
    ) {

    val project = ProjectContext()
    val context = LucidModuleContext(
        project,
        ModuleInstance(
            "top",
            project,
            null,
            Module("testModule", mapOf(), mapOf(), ModuleContext(null, 0)),
            mapOf()
        ),
        ParseStage.ErrorCheck,
        localSignalResolver = localSignalResolver
    )

    init {
        (tokenStream.tokenSource as LucidLexer).addErrorListener(context.errorCollector)
        removeErrorListeners()
        addErrorListener(context.errorCollector)
    }

    val hasNoIssues: Boolean get() = context.errorCollector.hasNoIssues
    val hasNoErrors: Boolean get() = context.errorCollector.hasNoErrors
    val hasNoWarnings: Boolean get() = context.errorCollector.hasNoWarnings
    val hasNoSyntaxIssues: Boolean get() = context.errorCollector.hasNoSyntaxIssues
    val hasErrors: Boolean get() = context.errorCollector.hasErrors
    val hasWarnings: Boolean get() = context.errorCollector.hasWarnings
    val hasSyntaxIssues: Boolean get() = context.errorCollector.hasSyntaxIssues
}