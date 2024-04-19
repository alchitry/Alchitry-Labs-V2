import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
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
                        repeat(16, i) {
                            repeat(i+1, j) {
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
        assertEquals(BitListValue(136, width = 32, signed = false), out.external.read())
        println(tester.notationManager.getReport().text)
        assert(tester.notationManager.hasNoErrors)
    }
}