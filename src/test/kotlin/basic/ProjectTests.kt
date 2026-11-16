package basic

import com.alchitry.hardware.Board
import com.alchitry.labs2.Settings
import com.alchitry.labs2.project.*
import com.alchitry.labs2.project.files.*
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.io.path.*
import kotlin.test.*

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

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun compatibleBoardsWithoutIpCoresOrRestrictedComponents() {
        val tempDir = Files.createTempDirectory("testProjCompat1")
        val projData = ProjectData1V3(
            projectName = "TestProj",
            board = Board.AlchitryAu,
            sourceFiles = emptySet(),
            constraintFiles = emptySet(),
            ipCores = emptySet()
        )
        val project = Project(tempDir, projData)
        assertEquals(Board.All, project.compatibleBoards())
        Board.All.forEach { board ->
            assertTrue(project.canSwitchTo(board))
        }
        tempDir.deleteRecursively()
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun compatibleBoardsWithIpCoresReturnsEmpty() {
        val tempDir = Files.createTempDirectory("testProjCompat2")
        val ipCore = IPCore(name = "mig_7series_0", stub = null, files = emptySet())
        val projData = ProjectData1V3(
            projectName = "TestProj",
            board = Board.AlchitryAu,
            sourceFiles = emptySet(),
            constraintFiles = emptySet(),
            ipCores = setOf(ipCore)
        )
        val project = Project(tempDir, projData)
        assertEquals(emptyList(), project.compatibleBoards())
        Board.All.forEach { board ->
            assertFalse(project.canSwitchTo(board))
        }
        tempDir.deleteRecursively()
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun compatibleBoardsWithComponentsChecksSupportedBoards() {
        val tempDir = Files.createTempDirectory("testProjCompat3")
        val comp1 = Component(
            name = "comp1.luc",
            componentName = "Comp1",
            description = "Test component 1",
            supportedBoards = setOf(Board.AlchitryAu, Board.AlchitryAuPlus, Board.AlchitryCu),
            categories = listOf("Test"),
            content = "module comp1 () {}"
        )
        val comp2 = Component(
            name = "comp2.luc",
            componentName = "Comp2",
            description = "Test component 2",
            supportedBoards = setOf(Board.AlchitryAu, Board.AlchitryPtV2T100),
            categories = listOf("Test"),
            content = "module comp2 () {}"
        )
        val projData = ProjectData1V3(
            projectName = "TestProj",
            board = Board.AlchitryAu,
            sourceFiles = setOf(SourceFile(comp1), SourceFile(comp2)),
            constraintFiles = emptySet(),
            ipCores = emptySet()
        )
        val project = Project(tempDir, projData)
        assertEquals(listOf(Board.AlchitryAu), project.compatibleBoards())
        assertTrue(project.canSwitchTo(Board.AlchitryAu))
        assertFalse(project.canSwitchTo(Board.AlchitryAuPlus))
        assertFalse(project.canSwitchTo(Board.AlchitryCu))
        assertFalse(project.canSwitchTo(Board.AlchitryPtV2T100))
        tempDir.deleteRecursively()
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun setBoardUpdatesProjectDataAndSaves() {
        val tempDir = Files.createTempDirectory("testProjSetBoard")
        val alpFile = tempDir.resolve("TestProj.alp").toFile()
        val projData = ProjectData1V3(
            projectName = "TestProj",
            board = Board.AlchitryAu,
            sourceFiles = emptySet(),
            constraintFiles = emptySet(),
            ipCores = emptySet()
        )
        val project = Project(tempDir, projData)
        project.save()

        project.setBoard(Board.AlchitryCu)

        assertEquals(Board.AlchitryCu, Project.current?.data?.board)
        assertEquals(Board.AlchitryCu.name, Settings.boardType)

        val reloaded = Project.load(alpFile)
        assertEquals(Board.AlchitryCu, reloaded.data.board)

        Project.close()
        tempDir.deleteRecursively()
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun diskFileModificationDetection() {
        val tempDir = Files.createTempDirectory("testDiskFileMod")
        val filePath = tempDir.resolve("test.luc")
        filePath.writeText("initial content")

        val diskFile = FileProvider.DiskFile(tempDir.relativize(filePath))
        val sourceFile = SourceFile(diskFile)
        val projData = ProjectData1V3(
            projectName = "TestProj",
            board = Board.AlchitryAu,
            sourceFiles = setOf(sourceFile),
            constraintFiles = emptySet(),
            ipCores = emptySet()
        )
        val project = Project(tempDir, projData)
        Project.open(project)

        // Initial read
        assertEquals("initial content", diskFile.readText())
        assertFalse(diskFile.wasModifiedOnDisk())

        // Writing via diskFile should update cache and not trigger wasModifiedOnDisk
        diskFile.writeText("updated content")
        assertFalse(diskFile.wasModifiedOnDisk())
        assertEquals("updated content", diskFile.readText())

        // External file modification should trigger wasModifiedOnDisk
        filePath.writeText("externally modified content")
        // Force timestamp change if on fast filesystem
        filePath.toFile().setLastModified(System.currentTimeMillis() + 5000)
        assertTrue(diskFile.wasModifiedOnDisk())

        // Reading the modified content updates the cache and clears wasModifiedOnDisk
        assertEquals("externally modified content", diskFile.readText())
        assertFalse(diskFile.wasModifiedOnDisk())

        Project.close()
        tempDir.deleteRecursively()
    }
}