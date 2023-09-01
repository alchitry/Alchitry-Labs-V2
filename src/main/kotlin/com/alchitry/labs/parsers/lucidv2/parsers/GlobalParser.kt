package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.grammar.LucidBaseListener
import com.alchitry.labs.parsers.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs.parsers.lucidv2.types.Constant
import com.alchitry.labs.parsers.lucidv2.types.EnumType
import com.alchitry.labs.parsers.lucidv2.types.GlobalNamespace
import com.alchitry.labs.parsers.lucidv2.types.StructType

class GlobalParser(
    private val context: LucidGlobalContext
) : LucidBaseListener() {
    private val constants = mutableMapOf<String, Constant>()
    private val structs = mutableMapOf<String, StructType>()
    private val globals = mutableMapOf<String, GlobalNamespace>()
    private val enums = mutableMapOf<String, EnumType>()

    fun resolveGlobal(name: String) = globals[name]

    override suspend fun enterGlobal(ctx: GlobalContext) {
        constants.clear()
        structs.clear()
        enums.clear()
    }

    override suspend fun exitConstDec(ctx: ConstDecContext) {
        context.resolve(ctx)?.let { constants[it.name] = it }
    }

    override suspend fun exitStructDec(ctx: StructDecContext) {
        context.resolve(ctx)?.let { structs[it.name] = it }
    }

    override suspend fun exitEnumDec(ctx: EnumDecContext) {
        context.resolve(ctx)?.let { enums[it.name] = it }
    }

    override suspend fun exitGlobal(ctx: GlobalContext) {
        val nameCtx = ctx.name() ?: return
        val name = nameCtx.text
        if (nameCtx.SPACE_ID() == null)
            context.reportError(
                nameCtx,
                "Global names must start with an uppercase letter and contain at least one lowercase letter."
            )

        val global =
            GlobalNamespace(name, constants.toMap(), structs.toMap(), enums.values.toList())
        if (!context.project.addGlobal(global)) {
            context.reportError(nameCtx, "The global name $name has already been used.")
            return
        }
        globals[name] = global
    }
}