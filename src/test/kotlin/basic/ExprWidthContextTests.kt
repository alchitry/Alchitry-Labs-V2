package basic

import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ExprWidthContextTests {

    @Test
    fun basicWidthText() = runBlocking {
        val tester = ProjectTester(
            """
                module rca #(
                    SIZE = 4 : SIZE > 1
                )(
                    input cin,
                    output cout
                ) {
                    sig testing[8]
                    
                    always {
                        cout = cin
                        testing = 15+6
                        testing = testing[SIZE]
                        
                    }
                }
            """.trimIndent().toSourceFile()
        )

        tester.fullParse()
        tester.assertNoIssues()
    }
}