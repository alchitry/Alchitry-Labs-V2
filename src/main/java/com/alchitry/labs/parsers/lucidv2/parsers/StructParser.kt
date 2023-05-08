package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.StructMember
import com.alchitry.labs.parsers.lucidv2.signals.StructType

data class StructParser(
    private val context: LucidExprContext,
    private val structTypes: MutableMap<StructDecContext, StructType> = mutableMapOf(),
    private val resolvedStructTypes: MutableMap<StructTypeContext, StructType> = mutableMapOf(),
    private val localStructType: MutableMap<String, StructType> = mutableMapOf()
) : LucidBaseListener() {
    fun withContext(context: LucidExprContext) = copy(context = context)

    fun resolveStruct(name: String) = localStructType[name]
    fun resolve(structTypeContext: StructTypeContext) = resolvedStructTypes[structTypeContext]
    fun resolve(structDecContext: StructDecContext) = structTypes[structDecContext]

    override fun enterModule(ctx: ModuleContext?) {
        localStructType.clear()
    }

    override fun enterGlobal(ctx: GlobalContext?) {
        localStructType.clear()
    }

    override fun exitStructDec(ctx: StructDecContext) {
        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.reportError(ctx.name(), "The struct name $name must start with a lowercase letter.")

        val members = mutableMapOf<String, StructMember>()

        ctx.structMember().forEach { structMemberContext ->
            val memberName = structMemberContext.name().text
            val signed = structMemberContext.SIGNED() != null

            if (structMemberContext.name().TYPE_ID() == null)
                context.reportError(
                    structMemberContext.name(),
                    "The struct member name $memberName must start with a lowercase letter."
                )

            val width = context.resolve(structMemberContext.signalWidth())

            if (width == null) {
                context.reportError(structMemberContext.signalWidth(), "Failed to resolve signal width!")
                return@forEach
            }

            members[memberName] = StructMember(memberName, width, signed)
        }

        structTypes[ctx] = StructType(name, members).also {
            localStructType[name] = it
        }
    }

    override fun exitStructType(ctx: StructTypeContext) {
        val name = ctx.name().firstOrNull()?.text ?: return

        val type = if (ctx.name().size > 1) { // includes a . aka GlobalSpace.structName
            val global = context.resolveGlobal(name)
            if (global == null) {
                context.reportError(ctx.name(0), "Couldn't find global namespace $name")
                return
            }

            if (ctx.name().size > 2) {
                context.reportError(ctx.name(2), "Unknown extension to struct name.")
                return
            }

            global.structs[ctx.name(1).text]
        } else { // local struct
            context.resolveStruct(name)
        }

        if (type == null) {
            context.reportError(ctx.name(0), "Failed to find struct with name $name.")
            return
        }

        resolvedStructTypes[ctx] = type
    }
}