import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.ProjectFile
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectTests {
    @Test
    fun projectFileSerialization() {
        val testFile = ConstraintFile(
            FileProvider.StringFile("test.acf", "fake/path", "myFile"),
        )
        val json = Json { prettyPrint = true }
        val encoded = json.encodeToString(ProjectFile.serializer(), testFile)
        println(encoded)
        val decoded = json.decodeFromString<ProjectFile>(encoded)
        assertEquals(testFile, decoded)
    }
}