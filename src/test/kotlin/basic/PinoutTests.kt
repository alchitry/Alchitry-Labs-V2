package basic

import com.alchitry.labs2.hardware.pinout.*
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

    @Test
    fun checkRedundantCuV2() {
        val values = mutableSetOf<String>()
        CuV2Pin.entries.forEach { pin ->
            if (values.contains(pin.fpgaPin)) {
                throw Exception("Duplicate Cu V2 pin: ${pin.fpgaPin}")
            }
            values.add(pin.fpgaPin)
        }
    }

    @Test
    fun checkRedundantAuV2() {
        val values = mutableSetOf<String>()
        AuV2Pin.entries.forEach { pin ->
            if (values.contains(pin.fpgaPin)) {
                throw Exception("Duplicate Au V2 pin: ${pin.fpgaPin}")
            }
            values.add(pin.fpgaPin)
        }
    }

    @Test
    fun checkRedundantCu() {
        val values = mutableSetOf<String>()
        CuPin.entries.forEach { pin ->
            if (values.contains(pin.fpgaPin)) {
                throw Exception("Duplicate Cu pin: ${pin.fpgaPin}")
            }
            values.add(pin.fpgaPin)
        }
    }

    @Test
    fun checkRedundantAu() {
        val values = mutableSetOf<String>()
        AuPin.entries.forEach { pin ->
            if (values.contains(pin.fpgaPin)) {
                throw Exception("Duplicate Au pin: ${pin.fpgaPin}")
            }
            values.add(pin.fpgaPin)
        }
    }
}