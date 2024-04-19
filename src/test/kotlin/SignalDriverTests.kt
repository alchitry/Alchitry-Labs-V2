import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SignalDriverTests {
    @Test
    fun basicDriverTest(): Unit = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig mySig[8]
                   
                    always {
                        mySig = 8b0;
                        
                        if (mySig) {} // to remove unused signal warning
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
    }

    @Test
    fun checkSigInRepeat() = runBlocking {
        val tester = LucidTester(
            """
                module errorTest (
                    input in[32],
                    output out[32]
                ) {
                    sig currentIdx[6]
                    always {
                        currentIdx = 0
                        //out = 0
                        repeat(i, 16) {
                            out[currentIdx] = 0
                            out[currentIdx+1] = 1
                            currentIdx = currentIdx + 2
                        }
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasNoErrors)
    }

    @Test
    fun doubleDriverTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig mySig[8]
                    
                    always {
                        mySig = 8b1;
                    }
                   
                    always {
                        mySig = 8b0;
                        
                        if (mySig) {} // to remove unused signal warning
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasErrors)
        assert(tester.notationManager.hasNoWarnings)
    }

    @Test
    fun basicPartialDrivenTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig mySig[8]
                   
                    always {
                        mySig[4:0] = 0;
                        
                        if (mySig) { } // to remove unused signal warning
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasErrors) // should complain about test being partially driven
        assert(tester.notationManager.hasNoWarnings)
    }

    @Test
    fun multiPartialDrivenTest(): Unit = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig mySig[8]
                   
                    always {
                        mySig[4:0] = 0;
                        mySig[7:5] = 1;
                        
                        if (mySig) { } // to remove unused signal warning
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
    }

    @Test
    fun basicIfPartialDrivenTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig mySig[8]
                   
                    always {
                        if (1) {
                            mySig = 0
                        }
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasErrors) // should complain about test being partially driven
    }

    @Test
    fun basicIfCompleteDrivenTest(): Unit = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig mySig[8]
                   
                    always {
                        if (1) 
                            mySig = a
                        else
                            mySig = 1
                            
                        if (mySig) {} // used to remove unread warnings
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
    }

    @Test
    fun sigDirectAssignTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a,
                    output b[8]
                ) {
                    sig mySig[8] = 12
                   
                    always {
                        b = mySig
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val top = tester.fullParse()

        runBlocking {
            top.context.initialize()
        }

        assertEquals(BitListValue(12, 8, false), (top.getSignal("b") as Signal).read())
    }

    @Test
    fun sigDirectAssignOverdriveTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a,
                    output b[8]
                ) {
                    sig mySig[8] = 12
                   
                    always {
                        mySig = a
                        b = mySig
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasErrors)
        assert(tester.notationManager.hasNoWarnings)
    }

    @Test
    fun sigDirectAssignTruncationTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a,
                    output b[8]
                ) {
                    sig mySig[8] = 9b0 // 9b0 is wider than 8 bits
                   
                    always {
                        if (a) {}
                        b = mySig
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasNoErrors)
        assert(tester.notationManager.hasWarnings)
    }
}