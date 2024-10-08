package basic

import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.types.StructMember
import com.alchitry.labs2.parsers.hdl.types.StructType
import com.alchitry.labs2.parsers.hdl.values.*
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GlobalParserTests {
    @Test
    fun simpleGlobalTest() = runBlocking {
        val tester = ProjectTester(
            """
                global MyGlobal {
                    const ONE = 1
                }
            """.trimIndent().toSourceFile()
        )
        tester.globalParse()
        val global = tester.project.resolveGlobal("MyGlobal")

        assertNotNull(global)
        assertEquals(BitValue(Bit.B1, signed = false), global.constants["ONE"]?.read())
    }

    @Test
    fun selfRefGlobalTest() = runBlocking {
        val tester = ProjectTester(
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
        assertEquals(BitListValue("10", 2, signed = false), global.constants["TWO"]?.read())
    }

    @Test
    fun structGlobalTest() = runBlocking {
        val tester = ProjectTester(
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
                ),
                "MyGlobal"
            ),
            global.structs["rgb"]
        )
    }

    @Test
    fun structRefGlobalTest() = runBlocking {
        val tester = ProjectTester(
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
            ),
            "MyGlobal"
        )

        val value = StructValue(
            type,
            linkedMapOf(
                "r" to BitListValue(0, width = 8, signed = false),
                "g" to BitListValue(0, width = 8, signed = false),
                "b" to BitListValue(255, width = 8, signed = false),
            )
        )

        assertEquals(value, global.constants["BLUE"]?.read())
    }

    @Test
    fun nestedStructGlobalTest() = runBlocking {
        val tester = ProjectTester(
            """
                global MyGlobal {
                    struct rgb {
                        r[8],
                        g[7],
                        b[6]
                    }
                    struct my_struct {
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
            ),
            "MyGlobal"
        )


        assertEquals(
            StructType(
                "my_struct",
                linkedMapOf(
                    "color" to StructMember("color", StructWidth(rgbStruct), false),
                    "type" to StructMember("type", BitListWidth(8), false)
                ),
                "MyGlobal"
            ),
            global.structs["my_struct"]
        )
    }
}