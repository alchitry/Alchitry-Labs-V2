package com.alchitry.labs2.ui.register_interface

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import java.math.BigInteger
import kotlin.math.pow
import kotlin.text.toString

data class NumberFieldState(
    val value: Int,
    val fractionalBits: Int,
    val signed: Boolean,
    val radix: Radix,
    val text: String = formatValue(value, fractionalBits, signed, radix, false),
    val valid: Boolean = true,
) {
    val valueString: String = formatValue(value, fractionalBits, signed, radix, false)

    fun withNewValue(value: Int): NumberFieldState =
        copy(value = value, text = formatValue(value, fractionalBits, signed, radix, false))

    val fractionalValue = value.toDouble() / 2.0.pow(fractionalBits.toDouble())

    companion object {
        private fun formatFixedPoint(value: Long, fractionalBits: Int, radix: Int): String = buildString {
            var v = value
            if (v < 0) {
                append('-')
                v = -v
            }
            val fractionalMask = (1L shl fractionalBits) - 1
            append((v shr fractionalBits).toString(radix).uppercase())
            append('.')
            var fraction = v and fractionalMask
            val bitsPerDigit = when (radix) {
                2 -> 1
                16 -> 4
                else -> 0
            }
            if (bitsPerDigit != 0) {
                // power-of-two radix: always show all fractional bits
                val digits = (fractionalBits + bitsPerDigit - 1) / bitsPerDigit
                repeat(digits) {
                    fraction *= radix
                    append((fraction shr fractionalBits).toString(radix).uppercase())
                    fraction = fraction and fractionalMask
                }
            } else if (fraction == 0L) {
                append('0')
            } else {
                while (fraction != 0L) {
                    fraction *= radix
                    append((fraction shr fractionalBits).toString(radix).uppercase())
                    fraction = fraction and fractionalMask
                }
            }
        }

        fun parseFixedPoint(text: String, fractionalBits: Int, radix: Int, signed: Boolean): Int? {
            var body = text.trim()
            if (body.isEmpty()) return null
            var negative = false
            if (body.startsWith("-")) {
                if (!signed) return null
                negative = true
                body = body.substring(1)
            } else if (body.startsWith("+")) {
                body = body.substring(1)
            }
            val parts = body.split('.')
            if (parts.size > 2) return null
            val intPart = parts[0]
            val fracPart = if (parts.size == 2) parts[1] else ""
            if (intPart.isEmpty() && fracPart.isEmpty()) return null
            val intValue = if (intPart.isEmpty()) 0L else intPart.toLongOrNull(radix) ?: return null
            var fracValue = 0L
            if (fracPart.isNotEmpty()) {
                val radixBig = BigInteger.valueOf(radix.toLong())
                var numerator = BigInteger.ZERO
                for (c in fracPart) {
                    val digit = Character.digit(c, radix)
                    if (digit < 0) return null
                    numerator = numerator.multiply(radixBig).add(BigInteger.valueOf(digit.toLong()))
                }
                val denominator = radixBig.pow(fracPart.length)
                // scale to fractionalBits and round to the nearest value
                fracValue = numerator.shiftLeft(fractionalBits)
                    .add(denominator.shiftRight(1))
                    .divide(denominator)
                    .toLong()
            }
            val magnitude = (intValue shl fractionalBits) + fracValue
            val value = if (negative) -magnitude else magnitude
            return if (signed) {
                if (value in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()) value.toInt() else null
            } else {
                if (value in 0L..UInt.MAX_VALUE.toLong()) value.toInt() else null
            }
        }

        fun formatValue(value: Int, fractionalBits: Int, signed: Boolean, radix: Radix, includePrefix: Boolean = true) =
            buildString {
                if (includePrefix) {
                    append(radix.prefix)
                }
                if (fractionalBits != 0) {
                    if (signed) {
                        append(formatFixedPoint(value.toLong(), fractionalBits, radix.radix))
                    } else {
                        append(formatFixedPoint(value.toUInt().toLong(), fractionalBits, radix.radix))
                    }
                } else {
                    if (signed) {
                        append(value.toString(radix.radix).uppercase())
                    } else {
                        append(value.toUInt().toString(radix.radix).uppercase())
                    }
                }
            }
    }
}

