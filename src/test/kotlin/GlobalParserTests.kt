import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.types.StructMember
import com.alchitry.labs2.parsers.hdl.types.StructType
import com.alchitry.labs2.parsers.hdl.values.*
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GlobalParserTests {
    @Test
    fun simpleGlobalTest() = runBlocking {
        val tester = LucidTester(
            """
                global MyGlobal {
                    const ONE = 1
                }
            """.trimIndent().toSourceFile()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")

        assertNotNull(global)
        assertEquals(BitValue(Bit.B1, signed = false), global.constants["ONE"]?.value)
    }

    @Test
    fun selfRefGlobalTest() = runBlocking {
        val tester = LucidTester(
            """
                global MyGlobal {
                    const ONE = 1
                    const TWO = ONE + ONE
                }
            """.trimIndent().toSourceFile()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")

        assertNotNull(global)
        assertEquals(BitListValue("10", 2, signed = false), global.constants["TWO"]?.value)
    }

    @Test
    fun structGlobalTest() = runBlocking {
        val tester = LucidTester(
            """
                global MyGlobal {
                    struct rgb {
                        r[8],
                        g[7],
                        b[6]
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")
        assertNotNull(global)
        assertEquals(
            StructType(
                "rgb",
                linkedMapOf(
                    "r" to StructMember("r", BitListWidth(8), false),
                    "g" to StructMember("g", BitListWidth(7), false),
                    "b" to StructMember("b", BitListWidth(6), false),
                )
            ),
            global.structs["rgb"]
        )
    }

    @Test
    fun structRefGlobalTest() = runBlocking {
        val tester = LucidTester(
            """
                global MyGlobal {
                    struct rgb {
                        r[8],
                        g[8],
                        b[8]
                    }
                    const BLUE = <rgb>(.r(0), .g(0), .b(255))
                }
            """.trimIndent().toSourceFile()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")
        assertNotNull(global)

        val type = StructType(
            "rgb",
            linkedMapOf(
                "r" to StructMember("r", BitListWidth(8), false),
                "g" to StructMember("g", BitListWidth(8), false),
                "b" to StructMember("b", BitListWidth(8), false),
            )
        )

        val value = StructValue(
            type,
            linkedMapOf(
                "r" to BitListValue(0, width = 8, signed = false),
                "g" to BitListValue(0, width = 8, signed = false),
                "b" to BitListValue(255, width = 8, signed = false),
            )
        )

        assertEquals(value, global.constants["BLUE"]?.value)
    }

    @Test
    fun nestedStructGlobalTest() = runBlocking {
        val tester = LucidTester(
            """
                global MyGlobal {
                    struct rgb {
                        r[8],
                        g[7],
                        b[6]
                    }
                    struct myStruct {
                        color<rgb>,
                        type[8]
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")
        assertNotNull(global)
        val rgbStruct = StructType(
            "rgb",
            linkedMapOf(
                "r" to StructMember("r", BitListWidth(8), false),
                "g" to StructMember("g", BitListWidth(7), false),
                "b" to StructMember("b", BitListWidth(6), false),
            )
        )


        assertEquals(
            StructType(
                "myStruct",
                linkedMapOf(
                    "color" to StructMember("color", StructWidth(rgbStruct), false),
                    "type" to StructMember("type", BitListWidth(8), false)
                )
            ),
            global.structs["myStruct"]
        )
    }
}