package basic

import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.ProjectData
import com.alchitry.labs2.project.ProjectData1V0
import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.openXml
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.io.path.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectTests {
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