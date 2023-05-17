import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import helpers.LucidTester
import org.junit.jupiter.api.Test

class SignalDriverTests {
    @Test
    fun basicDriverTest() {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig test[8]
                   
                    always {
                        test = 8b0;
                        
                        if (test) {} // to remove unused signal warning
                        if (a) {}
                    }
                }
            """.trimIndent()
        )
        tester.fullParse()
    }

    @Test
    fun basicPartialDrivenTest() {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig test[8]
                   
                    always {
                        test[4:0] = 0;
                        
                        if (test) { } // to remove unused signal warning
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
                    sig test[8]
                   
                    always {
                        test[4:0] = 0;
                        test[7:5] = 1;
                        
                        if (test) { } // to remove unused signal warning
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
                    sig test[8]
                   
                    always {
                        if (1) {
                            test = 0
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
                    sig test[8]
                   
                    always {
                        if (1) 
                            test = a
                        else
                            test = 1
                            
                        if (test) {} // used to remove unread warnings
                        if (a) {}
                    }
                }
            """.trimIndent()
        )
        tester.fullParse()
    }
}