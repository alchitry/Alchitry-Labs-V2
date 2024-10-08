package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.LucidParser.EnumDecContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidExprContext
import com.alchitry.labs2.parsers.hdl.lucid.context.SignalResolver
import com.alchitry.labs2.parsers.hdl.types.EnumType
import com.alchitry.labs2.parsers.hdl.types.SignalOrParent
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class EnumParser(
    private val context: LucidExprContext,
    private val enumTypes: MutableMap<EnumDecContext, EnumType> = mutableMapOf(),
    val localEnumType: MutableMap<String, EnumType> = mutableMapOf()
) : LucidBaseListener(), SignalResolver {
    override fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent? = localEnumType[name]
    fun resolve(enumDecContext: EnumDecContext) = enumTypes[enumDecContext]

    override fun enterGlobal(ctx: LucidParser.GlobalContext) {
        localEnumType.clear()
    }

    override fun enterModule(ctx: LucidParser.ModuleContext) {
        localEnumType.clear()
    }

    override fun exitEnumDec(ctx: EnumDecContext) {
        val names = ctx.name()
        val nameCtx = names.firstOrNull() ?: return

        if (names.size < 2) {
            context.reportError(ctx, "Enums must have at least one member!")
            return
        }

        if (nameCtx.SPACE_ID() == null) {
            context.reportError(
                nameCtx,
                "Enum names must start with an uppercase letter followed by at least one lowercase letter."
            )
            return
        }

        val membersCtx = ctx.name().subList(1, ctx.name().size)

        val memberSet = mutableSetOf<String>()

        membersCtx.forEach { mCtx ->
            val member = mCtx.CONST_ID()?.text
            if (member == null) {
                context.reportError(mCtx, "Enum member names must be all uppercase letters an underscores.")
                return
            }
            if (member == "WIDTH") {
                context.reportError(mCtx, "The name \"WIDTH\" is a reserved name for enums.")
                return
            }
            if (!memberSet.add(member)) {
                context.reportError(mCtx, "The name \"$member\" has already been defined.")
                return
            }
        }

        enumTypes[ctx] = EnumType(nameCtx.text, memberSet, null)
            .also { localEnumType[nameCtx.text] = it }
    }
}