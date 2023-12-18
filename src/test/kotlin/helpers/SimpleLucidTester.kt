package helpers

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.parsers.ParseStage
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

class SimpleLucidTester(text: String, localSignalResolver: SignalResolver? = null) {
    val parser = LucidParser(
        CommonTokenStream(
            LucidLexer(
                CharStreams.fromString(text)
            ).also { it.removeErrorListeners() })
    )

    val project = ProjectContext()
    val context = LucidBlockContext(
        project,
        ModuleInstance(
            "top",
            project,
            null,
            Module("testModule", mapOf(), mapOf(), LucidParser.ModuleContext(null, 0)),
            mapOf(),
            mapOf(),
            testErrorCollector()
        ),
        ParseStage.ErrorCheck,
        localSignalResolver = localSignalResolver,
        notationCollector = testErrorCollector()
    )

    init {
        (parser.tokenStream?.tokenSource as LucidLexer).addErrorListener(context.notationCollector)
        parser.removeErrorListeners()
        parser.addErrorListener(context.notationCollector)
    }

    val hasNoIssues: Boolean get() = context.notationCollector.hasNoIssues
    val hasNoErrors: Boolean get() = context.notationCollector.hasNoErrors
    val hasNoWarnings: Boolean get() = context.notationCollector.hasNoWarnings
    val hasErrors: Boolean get() = context.notationCollector.hasErrors
    val hasWarnings: Boolean get() = context.notationCollector.hasWarnings
}