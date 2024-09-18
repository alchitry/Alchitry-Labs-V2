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
        val inout1 = Inout("test1", null, BitListWidth(4), false)
        val inout2 = Inout("test2", null, BitListWidth(4), false)
        val project = ProjectContext(NotationManager())
        inout1.passthrough = true
        inout1.internal.connectTo(inout2.external, project)
        inout2.external.connectTo(inout1.internal, project)

        val b0 = BitListWidth(4).filledWith(Bit.B0, false)
        val b1 = BitListWidth(4).filledWith(Bit.B1, false)
        val bx = BitListWidth(4).filledWith(Bit.Bx, false)
        val bz = BitListWidth(4).filledWith(Bit.Bz, false)

        runBlocking {
            inout1.external.write(b1)
            inout2.internal.write(bz)
            project.processQueue()
            assertEquals(bz, inout1.external.read())
            assertEquals(b1, inout2.internal.read())
            inout1.external.write(bz)
            inout2.internal.write(b0)
            project.processQueue()
            assertEquals(b0, inout1.external.read())
            assertEquals(b0, inout2.internal.read())
            inout1.external.write(b1)
            inout2.internal.write(b0)
            assertEquals(b0, inout1.external.read())
            assertEquals(bx, inout2.internal.read())
            inout1.external.write(b1)
            inout2.internal.write(bz)
            project.processQueue()
            assertEquals(bz, inout1.external.read())
            assertEquals(b1, inout2.internal.read())
        }
    }
}