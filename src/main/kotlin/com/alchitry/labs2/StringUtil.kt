package com.alchitry.labs2

fun String.prefixLinesWith(prefix: String): String = lines().joinToString("\n") { prefix + it }
fun String.asSingleLine(): String = replace("\n", "").replace("\r", "")


/**
 * Converts a string from camelCase to snake_case.
 *
 * This function takes a string in camelCase notation and converts it to snake_case notation.
 * Each capital letter in the camelCase string is replaced by an underscore followed by the
 * lowercase version of the letter. The resulting string is returned.
 *
 * @receiver The camelCase string to be converted.
 * @return A new string in snake_case notation.
 */
fun String.camelToSnakeCase() = foldIndexed(StringBuilder()) { index, acc, char ->
    val nextUpper = this.getOrNull(index + 1)?.isUpperCase()

    val lastUpper = this.getOrNull(index - 1)?.isUpperCase()
    val lastDigit = this.getOrNull(index - 1)?.isDigit()

    if (
        (char.isUpperCase() && lastUpper == false && lastDigit == false) ||
        (char.isDigit() && nextUpper == true)
    )
        acc.append('_')
    acc.append(char.lowercaseChar())
}.toString()

/**
 * Converts a string from snake_case to camelCase.
 *
 * The method splits the string by underscores, capitalizes the first character
 * of each word except the first one, and then joins them back together.
 *
 * @receiver String The string in snake_case format.
 * @return String The string converted to camelCase format.
 */
fun String.snakeToCamelCase() = split('_').mapIndexed { index, word ->
    if (index == 0) word else word.replaceFirstChar { it.uppercaseChar() }
}.joinToString("")
