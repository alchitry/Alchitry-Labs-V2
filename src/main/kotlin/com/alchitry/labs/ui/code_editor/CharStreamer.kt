package com.alchitry.labs.ui.code_editor

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CodePointBuffer
import org.antlr.v4.runtime.CodePointCharStream
import org.antlr.v4.runtime.IntStream
import java.nio.CharBuffer

fun List<CodeLineState>.toCharStream(sourceName: String = IntStream.UNKNOWN_SOURCE_NAME): CharStream {
    val charCount = sumOf { it.text.text.length } + size // include newline characters

    val codePointBufferBuilder = CodePointBuffer.builder(charCount)
    val cb = CharBuffer.allocate(charCount)
    forEach {
        cb.put(it.text.text)
        cb.put("\n")
    }
    cb.flip()
    codePointBufferBuilder.append(cb)
    return CodePointCharStream.fromBuffer(codePointBufferBuilder.build(), sourceName)
}