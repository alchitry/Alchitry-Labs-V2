package basic

import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class RepeatTests {
    @Test
    fun nestedRepeats() = runBlocking {
        val tester = ProjectTester(
            """
                module error_test (
                    output out[32]
                ) {
                    sig current_idx[8]
                    always {
                        current_idx = 0
                        //out = 0
                        repeat(i, 16) {
                            repeat(j, i+1) {
                                current_idx = current_idx + 1
                            }
                        }
                        out = current_idx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        tester.assertNoIssues()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        println(tester.notationManager.getReport().text)
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(136, width = 32, signed = false), out.external.read())
    }

    @Test
    fun basicRepeat() = runBlocking {
        val tester = ProjectTester(
            """
                module error_test (
                    output out[32]
                ) {
                    sig current_idx[8]
                    always {
                        current_idx = 0
                        //out = 0
                        repeat(8) {
                           current_idx = current_idx + 1
                        }
                        out = current_idx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        tester.assertNoIssues()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(8, width = 32, signed = false), out.external.read())
    }

    @Test
    fun indexedRepeat() = runBlocking {
        val tester = ProjectTester(
            """
                module error_test (
                    output out[32]
                ) {
                    sig current_idx[8]
                    always {
                        current_idx = 0
                        //out = 0
                        repeat(i, 8) {
                           current_idx = current_idx + i
                        }
                        out = current_idx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        tester.assertNoIssues()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(28, width = 32, signed = false), out.external.read())
    }

    @Test
    fun indexedOffsetRepeat() = runBlocking {
        val tester = ProjectTester(
            """
                module error_test (
                    output out[32]
                ) {
                    sig current_idx[8]
                    always {
                        current_idx = 0
                        //out = 0
                        repeat(i, 8, 1) {
                           current_idx = current_idx + i
                        }
                        out = current_idx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        tester.assertNoIssues()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(36, width = 32, signed = false), out.external.read())
    }

    @Test
    fun indexedOffsetStepRepeat() = runBlocking {
        val tester = ProjectTester(
            """
                module error_test (
                    output out[32]
                ) {
                    sig current_idx[8]
                    always {
                        current_idx = 0
                        //out = 0
                        repeat(i, 8, 1, 2) {
                           current_idx = current_idx + i
                        }
                        out = current_idx
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val module = tester.fullParse()
        tester.assertNoIssues()
        val out = module.ports["out"] ?: error("Failed to resolve out")
        assert(tester.notationManager.hasNoErrors)
        assertEquals(BitListValue(64, width = 32, signed = false), out.external.read())
    }

    @Test
    fun rippleCarryAdderV1() = runBlocking {
        val tester = ProjectTester(
            """
                testbench playground {
                    rca dut(#SIZE(5))

                    test my_test {
                        dut.a = 5d12
                        dut.b = 5d4
                        dut.cin = 0
                        ${"$"}assert(dut.s == 5d16)
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                module rca #(
                    SIZE = 2 : SIZE > 1
                )(
                    input a[SIZE],
                    input b[SIZE],
                    input cin,
                    output s[SIZE]
                ) {
                    fa fa[SIZE]
                    
                    always {
                        fa.a = a
                        fa.b = b
                        fa.cin[0] = cin
                        
                        // version 1
                        // sum output is not correct except for when SIZE is 3
                        repeat(i, SIZE-1){
                            fa.cin[i+1] = fa.cout[i]
                        }
                        
                        s = fa.s
                    }
                }
            """.trimIndent().toSourceFile("rca.luc"),
            """
                module fa (
                    output s,
                    output cout,
                    input a,
                    input b,
                    input cin
                ) {
                    always {
                        s = a ^ b ^ cin;
                        cout = (a & b) + (a & cin) + (b & cin);
                    }
                }
            """.trimIndent().toSourceFile("fa.luc")
        )

        tester.runTestBenches()
    }

    @Test
    fun rippleCarryAdderV2() = runBlocking {
        val tester = ProjectTester(
            """
                testbench playground {
                    rca dut(#SIZE(5))
                    
                    test my_test {
                        dut.a = 5d12
                        dut.b = 5d4
                        dut.cin = 0
                        ${"$"}assert(dut.s == 5d16)
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                module rca #(
                    SIZE = 2 : SIZE > 1
                )(
                    input a[SIZE],
                    input b[SIZE],
                    input cin,
                    output s[SIZE]
                ) {
                    fa fa[SIZE]
                    
                    always {
                        fa.a = a
                        fa.b = b
                        fa.cin[0] = cin
                        
                        // version 2, error when SIZE is 3, OK otherwise
                        // sum output is not correct 
                        repeat(i, SIZE-1, SIZE-1, -1){
                            fa.cin[i] = fa.cout[i-1]
                        }
                        
                        s = fa.s
                    }
                }
            """.trimIndent().toSourceFile("rca.luc"),
            """
                module fa (
                    output s,
                    output cout,
                    input a,
                    input b,
                    input cin
                ) {
                    always {
                        s = a ^ b ^ cin;
                        cout = (a & b) + (a & cin) + (b & cin);
                    }
                }
            """.trimIndent().toSourceFile("fa.luc")
        )

        tester.runTestBenches()
    }

    @Test
    fun rippleCarryAdderV3() = runBlocking {
        val tester = ProjectTester(
            """
                testbench playground {
                    rca dut(#SIZE(5))
                    
                    test my_test {
                        dut.a = 5d12
                        dut.b = 5d4
                        dut.cin = 0
                        ${"$"}assert(dut.s == 5d16)
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                module rca #(
                    SIZE = 2 : SIZE > 1
                )(
                    input a[SIZE],
                    input b[SIZE],
                    input cin,
                    output s[SIZE]
                ) {
                    fa fa[SIZE]
                    
                    always {
                        fa.a = a
                        fa.b = b
                        fa.cin[0] = cin
                        
                        // version 3, error when SIZE is 3, OK otherwise
                        // sum output is not correct
                        repeat(i, SIZE-1, 1){
                            fa.cin[i] = fa.cout[i-1]
                        }
                        
                        s = fa.s
                    }
                }
            """.trimIndent().toSourceFile("rca.luc"),
            """
                module fa (
                    output s,
                    output cout,
                    input a,
                    input b,
                    input cin
                ) {
                    always {
                        s = a ^ b ^ cin;
                        cout = (a & b) + (a & cin) + (b & cin);
                    }
                }
            """.trimIndent().toSourceFile("fa.luc")
        )

        tester.runTestBenches()
    }
}