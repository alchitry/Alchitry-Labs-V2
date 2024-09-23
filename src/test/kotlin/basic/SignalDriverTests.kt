package basic

import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SignalDriverTests {
    @Test
    fun basicDriverTest(): Unit = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a
                ) {
                    sig my_sig[8]
                   
                    always {
                        my_sig = 8b0;
                        
                        if (my_sig) {} // to remove unused signal warning
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasNoErrors)
    }

    @Test
    fun signalNotWrittenTest(): Unit = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a
                ) {
                    sig my_sig[8]
                   
                    always {
                        if (my_sig) {} // to remove unused signal warning
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasErrors) { tester.notationManager.getReport() }
    }

    @Test
    fun checkSigInRepeat() = runBlocking {
        val tester = ProjectTester(
            """
                module error_test (
                    input in[32],
                    output out[32]
                ) {
                    sig current_idx[6]
                    always {
                        current_idx = 0
                        //out = 0
                        repeat(i, 16) {
                            out[current_idx] = 0
                            out[current_idx+1] = 1
                            current_idx = current_idx + 2
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
        val tester = ProjectTester(
            """
                module my_module (
                    input a
                ) {
                    sig my_sig[8]
                    
                    always {
                        my_sig = 8b1;
                    }
                   
                    always {
                        my_sig = 8b0;
                        
                        if (my_sig) {} // to remove unused signal warning
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
        val tester = ProjectTester(
            """
                module my_module (
                    input a
                ) {
                    sig my_sig[8]
                   
                    always {
                        my_sig[4:0] = 0;
                        
                        if (my_sig) { } // to remove unused signal warning
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
        val tester = ProjectTester(
            """
                module my_module (
                    input a
                ) {
                    sig my_sig[8]
                   
                    always {
                        my_sig[4:0] = 0;
                        my_sig[7:5] = 1;
                        
                        if (my_sig) { } // to remove unused signal warning
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        tester.assertNoIssues()
    }

    @Test
    fun basicIfPartialDrivenTest() = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a
                ) {
                    sig dummy
                    sig my_sig[8]
                   
                    always {
                        dummy = 1
                        if (dummy) {
                            my_sig = 0
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
        val tester = ProjectTester(
            """
                module my_module (
                    input a
                ) {
                    sig my_sig[8]
                   
                    always {
                        if (1) 
                            my_sig = a
                        else
                            my_sig = 1
                            
                        if (my_sig) {} // used to remove unread warnings
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        tester.assertNoIssues()
    }

    @Test
    fun sigDirectAssignTest() = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a,
                    output b[8]
                ) {
                    sig my_sig[8] = 12
                   
                    always {
                        b = my_sig
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val top = tester.fullParse()
        tester.assertNoIssues()

        runBlocking {
            top.context.initialize()
        }

        assertEquals(BitListValue(12, 8, false), (top.getSignal("b") as Signal).read())
    }

    @Test
    fun sigDirectAssignOverdriveTest() = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a,
                    output b[8]
                ) {
                    sig my_sig[8] = 12
                   
                    always {
                        my_sig = a
                        b = my_sig
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
        val tester = ProjectTester(
            """
                module my_module (
                    input a,
                    output b[8]
                ) {
                    sig my_sig[8] = 9b0 // 9b0 is wider than 8 bits
                   
                    always {
                        if (a) {}
                        b = my_sig
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasNoErrors)
        assert(tester.notationManager.hasWarnings)
    }

    @Test
    fun localSignalTruncationTest() = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a,
                    output b[8]
                ) {
                    always {
                        sig my_sig[8] = 9b0 // 9b0 is wider than 8 bits
                        if (a) {}
                        b = my_sig
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasNoErrors)
        assert(tester.notationManager.hasWarnings)
    }

    @Test
    fun localSignalNestedNameTest() = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a,
                    output b[8]
                ) {
                    always {
                        sig my_sig[8] = 8haa
                        if (a) {
                            sig my_sig2[8] = 8hbb
                            b = my_sig2
                        }
                        b = my_sig
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasNoIssues) { tester.notationManager.getReport() }
    }

    @Test
    fun localSignalReadBeforeWrittenTest() = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a,
                    output b[8]
                ) {
                    always {
                        if (a) {}
                        sig my_sig[8]
                        b = my_sig
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasErrors) { tester.notationManager.getReport() }
    }

    @Test
    fun localSignalLocalWriteTest() = runBlocking {
        val tester = ProjectTester(
            """
                module my_module (
                    input a,
                    output b[8]
                ) {
                    always {
                        if (a) {}
                        sig my_sig[8]
                        my_sig = 8haa
                        b = my_sig
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasNoIssues) { tester.notationManager.getReport() }
    }

    @Test
    fun localSignalDriverTest() = runBlocking {
        val tester = ProjectTester(
            """
                module bin_to_dec #(
                    DIGITS = 4 : DIGITS > 0 && DIGITS < 20,           // limited by 64 bit constants in the tools
                    LEADING_ZEROS = 0 : LEADING_ZEROS == 0 || LEADING_ZEROS == 1
                )(
                    input value[${'$'}clog2(${'$'}pow(10, DIGITS))],            // minimum number of bits for DIGITS
                    output digits[DIGITS][4]                          // decimal output
                ) {
                    always {
                        repeat(i, DIGITS)                                   // for all digits
                            digits[i] = d11                                 // default to invalid number

                        sig remainder[${'$'}width(value)] = value                // initialize remainder
                        sig blank = !LEADING_ZEROS                          // set blank zero flag

                        if (value < ${'$'}pow(10, DIGITS)) {                     // if can be displayed
                            repeat(j, DIGITS, DIGITS-1, -1) {               // for each digit
                                sig scale[${'$'}width(value)] = ${'$'}pow(10, j)      // get the scale for the digit

                                if (remainder < scale) {                    // if this digit is 0
                                    if (j != 0 && blank)                    // use 10 for blank
                                        digits[j] = 10
                                    else                                    // or 0 for zero
                                        digits[j] = 0
                                } else {                                    // digit is 1-9
                                    blank = 0                               // don't blank future zeros
                                    sig sub_value[${'$'}width(value)] = 0        // default to no subtraction
                                    repeat(i, 8, 9, -1) {                   // for each possible value (starting from 9)
                                        if (remainder < (i+1) * scale) {    // if remainder is less than value
                                            digits[j] = i                   // set digit to this value
                                            sub_value = i * scale           // set subtraction value
                                        }
                                    }
                                    remainder = remainder - sub_value       // subtract off last digit
                                }
                            }
                        }
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        assert(tester.notationManager.hasNoIssues) { tester.notationManager.getReport() }
    }
}