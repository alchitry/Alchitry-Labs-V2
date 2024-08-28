import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LocalSignalTests {
    @Test
    fun localSignalTest() = runBlocking {
        val test = LucidTester(
            """
                module controlModule #(
                    DIGITS ~ 1 : DIGITS > 0 && DIGITS < 20,           // limited by 64 bit constants in the tools
                    LEADING_ZEROS = 0 : LEADING_ZEROS == 0 || LEADING_ZEROS == 1
                )(
                    input value[${"$"}clog2(${"$"}pow(10, DIGITS))],            // minimum number of bits for DIGITS 
                    output digits[DIGITS][4]                          // decimal output
                ) {
                    
                    sig scale[${"$"}width(value)]
                    sig remainder[${"$"}width(value)]                         // running remainder
                    sig sub_value[${"$"}width(value)]                         // temporary subtraction value
                    sig blank                                          // flag for leading zeros
                    
                    always {
                        repeat(i, DIGITS){
                            digits[i] = d11                                // default to invalid number
                        }
                        
                        remainder = value                                // initialize remainder
                        blank = !LEADING_ZEROS                           // set blank zero flag
                        scale = 0
                        sub_value = 0
                        
                        if (value < ${"$"}pow(10, DIGITS)) {                   // if can be displayed
                            repeat(j, DIGITS, DIGITS-1, -1){// for each digit  
                                scale = ${"$"}pow(10, j)                          // get the scale for the digit
                                
                                if (remainder < scale) {                      // if this digit is 0
                                    if (j != 0 && blank)                        // use 10 for blank
                                        digits[j] = 10
                                    else                                        // or 0 for zero
                                        digits[j] = 0
                                } else {                                      // digit is 1-9
                                    blank = 0                                  // don't blank future zeros
                                    sub_value = 0                              // default to no subtraction
                                    repeat(i, 9, 9, -1){// for each possible value (starting from 9)
                                        
                                        if (remainder < (i+1) * scale) {          // if remainder is less than value
                                            digits[j] = i                          // set digit to this value
                                            sub_value = i * scale                  // set subtraction value
                                        }
                                    }
                                    remainder = remainder - sub_value          // subtract off last digit
                                }
                            }
                        }
                    }
                } 
            """.trimIndent().toSourceFile("control.luc"),
            """
                module testModule #(
                    DIGITS ~ 1 : DIGITS > 0 && DIGITS < 20,           // limited by 64 bit constants in the tools
                    LEADING_ZEROS = 0 : LEADING_ZEROS == 0 || LEADING_ZEROS == 1
                )(
                    input value[${"$"}clog2(${"$"}pow(10, DIGITS))],            // minimum number of bits for DIGITS
                    output digits[DIGITS][4]                          // decimal output
                ) {
                    always {
                        repeat(i, DIGITS)                                   // for all digits
                            digits[i] = d11                                 // default to invalid number
    
                        sig remainder[${"$"}width(value)] = value                // initialize remainder
                        sig blank = !LEADING_ZEROS                          // set blank zero flag
    
                        if (value < ${"$"}pow(10, DIGITS)) {                     // if can be displayed
                            repeat(j, DIGITS, DIGITS-1, -1) {               // for each digit
                                sig scale[${"$"}width(value)] = ${"$"}pow(10, j)      // get the scale for the digit
    
                                if (remainder < scale) {                    // if this digit is 0
                                    if (j != 0 && blank)                    // use 10 for blank
                                        digits[j] = 10
                                    else                                    // or 0 for zero
                                        digits[j] = 0
                                } else {                                    // digit is 1-9
                                    blank = 0                               // don't blank future zeros
                                    sig sub_value[${"$"}width(value)] = 0        // default to no subtraction
                                    repeat(i, 9, 9, -1) {                   // for each possible value (starting from 9)
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
            """.trimIndent().toSourceFile("local.luc"),
            """
                testBench localTests {
                    testModule testModule(#DIGITS(4))
                    controlModule controlModule(#DIGITS(4))
                    
                    test myTest {
                        testModule.value = 1234
                        controlModule.value = 1234
                        ${"$"}silentTick()
                        ${"$"}assert(testModule.digits == controlModule.digits)
                    }
                }
            """.trimIndent().toSourceFile("localTest.luc")
        )
        test.runTestBenches()
    }

    @Test
    fun simpleLocalTest() = runBlocking {
        val test = LucidTester(
            """
                module localTest (
                    input value[4],  // clock
                    output digit[4]
                ) {
                    always {
                        digit = d11
                        sig remainder[${"$"}width(value)] = value
                        
                        repeat(i, 1, 1) {
                            sig scale[${"$"}width(value)] = ${"$"}pow(10, i)
                            if (remainder < scale) {
                                digit = value
                            }
                        }
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = test.fullParse()
        module.getSignal("value")!!.write(BitListValue(5.toBigInteger()))
        test.project.processQueue()
        assertEquals(BitListValue(5.toBigInteger(), width = 4), module.getSignal("digit")!!.read())
    }
}