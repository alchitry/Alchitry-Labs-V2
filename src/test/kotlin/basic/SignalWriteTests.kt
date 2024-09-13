package basic

import com.alchitry.labs2.parsers.hdl.types.SelectionContext
import com.alchitry.labs2.parsers.hdl.types.SignalSelector
import com.alchitry.labs2.parsers.hdl.values.ArrayValue
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.BitValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SignalWriteTests {
    @Test
    fun singleBitWriteTest() {
        val signal = BitListValue("0000", 2, 4, false)
        val newSig =
            signal.write(listOf(SignalSelector.Bits(1..1, SelectionContext.Constant)), BitValue(Bit.B1, false))
        assertEquals(BitListValue("0010", 2, 4, false), newSig)
    }

    @Test
    fun multiBitWriteTest() {
        val signal = BitListValue("0000", 2, 4, false)
        val newSig = signal.write(
            listOf(SignalSelector.Bits(1..2, SelectionContext.Constant)),
            BitListValue("11", 2, 2, false)
        )
        assertEquals(BitListValue("0110", 2, 4, false), newSig)
    }

    @Test
    fun arrayWriteTest() {
        val baseValue = BitListValue("0000", 2, 4, false)
        val arrayValue = ArrayValue(listOf(baseValue, baseValue, baseValue, baseValue))
        val newValue = BitListValue("0110", 2, 4, false)
        val newArray = arrayValue.write(listOf(SignalSelector.Bits(1..1, SelectionContext.Constant)), newValue)
        assertEquals(ArrayValue(listOf(baseValue, newValue, baseValue, baseValue)), newArray)
    }
}