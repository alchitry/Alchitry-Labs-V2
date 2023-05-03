import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidLexer
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class LucidTester (text: String) :
    LucidParser(
        CommonTokenStream(
            LucidLexer(
                CharStreams.fromString(text)
            ).also { it.removeErrorListeners() })
    ) {

    val context = LucidModuleContext(ProjectContext())

    init {
        (tokenStream.tokenSource as LucidLexer).addErrorListener(context.errorCollector)
        removeErrorListeners()
        addErrorListener(context.errorCollector)

        context.addToParser(this)
    }

    val hasNoIssues: Boolean get() = hasNoErrors && hasNoWarnings && hasNoSyntaxIssues

    val hasNoErrors: Boolean get() {
        context.errorCollector.errors.forEach {
            println(it)
        }
        return context.errorCollector.errors.isEmpty()
    }
    val hasNoWarnings: Boolean get() {
        context.errorCollector.warnings.forEach {
            println(it)
        }
        return context.errorCollector.warnings.isEmpty()
    }
    val hasNoSyntaxIssues: Boolean get() {
        context.errorCollector.syntaxIssues.forEach {
            println(it)
        }
        return context.errorCollector.syntaxIssues.isEmpty()
    }
    val hasErrors: Boolean get() = context.errorCollector.errors.isNotEmpty()
    val hasWarnings: Boolean get() = context.errorCollector.warnings.isNotEmpty()
    val hasSyntaxIssues: Boolean get() = context.errorCollector.syntaxIssues.isNotEmpty()
}