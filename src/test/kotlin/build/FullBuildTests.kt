package build

import com.alchitry.labs2.project.Board
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.ProjectTemplate
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class FullBuildTests {
    @field:TempDir
    lateinit var tempFolder: File

    @Test
    fun buildAllExampleProjects() = runBlocking {
        System.setProperty("app.dir", "../")
        val templates = ProjectTemplate.getTemplates()

        Board.All.forEach { board ->
            val validTemplates = templates.filter { it.boards.contains(board) }
            validTemplates.forEach { template ->
                val project = template.copyTo("${template.name}_${board.name}", tempFolder, board)
                println("Building ${template.name} for $board.")
                Project.open(project)
                assert(project.build()) { "Failed to build ${template.name} for $board!" }
                assert(project.binFile.exists()) { "Bin file does not exist for ${template.name} with $board!" }
            }
        }
    }
}