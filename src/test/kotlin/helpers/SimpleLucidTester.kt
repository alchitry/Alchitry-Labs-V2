package helpers

import com.alchitry.labs.parsers.EvalQueue
import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.parsers.ParseStage
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.project.Board
import com.alchitry.labs.project.Project
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import java.io.File

class SimpleLucidTester(text: String, localSignalResolver: SignalResolver? = null) {
    val parser = LucidParser(
        CommonTokenStream(
            LucidLexer(
                CharStreams.fromString(text)
            ).also { it.removeErrorListeners() })
    )

    val project = Project("Testing", File("."), Board.AlchitryAu, emptySet(), emptySet(), emptySet())
    val evalQueue = EvalQueue()
    val context = LucidBlockContext(
        project,
        evalQueue,
        ModuleInstance(
            "top",
            project,
            evalQueue,
            null,
            Module("testModule", mapOf(), mapOf(), LucidParser.ModuleContext(null, 0)),
            mapOf(),
            mapOf(),
            testErrorCollector()
        ),
        ParseStage.ErrorCheck,
        localSignalResolver = localSignalResolver,
        errorCollector = testErrorCollector()
    )

    init {
        (parser.tokenStream?.tokenSource as LucidLexer).addErrorListener(context.errorCollector)
        parser.removeErrorListeners()
        parser.addErrorListener(context.errorCollector)
    }

    val hasNoIssues: Boolean get() = context.errorCollector.hasNoIssues
    val hasNoErrors: Boolean get() = context.errorCollector.hasNoErrors
    val hasNoWarnings: Boolean get() = context.errorCollector.hasNoWarnings
    val hasErrors: Boolean get() = context.errorCollector.hasErrors
    val hasWarnings: Boolean get() = context.errorCollector.hasWarnings
}