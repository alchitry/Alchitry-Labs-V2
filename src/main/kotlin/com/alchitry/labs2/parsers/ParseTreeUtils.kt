package com.alchitry.labs2.parsers

import org.antlr.v4.kotlinruntime.TokenStream
import org.antlr.v4.kotlinruntime.tree.ParseTree

fun basicOffsetProvider(parseTree: ParseTree, tokenStream: TokenStream): IntRange? {
    val interval = parseTree.sourceInterval
    if (interval.a < 0 || interval.b < 0) {
        return null
    }
    return tokenStream[interval.a].startIndex..tokenStream[interval.b].stopIndex
}

fun ParseTree.findFinalNode(
    tokenStream: TokenStream,
    location: Int,
    offsetProvider: (ParseTree, TokenStream) -> IntRange? = ::basicOffsetProvider
): ParseTree {
    for (i in 0..<childCount) {
        val child = getChild(i) ?: error("getChild() failed for a child that should exist!")
        val offset = offsetProvider(child, tokenStream) ?: continue
        // endOffset will be negative if the rule doesn't end
        val adjEndOffset = if (offset.last < 0) Int.MAX_VALUE else offset.last
        if (location in offset.first..adjEndOffset) {
            return child.findFinalNode(tokenStream, location, offsetProvider)
        }
    }
    return this
}

val ParseTree.parents: List<ParseTree>
    get() {
        val parents = mutableListOf<ParseTree>()
        var current = this.getParent()
        while (current != null) {
            parents.add(current)
            current = current.getParent()
        }
        return parents
    }

inline fun <reified T : ParseTree> ParseTree.hasParent(): Boolean {
    var current = this.getParent()
    while (current != null) {
        if (current is T) return true
        current = current.getParent()
    }
    return false
}