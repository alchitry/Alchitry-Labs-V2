package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs2.parsers.lucidv2.types.StructMember
import com.alchitry.labs2.parsers.lucidv2.types.StructType

data class StructParser(
    private val context: LucidExprContext,
    private val structTypes: MutableMap<StructDecContext, StructType> = mutableMapOf(),
    private val resolvedStructTypes: MutableMap<StructTypeContext, StructType> = mutableMapOf(),
    private val localStructType: MutableMap<String, StructType> = mutableMapOf()
) : LucidBaseListener() {
    fun resolveStruct(name: String) = localStructType[name]
    fun resolve(structTypeContext: StructTypeContext) = resolvedStructTypes[structTypeContext]
    fun resolve(structDecContext: StructDecContext) = structTypes[structDecContext]

    override fun enterModule(ctx: ModuleContext) {
        localStructType.clear()
    }

    override fun enterGlobal(ctx: GlobalContext) {
        localStructType.clear()
    }

    override fun exitStructDec(ctx: StructDecContext) {
        val nameCtx = ctx.name() ?: return
        val name = nameCtx.text

        if (nameCtx.TYPE_ID() == null) {
            context.reportError(nameCtx, "The struct name \"$name\" must start with a lowercase letter.")
            return
        }

        if (localStructType.containsKey(name)) {
            context.reportError(nameCtx, "The struct name \"${name}\" has already been used.")
            return
        }

        val members = linkedMapOf<String, StructMember>()

        ctx.structMember().forEach { structMemberContext ->
            val structNameCtx = structMemberContext.name() ?: return@forEach
            val memberName = structNameCtx.text
            val signed = structMemberContext.SIGNED() != null

            if (structNameCtx.TYPE_ID() == null)
                context.reportError(
                    structNameCtx,
                    "The struct member name $memberName must start with a lowercase letter."
                )

            val width = structMemberContext.signalWidth()?.let { context.resolve(it) }

            if (width == null) {
                context.reportError(
                    structMemberContext.signalWidth() ?: structMemberContext,
                    "Failed to resolve signal width!"
                )
                return@forEach
            }

            members[memberName] = StructMember(memberName, width, signed)
        }

        structTypes[ctx] = StructType(name, members).also {
            localStructType[name] = it
        }
    }

    override fun exitStructType(ctx: StructTypeContext) {
        val nameCtx = ctx.name()
        val name = nameCtx.firstOrNull()?.text ?: return

        val type = if (nameCtx.size > 1) { // includes a . aka GlobalSpace.structName
            val global = context.resolveGlobal(name)
            if (global == null) {
                context.reportError(nameCtx[0], "Couldn't find global namespace $name")
                return
            }

            if (nameCtx.size > 2) {
                context.reportError(nameCtx[2], "Unknown extension to struct name.")
                return
            }

            global.structs[nameCtx[1].text]
        } else { // local struct
            context.resolveStruct(name)
        }

        if (type == null) {
            context.reportError(nameCtx[0], "Failed to find struct with name $name.")
            return
        }

        resolvedStructTypes[ctx] = type
    }
}