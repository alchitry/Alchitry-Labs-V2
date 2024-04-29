package helpers

import com.alchitry.labs2.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs2.parsers.lucidv2.types.SignalOrParent
import org.antlr.v4.kotlinruntime.ParserRuleContext

class TestSignalResolver(vararg val signals: SignalOrParent) : SignalResolver {
    override fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent? {
        return signals.firstOrNull { it.name == name }
    }
}