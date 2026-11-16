package basic

import com.alchitry.labs2.ui.alchitry_text_field.styles.TextProvider
import com.alchitry.labs2.ui.alchitry_text_field.styles.lucid.LucidFormatter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FormatterTests {
    private class StringTextProvider(private val text: String) : TextProvider {
        private val lines = text.lines()
        override fun getText(): String = text
        override fun getLine(line: Int): String? = lines.getOrNull(line)
        override fun getLineCount(): Int = lines.size
    }

    private fun assertIndent(expectedIndent: String, src: String, lineNum: Int) {
        val formatter = LucidFormatter()
        val indent = formatter.getIndentFor(StringTextProvider(src), lineNum)
        assertEquals(expectedIndent, indent, "Mismatch for line $lineNum in:\n$src")
    }

    @Test
    fun testIndentPortListTypingMiddle() {
        val src = """
            module myModule (
                input clk,
                o
                output rst
            ) {
            }
        """.trimIndent()
        assertIndent("    ", src, 2)
    }

    @Test
    fun testIndentPortListWithTrailingComma() {
        val src = """
            module myModule (
                input clk,
                o
            ) {
            }
        """.trimIndent()
        assertIndent("    ", src, 2)
    }

    @Test
    fun testIndentParamListWithTrailingComma() {
        val src = """
            module myModule #(
                PARAM_A = 1,
                p
            ) (
            ) {
            }
        """.trimIndent()
        assertIndent("    ", src, 2)
    }

    @Test
    fun testIndentEnumWithTrailingComma() {
        val src = """
            global MyGlobal {
                enum MyEnum {
                    FOO,
                    b
                };
            }
        """.trimIndent()
        assertIndent("        ", src, 3)
    }

    @Test
    fun testIndentStructWithTrailingComma() {
        val src = """
            global MyGlobal {
                struct MyStruct {
                    a[8],
                    b
                };
            }
        """.trimIndent()
        assertIndent("        ", src, 3)
    }

    @Test
    fun testIndentInstConsWithTrailingComma() {
        val src = """
            module myModule (
                input clk
            ) {
                dff myDff (
                    .clk(clk),
                    .rst(rst)
                );
            }
        """.trimIndent()
        assertIndent("        ", src, 5)
    }

    @Test
    fun testIndentModuleInstTypingDot() {
        val src = """
            module myModule (
                input clk
            ) {
                myOtherModule inst (
                    .clk(clk),
                    .
                );
            }
        """.trimIndent()
        assertIndent("        ", src, 5)
    }

    @Test
    fun testIndentModuleInstTypingDotName() {
        val src = """
            module myModule (
                input clk
            ) {
                myOtherModule inst (
                    .clk(clk),
                    .rst
                );
            }
        """.trimIndent()
        assertIndent("        ", src, 5)
    }

    @Test
    fun testIndentModuleInstTypingDotNameParen() {
        val src = """
            module myModule (
                input clk
            ) {
                myOtherModule inst (
                    .clk(clk),
                    .rst(
                );
            }
        """.trimIndent()
        assertIndent("        ", src, 5)
    }

    @Test
    fun testIndentModuleInstTypingDotMiddle() {
        val src = """
            module myModule (
                input clk
            ) {
                myOtherModule inst (
                    .clk(clk),
                    .
                    .rst(rst)
                );
            }
        """.trimIndent()
        assertIndent("        ", src, 5)
    }

    @Test
    fun testIndentStructConstTypingDotMiddle() {
        val src = """
            global MyGlobal {
                const MY_STRUCT = <myStruct>(
                    .a(1),
                    .
                    .b(2)
                );
            }
        """.trimIndent()
        assertIndent("        ", src, 3)
    }

    @Test
    fun testIndentStructConstTypingDot() {
        val src = """
            global MyGlobal {
                const MY_STRUCT = <myStruct>(
                    .a(1),
                    .
                );
            }
        """.trimIndent()
        assertIndent("        ", src, 3)
    }

    @Test
    fun testIndentModuleInstTypingDotFirst() {
        val src = """
            module myModule (
                input clk
            ) {
                myOtherModule inst (
                    .
                );
            }
        """.trimIndent()
        assertIndent("        ", src, 4)
    }

    @Test
    fun testIndentConcatWithTrailingComma() {
        val src = """
            module myModule () {
                always {
                    a = c{
                        8h12,
                        8
                    };
                }
            }
        """.trimIndent()
        assertIndent("            ", src, 4)
    }

    @Test
    fun testIndentArrayWithTrailingComma() {
        val src = """
            module myModule () {
                always {
                    a = {
                        8h12,
                        8
                    };
                }
            }
        """.trimIndent()
        assertIndent("            ", src, 4)
    }
}
