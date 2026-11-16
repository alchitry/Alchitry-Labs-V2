package basic

import com.alchitry.hardware.Board
import com.alchitry.labs2.project.*
import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.ProjectFile
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.io.path.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectTests {
    @BeforeTest
    fun setup() {
        System.setProperty("app.dir", "../")
    }

    @Test
    fun projectFileSerialization() {
        val testFile = ConstraintFile(
            FileProvider.DiskFile("source/test.acf"),
        )
        val json = Json { prettyPrint = true }
        val encoded = json.encodeToString(ProjectFile.serializer(), testFile)
        //println(encoded)
        val decoded = json.decodeFromString<ProjectFile>(encoded)
        assertEquals(testFile, decoded)
    }

    @Test
    fun projectTemplateDeserialization() {
        val json = Json { prettyPrint = true }

        // Test "All"
        val allTemplateJson = """
            {
                "name": "Test All",
                "description": "Test description",
                "boards": ["All"],
                "priority": 0
            }
        """.trimIndent()
        val allTemplate = json.decodeFromString<ProjectTemplate>(allTemplateJson)
        assertEquals(Board.All.toSet(), allTemplate.boards.toSet())
        assertEquals(8, allTemplate.boards.size)

        // Test "V2"
        val v2TemplateJson = """
            {
                "name": "Test V2",
                "description": "Test description",
                "boards": ["V2"],
                "priority": 1
            }
        """.trimIndent()
        val v2Template = json.decodeFromString<ProjectTemplate>(v2TemplateJson)
        assertEquals(
            setOf(
                Board.AlchitryCuV2,
                Board.AlchitryAuV2T35,
                Board.AlchitryAuV2T50,
                Board.AlchitryPtV2T100,
                Board.AlchitryPtV2T200
            ),
            v2Template.boards.toSet()
        )

        // Test "AuV2" and "PtV2"
        val auPtTemplateJson = """
            {
                "name": "Test AuV2 and PtV2",
                "description": "Test description",
                "boards": ["AuV2", "PtV2"],
                "priority": 2,
                "requiresMig": true
            }
        """.trimIndent()
        val auPtTemplate = json.decodeFromString<ProjectTemplate>(auPtTemplateJson)
        assertEquals(
            listOf(
                Board.AlchitryAuV2T35,
                Board.AlchitryAuV2T50,
                Board.AlchitryPtV2T100,
                Board.AlchitryPtV2T200
            ),
            auPtTemplate.boards
        )
        assertEquals(true, auPtTemplate.requiresMig)

        // Test mixed and distinct
        val mixedJson = """
            {
                "name": "Test Mixed",
                "description": "Test description",
                "boards": ["Au", "Au+", "Cu", "AuV2", "CuV2", "PtV2"],
                "priority": 3
            }
        """.trimIndent()
        val mixedTemplate = json.decodeFromString<ProjectTemplate>(mixedJson)
        assertEquals(
            listOf(
                Board.AlchitryAu,
                Board.AlchitryAuPlus,
                Board.AlchitryCu,
                Board.AlchitryAuV2T35,
                Board.AlchitryAuV2T50,
                Board.AlchitryCuV2,
                Board.AlchitryPtV2T100,
                Board.AlchitryPtV2T200
            ),
            mixedTemplate.boards
        )

        // Test roundtrip serialization
        val encoded = json.encodeToString(ProjectTemplate.serializer(), mixedTemplate)
        val decoded = json.decodeFromString<ProjectTemplate>(encoded)
        assertEquals(mixedTemplate, decoded)
    }

    @Test
    fun loadAllLibraryTemplates() {
        val templates = ProjectTemplate.getTemplates()
        assert(templates.isNotEmpty())
        for (template in templates) {
            assert(template.boards.isNotEmpty())
        }
    }

    @Test
    fun projectDataSerialization() {
        val projectData = ProjectData1V0("Blinker", Board.AlchitryCu, emptySet(), emptySet())
        val json = Json { prettyPrint = true }
        val encoded = json.encodeToString(ProjectData.serializer(), projectData)
        //println(encoded)
        val decoded = json.decodeFromString<ProjectData>(encoded)
        assertEquals(projectData, decoded)
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun xmlToJsonConverter() {
        System.setProperty("app.dir", "../")

        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project name="Base Project" board="Alchitry Au" language="Lucid" version="4">
              <files>
                <src top="true">alchitry_top.luc</src>
                <constraint lib="true">alchitry.acf</constraint>
                <component>reset_conditioner.luc</component>
              </files>
            </project>
        """.trimIndent()
        val projectDir = Files.createTempDirectory("testProject")

        projectDir.resolve("source/alchitryTop.luc").createParentDirectories().createFile()

        val tempFile = Files.createTempFile("testXmlProjectFile", ".alp")

        tempFile.writeText(xml)
        val projectData = Project.openXml(tempFile.toFile())
        val json = Json { prettyPrint = true }
        val encoded = json.encodeToString(ProjectData.serializer(), projectData)
        //println(encoded)

        projectDir.deleteRecursively()
    }
}