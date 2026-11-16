package basic

import com.alchitry.hardware.Board
import com.alchitry.labs2.project.*
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.SourceFile
import java.io.File
import java.nio.file.Files
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlin.test.*

class RenameModuleTests {

    @Test
    fun testRenameLucidModuleInContent() {
        val original = """
            // Comment before
            module my_old_module (
                input clk,  // clock
                input rst,  // reset
                output out
            ) {
                always {
                    out = 0;
                }
            }
        """.trimIndent()

        val renamed = renameModuleInContent(original, "my_new_module", Languages.Lucid, "my_old_module")
        assertTrue(renamed.contains("module my_new_module ("))
        assertTrue(renamed.contains("// Comment before"))
        assertFalse(renamed.contains("my_old_module"))
    }

    @Test
    fun testRenameLucidTestBenchInContent() {
        val original = """
            testbench old_tb {
                sig clk;
                fun tick_clock() {
                    clk = 1;
                }
            }
        """.trimIndent()

        val renamed = renameModuleInContent(original, "new_tb", Languages.Lucid, "old_tb")
        assertTrue(renamed.contains("testbench new_tb {"))
        assertFalse(renamed.contains("old_tb"))
    }

    @Test
    fun testRenameVerilogModuleInContent() {
        val original = """
            /* Verilog header */
            module old_verilog #(parameter WIDTH = 8) (
                input clk,
                output reg [WIDTH-1:0] out
            );
                always @* begin
                    out = 0;
                end
            endmodule
        """.trimIndent()

        val renamed = renameModuleInContent(original, "new_verilog", Languages.Verilog, "old_verilog")
        assertTrue(renamed.contains("module new_verilog #"))
        assertTrue(renamed.contains("/* Verilog header */"))
        assertFalse(renamed.contains("old_verilog"))
    }

    @Test
    fun testProjectRenameModuleLucid() {
        val tempDir = Files.createTempDirectory("rename_test_project")
        try {
            val sourceDir = tempDir.resolve("source").createDirectories()
            val moduleFile = sourceDir.resolve("blinker.luc")
            val originalCode = """
                module blinker (
                    input clk,
                    input rst,
                    output out
                ) {
                    always {
                        out = 0;
                    }
                }
            """.trimIndent()
            moduleFile.writeText(originalCode)

            val sourceFile = SourceFile(FileProvider.DiskFile("source/blinker.luc"), top = false)
            val projectData = ProjectData1V3(
                projectName = "TestProject",
                board = Board.AlchitryAu,
                sourceFiles = setOf(sourceFile),
                constraintFiles = emptySet(),
                ipCores = emptySet()
            )

            val project = Project(tempDir, projectData)
            project.save()

            Project.open(project)

            val newSourceFile = project.renameModule(sourceFile, "led_blinker")
            assertNotNull(newSourceFile)
            assertEquals("led_blinker.luc", newSourceFile.name)
            assertFalse(newSourceFile.top)

            assertFalse(File(tempDir.toFile(), "source/blinker.luc").exists())
            val newFile = File(tempDir.toFile(), "source/led_blinker.luc")
            assertTrue(newFile.exists())
            assertTrue(newFile.readText().contains("module led_blinker ("))
            assertFalse(newFile.readText().contains("module blinker ("))

            val currentProj = Project.current
            assertNotNull(currentProj)
            assertEquals(1, currentProj.data.sourceFiles.size)
            assertTrue(currentProj.data.sourceFiles.any { it.name == "led_blinker.luc" })
            assertFalse(currentProj.data.sourceFiles.any { it.name == "blinker.luc" })

            Project.close()
        } finally {
            tempDir.toFile().deleteRecursively()
        }
    }

    @Test
    fun testProjectRenameVerilogModule() {
        val tempDir = Files.createTempDirectory("rename_verilog_test_project")
        try {
            val sourceDir = tempDir.resolve("source").createDirectories()
            val moduleFile = sourceDir.resolve("alu.v")
            val originalCode = """
                module alu (
                    input [7:0] a,
                    input [7:0] b,
                    output reg [7:0] out
                );
                    always @* begin
                        out = a + b;
                    end
                endmodule
            """.trimIndent()
            moduleFile.writeText(originalCode)

            val sourceFile = SourceFile(FileProvider.DiskFile("source/alu.v"), top = false)
            val projectData = ProjectData1V3(
                projectName = "VerilogProject",
                board = Board.AlchitryAu,
                sourceFiles = setOf(sourceFile),
                constraintFiles = emptySet(),
                ipCores = emptySet()
            )

            val project = Project(tempDir, projectData)
            project.save()

            Project.open(project)

            val newSourceFile = project.renameModule(sourceFile, "custom_alu")
            assertNotNull(newSourceFile)
            assertEquals("custom_alu.v", newSourceFile.name)
            assertFalse(newSourceFile.top)

            assertFalse(File(tempDir.toFile(), "source/alu.v").exists())
            val newFile = File(tempDir.toFile(), "source/custom_alu.v")
            assertTrue(newFile.exists())
            assertTrue(newFile.readText().contains("module custom_alu ("))
            assertFalse(newFile.readText().contains("module alu ("))

            val currentProj = Project.current
            assertNotNull(currentProj)
            assertEquals(1, currentProj.data.sourceFiles.size)
            assertTrue(currentProj.data.sourceFiles.any { it.name == "custom_alu.v" })
            assertFalse(currentProj.data.sourceFiles.any { it.name == "alu.v" })

            Project.close()
        } finally {
            tempDir.toFile().deleteRecursively()
        }
    }
}
