package basic

import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class BuildingModeTests {
    @Test
    fun fakePullDownTest() = runBlocking {
        val tester = ProjectTester(
            """
            module fake_pull_down #(
                SIZE = 1 : SIZE > 0
            )(
                input clk,  // clock
                inout in[SIZE],
                output out[SIZE]
            ) {
            
                .clk(clk) {
                    dff flip[4]
                    dff saved[SIZE]
                }
            
                always {
                    flip.d = flip.q + 1
                    in = flip.q ? SIZEx{bz} : SIZEx{b0}
                    if (flip.q > 2)
                        saved.d = in
                    out = saved.q
                }
            }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse(ExprEvalMode.Building)
        assert(tester.notationManager.hasNoIssues) { tester.notationManager.getReport() }
    }

    @Test
    fun fakePullDownTest2D() = runBlocking {
        val tester = ProjectTester(
            """
            module fake_pull_down2_d #(
                DIM_1 = 1 : DIM_1 > 0,
                DIM_2 = 1 : DIM_2 > 0
            )(
                input clk,  // clock
                inout in[DIM_2][DIM_1],
                output out[DIM_2][DIM_1]
            ) {
            
                .clk(clk) {
                    dff flip[4]
                    dff saved[DIM_2][DIM_1]
                }
            
                always {
                    flip.d = flip.q + 1
                    in = flip.q ? DIM_2x{{DIM_1x{bz}}} : DIM_2x{{DIM_1x{b0}}}
                    if (flip.q > 2)
                        saved.d = in
                    out = saved.q
                }
            }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse(ExprEvalMode.Building)
        assert(tester.notationManager.hasNoIssues) { tester.notationManager.getReport() }
    }
}