@Composable
fun NumberField(
    state: NumberFieldState,
    label: String,
    modifier: Modifier = Modifier.Companion,
    signSelector: Boolean = true,
    fractionalBits: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    showConfig: Boolean = true,
    onChange: (NumberFieldState) -> Unit
) {
    fun setNewValues(
        text: String = state.text,
        fractionalBits: Int = state.fractionalBits,
        signed: Boolean = state.signed,
        radix: Radix = state.radix,
        reformat: Boolean = false
    ) {
        val newValue = if (fractionalBits != 0) {
            NumberFieldState.parseFixedPoint(text, fractionalBits, radix.radix, signed)
        } else if (signed) {
            text.toIntOrNull(radix.radix)
        } else {
            text.toUIntOrNull(radix.radix)?.toInt()
        }
        when (newValue) {
            null ->
                onChange(
                    state.copy(
                        valid = false,
                        fractionalBits = fractionalBits,
                        text = text,
                        signed = signed,
                        radix = radix
                    )
                )

            else ->
                onChange(
                    state.copy(
                        value = newValue,
                        fractionalBits = fractionalBits,
                        valid = true,
                        text = if (reformat) {
                            NumberFieldState.formatValue(newValue, fractionalBits, signed, radix, false)
                        } else text,
                        signed = signed,
                        radix = radix
                    )
                )
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(
            showConfig,
            enter = expandHorizontally() + fadeIn(),
            exit = shrinkHorizontally() + fadeOut()
        ) {
            Row(
                modifier.width(IntrinsicSize.Max).padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 35.dp) {

                    RadixSliderSelector(state.radix, modifier = Modifier.width(125.dp)) {
                        val text = if (state.valid) {
                            NumberFieldState.formatValue(state.value, state.fractionalBits, state.signed, it, false)
                        } else state.text
                        setNewValues(radix = it, text = text)
                    }

                    if (signSelector) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Signed: ")
                            Switch(checked = state.signed, onCheckedChange = {
                                setNewValues(signed = it)
                            })
                        }
                    }
                }
                if (fractionalBits) {
                    var fractionalBitsText by remember { mutableStateOf(state.fractionalBits.toString()) }
                    TextField(
                        value = fractionalBitsText,
                        onValueChange = { newText ->
                            fractionalBitsText = newText
                            newText.toIntOrNull()?.let { newBits ->
                                if (state.valid) {
                                    // keep the underlying bit pattern and reinterpret it with the new fractional bits
                                    onChange(
                                        state.copy(
                                            fractionalBits = newBits,
                                            text = NumberFieldState.formatValue(
                                                state.value,
                                                newBits,
                                                state.signed,
                                                state.radix,
                                                false
                                            )
                                        )
                                    )
                                } else {
                                    setNewValues(fractionalBits = newBits, reformat = true)
                                }
                            }
                        },
                        label = { Text("Fractional Bits") },
                        enabled = enabled,
                        isError = fractionalBitsText.toIntOrNull() == null,
                        modifier = Modifier.width(150.dp),
                        singleLine = true
                    )
                }
            }
        }

        var valueFieldFocused by remember { mutableStateOf(false) }
        TextField(
            value = state.text,
            onValueChange = {
                setNewValues(text = it.uppercase())
            },
            prefix = { if (state.radix !is Radix.Decimal) Text(state.radix.prefix) },
            label = { Text(label) },
            enabled = enabled,
            isError = !state.valid,//state.text != state.valueString,
            readOnly = readOnly,
            modifier = Modifier.onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    valueFieldFocused = true
                } else if (valueFieldFocused) {
                    valueFieldFocused = false
                    setNewValues(reformat = true)
                }
            },
            singleLine = true
        )
    }
}
