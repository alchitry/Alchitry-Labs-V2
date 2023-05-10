import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import helpers.LucidModuleTester
import org.junit.jupiter.api.Test

class SignalDriverTests {
    @Test
    fun basicDriverTest() {
        val tester = LucidModuleTester(
            """
                module myModule (
                    input a
                ) {
                    sig test[8]
                   
                    always {
                        test = 8b0;
                    }
                }
            """.trimIndent()
        )
        tester.fullParse()
    }

    @Test
    fun basicPartialDrivenTest() {
        val tester = LucidModuleTester(
            """
                module myModule (
                    input a
                ) {
                    sig test[8]
                   
                    always {
                        test[4:0] = 0;
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
        val tester = LucidModuleTester(
            """
                module myModule (
                    input a
                ) {
                    sig test[8]
                   
                    always {
                        test[4:0] = 0;
                        test[7:5] = 1;
                    }
                }
            """.trimIndent()
        )
        tester.fullParse()
    }

    @Test
    fun basicIfPartialDrivenTest() {
        val tester = LucidModuleTester(
            """
                module myModule (
                    input a
                ) {
                    sig test[8]
                   
                    always {
                        if (1) {
                            test = 0
                        }
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
    fun basicIfCompleteDrivenTest() {
        val tester = LucidModuleTester(
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
                    }
                }
            """.trimIndent()
        )
        tester.fullParse()
    }
}