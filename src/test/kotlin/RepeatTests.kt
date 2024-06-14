import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class RepeatTests {
    @Test
    fun nestedRepeats() = runBlocking {
        val tester = LucidTester(
            """
                module errorTest (
                    input in[32],
                    output out[32]
                ) {
                    sig currentIdx[8]
                    always {
                        currentIdx = 0
                        //out = 0
                        repeat(i, 16) {
                            repeat(j, i+1) {
                                currentIdx = currentIdx + 1
                            }
                        }
                        out = currentIdx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        println(tester.notationManager.getReport().text)
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(136, width = 32, signed = false), out.external.read())
    }

    @Test
    fun basicRepeat() = runBlocking {
        val tester = LucidTester(
            """
                module errorTest (
                    input in[32],
                    output out[32]
                ) {
                    sig currentIdx[8]
                    always {
                        currentIdx = 0
                        //out = 0
                        repeat(8) {
                           currentIdx = currentIdx + 1
                        }
                        out = currentIdx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(8, width = 32, signed = false), out.external.read())

    }

    @Test
    fun indexedRepeat() = runBlocking {
        val tester = LucidTester(
            """
                module errorTest (
                    input in[32],
                    output out[32]
                ) {
                    sig currentIdx[8]
                    always {
                        currentIdx = 0
                        //out = 0
                        repeat(i, 8) {
                           currentIdx = currentIdx + i
                        }
                        out = currentIdx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(28, width = 32, signed = false), out.external.read())
    }

    @Test
    fun indexedOffsetRepeat() = runBlocking {
        val tester = LucidTester(
            """
                module errorTest (
                    input in[32],
                    output out[32]
                ) {
                    sig currentIdx[8]
                    always {
                        currentIdx = 0
                        //out = 0
                        repeat(i, 8, 1) {
                           currentIdx = currentIdx + i
                        }
                        out = currentIdx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(36, width = 32, signed = false), out.external.read())
    }

    @Test
    fun indexedOffsetStepRepeat() = runBlocking {
        val tester = LucidTester(
            """
                module errorTest (
                    input in[32],
                    output out[32]
                ) {
                    sig currentIdx[8]
                    always {
                        currentIdx = 0
                        //out = 0
                        repeat(i, 8, 1, 2) {
                           currentIdx = currentIdx + i
                        }
                        out = currentIdx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(64, width = 32, signed = false), out.external.read())
    }
}