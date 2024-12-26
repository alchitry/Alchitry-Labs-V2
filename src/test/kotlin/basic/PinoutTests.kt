package basic

import com.alchitry.labs2.hardware.pinout.V2toV1Adapter
import kotlin.test.Test

class PinoutTests {

    @Test
    fun checkRedundantV2toV1() {
        val values = mutableSetOf<String>()
        V2toV1Adapter.entries.forEach { pin ->
            if (values.contains(pin.v2Pin)) {
                throw Exception("Duplicate V2 pin: ${pin.v2Pin}")
            }
            values.add(pin.v2Pin)
        }
    }
}