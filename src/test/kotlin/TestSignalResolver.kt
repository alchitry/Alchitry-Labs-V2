import com.alchitry.labs.parsers.lucidv2.resolvers.SignalResolver
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.signals.Signal

class TestSignalResolver(vararg val signals: SignalOrParent): SignalResolver {
    override fun resolve(name: String): SignalOrParent? {
        return signals.firstOrNull { it.name == name }
    }
}