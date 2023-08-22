package helpers

import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.parsers.ParseStage
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.project.Board
import com.alchitry.labs.project.Project
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

class SimpleLucidTester(text: String, localSignalResolver: SignalResolver? = null) :
    LucidParser(
        CommonTokenStream(
            LucidLexer(
                CharStreams.fromString(text)
            ).also { it.removeErrorListeners() })
    ) {

    val project = Project("Testing", File("."), Board.AlchitryAu, emptySet(), emptySet(), emptySet())
    val context = LucidBlockContext(
        project,
        ModuleInstance(
            "top",
            project,
            null,
            Module("testModule", mapOf(), mapOf(), ModuleContext(null, 0)),
            mapOf(),
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