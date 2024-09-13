package basic

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.types.ports.Inout
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.BitListWidth
import com.alchitry.labs2.parsers.notations.NotationManager
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InoutTests {
    @Test
    fun basicInoutTest() {
        val inout = Inout("test", null, BitListWidth(8), false)
        runBlocking {
            var ct = 0
            inout.external.addDependant {
                when (ct++) {
                    0 -> assertEquals(BitListValue("zzzz1100", 2, 8, false), inout.external.read())
                    1 -> assertEquals(BitListValue("zzzz1100", 2, 8, false), inout.external.read())
                    2 -> assertEquals(BitListValue("zzzz1100", 2, 8, false), inout.external.read())
                }
            }

            inout.internal.write(BitListValue("zzzz1100", 2, 8, false))
            assertEquals(BitListValue("zzzz1100", 2, 8, false), inout.external.read())
            inout.external.write(BitListValue("0101zzzz", 2, 8, false))
            assertEquals(BitListValue("01011100", 2, 8, false), inout.internal.read())
            inout.external.write(BitListValue("01010zzz", 2, 8, false))
            assertEquals(BitListValue("0101x100", 2, 8, false), inout.internal.read())

            // this shouldn't trigger a new value for readJob since this write will produce the same value
            inout.external.write(BitListValue("01011zzz", 2, 8, false))
            assertEquals(BitListValue("0101x100", 2, 8, false), inout.internal.read())

            assert(ct == 3)
        }
    }

    @Test
    fun connectedInoutTest() {
        val inout1 = Inout("test1", null, BitListWidth(1), false)
        val inout2 = Inout("test2", null, BitListWidth(1), false)
        val project = ProjectContext(NotationManager())
        inout1.internal.connectTo(inout2.external, project)
        inout2.external.connectTo(inout1.internal, project)

        runBlocking {
            inout1.external.write(Bit.B1.toBitValue())
            project.processQueue()
            assertEquals(Bit.B1.toBitValue().asBitListValue(), inout2.internal.read())
            inout1.external.write(Bit.Bz.toBitValue())
            inout2.internal.write(Bit.B0.toBitValue())
            project.processQueue()
            assertEquals(Bit.B0.toBitValue().asBitListValue(), inout1.external.read())
        }
    }
}