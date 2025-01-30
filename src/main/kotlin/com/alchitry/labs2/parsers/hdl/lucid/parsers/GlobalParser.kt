package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidGlobalContext
import com.alchitry.labs2.parsers.hdl.types.Constant
import com.alchitry.labs2.parsers.hdl.types.EnumType
import com.alchitry.labs2.parsers.hdl.types.GlobalNamespace
import com.alchitry.labs2.parsers.hdl.types.StructType

class GlobalParser(
    private val context: LucidGlobalContext
) : LucidBaseListener() {
    private val constants = mutableMapOf<String, Constant>()
    private val structs = mutableMapOf<String, StructType>()
    private val globals = mutableMapOf<String, GlobalNamespace>()
    private val enums = mutableMapOf<String, EnumType>()

    fun resolveGlobal(name: String) = globals[name]

    override fun enterGlobal(ctx: GlobalContext) {
        constants.clear()
        structs.clear()
        enums.clear()
    }

    override fun exitConstDec(ctx: ConstDecContext) {
        context.resolve(ctx)?.let { constants[it.name] = it }
    }

    override fun exitStructDec(ctx: StructDecContext) {
        context.resolve(ctx)?.let { structs[it.name] = it }
    }

    override fun exitEnumDec(ctx: EnumDecContext) {
        context.resolve(ctx)?.let { enums[it.name] = it }
    }

    override fun exitGlobal(ctx: GlobalContext) {
        val nameCtx = ctx.name() ?: return
        val name = nameCtx.text
        if (nameCtx.SPACE_ID() == null)
            context.reportError(
                nameCtx,
                "Global names must start with an uppercase letter and contain at least one lowercase letter."
            )

        val global =
            GlobalNamespace(name, context.sourceFile, constants.toMap(), structs.toMap(), enums.values.toList())
        if (!context.project.addGlobal(global)) {
            context.reportError(nameCtx, "The global name $name has already been used.")
            return
        }
        globals[name] = global
    }
}