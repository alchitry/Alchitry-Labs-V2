import com.alchitry.labs2.project.files.Component
import com.alchitry.labs2.project.library.ComponentLibrary
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
}