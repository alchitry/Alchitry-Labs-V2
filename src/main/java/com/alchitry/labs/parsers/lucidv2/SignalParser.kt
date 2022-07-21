package com.alchitry.labs.parsers.lucidv2

import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.dummyErrorListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.signals.Signal

class SignalParser(val errorListener: ErrorListener = dummyErrorListener) : LucidBaseListener(), SignalResolver {
    override fun resolve(name: String): Signal? {
        return null // TODO
    }
}