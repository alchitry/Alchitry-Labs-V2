package com.alchitry.labs2.parsers

import org.antlr.v4.kotlinruntime.TokenStream
import org.antlr.v4.kotlinruntime.tree.ParseTree

fun basicOffsetProvider(parseTree: ParseTree, tokenStream: TokenStream): IntRange {
    val interval = parseTree.sourceInterval
    return tokenStream[interval.a].startIndex..tokenStream[interval.b].stopIndex
}

fun ParseTree.findFinalNode(
    tokenStream: TokenStream,
    location: Int,
    offsetProvider: (ParseTree, TokenStream) -> IntRange = ::basicOffsetProvider
): ParseTree {
    for (i in 0..<childCount) {
        val child = getChild(i) ?: error("getChild() failed for a child that should exist!")
        val offset = offsetProvider(child, tokenStream)
        // endOffset will be negative if the rule doesn't end
        val adjEndOffset = if (offset.last < 0) Int.MAX_VALUE else offset.last
        if (location in offset.first..adjEndOffset) {
            return child.findFinalNode(tokenStream, location, offsetProvider)
        }
    }
    return this
}