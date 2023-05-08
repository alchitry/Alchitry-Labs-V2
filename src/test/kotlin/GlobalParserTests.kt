import com.alchitry.labs.parsers.lucidv2.signals.StructMember
import com.alchitry.labs.parsers.lucidv2.signals.StructType
import com.alchitry.labs.parsers.lucidv2.values.*
import helpers.LucidModuleTester
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GlobalParserTests {
    @Test
    fun simpleGlobalTest() {
        val tester = LucidModuleTester(
            """
                global MyGlobal {
                    const ONE = 1
                }
            """.trimIndent()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")

        assertNotNull(global)
        assertEquals(BitValue(Bit.B1, constant = true, signed = false), global.constants["ONE"])
    }

    @Test
    fun selfRefGlobalTest() {
        val tester = LucidModuleTester(
            """
                global MyGlobal {
                    const ONE = 1
                    const TWO = ONE + ONE
                }
            """.trimIndent()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")

        assertNotNull(global)
        assertEquals(BitListValue("10", 2, constant = true, signed = false), global.constants["TWO"])
    }

    @Test
    fun structGlobalTest() {
        val tester = LucidModuleTester(
            """
                global MyGlobal {
                    struct rgb {
                        r[8],
                        g[7],
                        b[6]
                    }
                }
            """.trimIndent()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")
        assertNotNull(global)
        assertEquals(
            StructType(
                "rgb",
                mapOf(
                    "r" to StructMember("r", BitListWidth(8), false),
                    "g" to StructMember("g", BitListWidth(7), false),
                    "b" to StructMember("b", BitListWidth(6), false),
                )
            ),
            global.structs["rgb"]
        )
    }

    @Test
    fun structRefGlobalTest() {
        val tester = LucidModuleTester(
            """
                global MyGlobal {
                    struct rgb {
                        r[8],
                        g[8],
                        b[8]
                    }
                    const BLUE = <rgb>(.r(0), .g(0), .b(255))
                }
            """.trimIndent()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")
        assertNotNull(global)

        val type = StructType(
            "rgb",
            mapOf(
                "r" to StructMember("r", BitListWidth(8), false),
                "g" to StructMember("g", BitListWidth(8), false),
                "b" to StructMember("b", BitListWidth(8), false),
            )
        )

        val value = StructValue(
            type,
            mapOf(
                "r" to BitListValue(0, width = 8, constant = true, signed = false),
                "g" to BitListValue(0, width = 8, constant = true, signed = false),
                "b" to BitListValue(255, width = 8, constant = true, signed = false),
            )
        )

        assertEquals(value, global.constants["BLUE"])
    }

    @Test
    fun nestedStructGlobalTest() {
        val tester = LucidModuleTester(
            """
                global MyGlobal {
                    struct rgb {
                        r[8],
                        g[7],
                        b[6]
                    }
                    struct test {
                        color<rgb>,
                        type[8]
                    }
                }
            """.trimIndent()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")
        assertNotNull(global)
        val rgbStruct = StructType(
            "rgb",
            mapOf(
                "r" to StructMember("r", BitListWidth(8), false),
                "g" to StructMember("g", BitListWidth(7), false),
                "b" to StructMember("b", BitListWidth(6), false),
            )
        )


        assertEquals(
            StructType(
                "test",
                mapOf(
                    "color" to StructMember("color", StructWidth(rgbStruct), false),
                    "type" to StructMember("type", BitListWidth(8), false)
                )
            ),
            global.structs["test"]
        )
    }
}