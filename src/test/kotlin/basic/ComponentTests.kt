package basic

import com.alchitry.hardware.Board
import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.project.files.Component
import com.alchitry.labs2.project.library.ComponentLibrary
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ComponentTests {
    @Test
    fun buildComponentLibrary() {
        System.setProperty("app.dir", "../")
        val components = ComponentLibrary.components
        assert(components.isNotEmpty())

        val ioV1Pulldown = ComponentLibrary.findByPath("Constraints/io_v1_pulldown.acf")
        assertNotNull(ioV1Pulldown)
        assertEquals(
            setOf(
                Board.AlchitryAu,
                Board.AlchitryAuPlus,
                Board.AlchitryAuV2T35,
                Board.AlchitryAuV2T50,
                Board.AlchitryPtV2T100,
                Board.AlchitryPtV2T200
            ),
            ioV1Pulldown.supportedBoards
        )

        val ioV2 = ComponentLibrary.findByPath("Constraints/io_v2.acf")
        assertNotNull(ioV2)
        assertEquals(
            setOf(
                Board.AlchitryAuV2T35,
                Board.AlchitryAuV2T50,
                Board.AlchitryCuV2,
                Board.AlchitryPtV2T100,
                Board.AlchitryPtV2T200
            ),
            ioV2.supportedBoards
        )

        val xilinxProbe = ComponentLibrary.findByPath("Internal/xilinx_probe.luc")
        assertNotNull(xilinxProbe)
        assertEquals(
            setOf(
                Board.AlchitryAu,
                Board.AlchitryAuPlus,
                Board.AlchitryAuV2T35,
                Board.AlchitryAuV2T50,
                Board.AlchitryPtV2T100,
                Board.AlchitryPtV2T200
            ),
            xilinxProbe.supportedBoards
        )

        val startupe2 = ComponentLibrary.findByPath("Primitives/startupe2.v")
        assertNotNull(startupe2)
        assertEquals(
            setOf(
                Board.AlchitryAu,
                Board.AlchitryAuPlus,
                Board.AlchitryAuV2T35,
                Board.AlchitryAuV2T50,
                Board.AlchitryPtV2T100,
                Board.AlchitryPtV2T200
            ),
            startupe2.supportedBoards
        )
    }

    @Test
    fun componentSerialization() {
        System.setProperty("app.dir", "../")
        val component = ComponentLibrary.components.first()
        val json = Json.encodeToString(Component, component)
        val deserializedComponent = Json.decodeFromString<Component>(json)
        assertEquals(component, deserializedComponent)
    }

    @Test
    fun componentDescriptorDeserialization() {
        val json = Json { prettyPrint = true }

        // Test "All"
        val allComp = Component.fromResource("Conditioning/button_conditioner.luc")
        // Button conditioner doesn't specify supportedBoards, so it should be null
        assertEquals(null, allComp.supportedBoards)

        // Test all components in library have valid supported boards if present
        for (comp in ComponentLibrary.components) {
            comp.supportedBoards?.let {
                assert(it.isNotEmpty())
            }
        }
    }

    @Test
    fun fakePullDown2DTest() = runBlocking {
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
                        in = DIM_2x{{DIM_1x{flip.q ? bz : b0}}}
                        if (flip.q > 2)
                            saved.d = in
                        out = saved.q
                    }
                }
            """.trimIndent().toSourceFile()
        )
        tester.fullParse()
        tester.assertNoIssues()
    }
}