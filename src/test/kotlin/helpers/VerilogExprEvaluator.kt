package helpers

import com.alchitry.labs2.parsers.hdl.values.ArrayValue
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.BitValue
import com.alchitry.labs2.parsers.hdl.values.Value
import kotlinx.serialization.json.*
import java.math.BigInteger

/**
 * Evaluates a SystemVerilog expression using slang's JSON interface and returns the result as a [Value].
 */
fun evalVerilogExpr(expr: String, extraContext: String = ""): Value {
    val svCode = buildString {
        if (extraContext.isNotBlank()) {
            appendLine(extraContext)
        }
        appendLine("module top;")
        appendLine("    localparam x = $expr;")
        appendLine("endmodule")
    }

    val process = ProcessBuilder("slang", "-q", "--ast-json-scope", "top", "--ast-json", "-", "-")
        .redirectErrorStream(false)
        .start()

    process.outputStream.bufferedWriter().use {
        it.write(svCode)
    }

    val jsonOutput = process.inputStream.bufferedReader().readText()
    val errorOutput = process.errorStream.bufferedReader().readText()
    val exitCode = process.waitFor()

    if (exitCode != 0) {
        error("slang failed to evaluate expression: '$expr'\nExit code: $exitCode\nErrors:\n$errorOutput")
    }

    val jsonElement = Json.parseToJsonElement(jsonOutput)
    val paramMember = findParamMember(jsonElement, "x")
        ?: error("Could not find parameter 'x' in slang AST output for expression: '$expr'\nOutput:\n$jsonOutput")

    val valueStr = paramMember.jsonObject["value"]?.jsonPrimitive?.contentOrNull
        ?: error("Parameter 'x' does not have an evaluated value for expression: '$expr'")

    return parseSlangValue(valueStr)
}

private fun findParamMember(element: JsonElement, name: String): JsonObject? {
    if (element is JsonObject) {
        if (element["name"]?.jsonPrimitive?.contentOrNull == name && element["kind"]?.jsonPrimitive?.contentOrNull == "Parameter") {
            return element
        }
        for ((_, child) in element) {
            val found = findParamMember(child, name)
            if (found != null) return found
        }
    } else if (element is JsonArray) {
        for (child in element) {
            val found = findParamMember(child, name)
            if (found != null) return found
        }
    }
    return null
}

private val literalRegex = Regex("""^([+-]?)([0-9]+)'([sS]?)([bBoOdDhH])([0-9a-fA-F_xXzZ?]+)$""")
private val intRegex = Regex("""^[+-]?[0-9]+$""")

private fun parseSlangValue(valueStr: String): Value {
    val trimmed = valueStr.trim()

    // Assignment pattern: '{elem1, elem2, ...}
    if (trimmed.startsWith("'{") && trimmed.endsWith("}")) {
        val content = trimmed.substring(2, trimmed.length - 1).trim()
        if (content.isEmpty()) {
            return ArrayValue(emptyList())
        }
        val elements = splitAssignmentPattern(content).map { elemStr ->
            val elemValue = parseSlangValue(elemStr)
            if (elemValue is BitValue) {
                BitListValue(listOf(elemValue.bit), elemValue.signed)
            } else {
                elemValue
            }
        }
        return ArrayValue(elements)
    }

    val match = literalRegex.matchEntire(trimmed)
    if (match != null) {
        val sign = match.groupValues[1]
        val size = match.groupValues[2].toInt()
        val isSigned = match.groupValues[3].isNotEmpty() || sign == "-"
        val baseChar = match.groupValues[4].lowercase().first()
        val digits = match.groupValues[5].replace("_", "").replace("?", "z")

        val bitList = when (baseChar) {
            'd' -> {
                val isNeg = sign == "-" || digits.startsWith("-")
                val cleanDigits = if (digits.startsWith("-")) digits.substring(1) else digits
                val bigInt = if (isNeg) -BigInteger(cleanDigits) else BigInteger(cleanDigits)
                BitListValue(bigInt, signed = isSigned || isNeg, width = size)
            }

            'b' -> BitListValue(digits, radix = 2, width = size, signed = isSigned)
            'h' -> BitListValue(digits, radix = 16, width = size, signed = isSigned)
            'o' -> BitListValue(digits, radix = 8, width = size, signed = isSigned)
            else -> error("Unsupported radix '$baseChar' in slang value: $trimmed")
        }
        if (size == 1) {
            return BitValue(bitList.bits.first(), bitList.signed)
        }
        return bitList
    }

    if (intRegex.matches(trimmed)) {
        val bigInt = BigInteger(trimmed)
        return BitListValue(bigInt, signed = true, width = 32)
    }

    error("Unable to parse slang value format: '$trimmed'")
}

private fun splitAssignmentPattern(content: String): List<String> {
    val elements = mutableListOf<String>()
    var depth = 0
    val current = StringBuilder()

    for (char in content) {
        when (char) {
            '{' -> {
                depth++
                current.append(char)
            }

            '}' -> {
                depth--
                current.append(char)
            }

            ',' -> {
                if (depth == 0) {
                    elements.add(current.toString().trim())
                    current.clear()
                } else {
                    current.append(char)
                }
            }

            else -> current.append(char)
        }
    }
    if (current.isNotBlank()) {
        elements.add(current.toString().trim())
    }
    return elements
}

