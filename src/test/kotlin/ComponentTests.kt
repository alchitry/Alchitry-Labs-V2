import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.project.files.Component
import com.alchitry.labs2.project.library.ComponentLibrary
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ComponentTests {
    @Test
    fun buildComponentLibrary() {
        System.setProperty("app.dir", "../")
        val components = ComponentLibrary.components
        assert(components.isNotEmpty())
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
    fun fakePullDown2DTest() = runBlocking {
        val tester = LucidTester(
            """
                module fakePullDown2D #(
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