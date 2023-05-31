import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SignalDriverTests {
    @Test
    fun basicDriverTest() {
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
            """.trimIndent()
        )
        tester.fullParse()
    }

    @Test
    fun doubleDriverTest() {
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
            """.trimIndent()
        )
        val errorCollector = ErrorCollector()
        tester.fullParse(errorCollector)
        assert(errorCollector.hasErrors)
        assert(errorCollector.hasNoWarnings)
    }

    @Test
    fun basicPartialDrivenTest() {
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
            """.trimIndent()
        )
        val errorCollector = ErrorCollector()
        tester.fullParse(errorCollector)
        assert(errorCollector.hasErrors) // should complain about test being partially driven
        assert(errorCollector.hasNoWarnings)
    }

    @Test
    fun multiPartialDrivenTest() {
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
            """.trimIndent()
        )
        tester.fullParse()
    }

    @Test
    fun basicIfPartialDrivenTest() {
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
            """.trimIndent()
        )
        val errorCollector = ErrorCollector()
        tester.fullParse(errorCollector)
        assert(errorCollector.hasErrors) // should complain about test being partially driven
    }

    @Test
    fun basicIfCompleteDrivenTest() {
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
            """.trimIndent()
        )
        tester.fullParse()
    }

    @Test
    fun sigDirectAssignTest() {
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
            """.trimIndent()
        )
        val top = tester.fullParse()

        runBlocking {
            top.context.initialize()
        }

        assertEquals(BitListValue(12, 8, false, false), (top.getSignal("b") as Signal).read())
    }

    @Test
    fun sigDirectAssignOverdriveTest() {
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
            """.trimIndent()
        )
        val errorCollector = ErrorCollector()
        tester.fullParse(errorCollector)
        assert(errorCollector.hasErrors)
        assert(errorCollector.hasNoWarnings)
    }

    @Test
    fun sigDirectAssignTruncationTest() {
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
            """.trimIndent()
        )
        val errorCollector = ErrorCollector()
        tester.fullParse(errorCollector)
        assert(errorCollector.hasNoErrors)
        assert(errorCollector.hasWarnings)
    }
}