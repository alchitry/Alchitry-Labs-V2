package helpers

import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent

class TestSignalResolver(vararg val signals: SignalOrParent) : SignalResolver {
    override fun resolve(name: String): SignalOrParent? {
        return signals.firstOrNull { it.name == name }
    }
}