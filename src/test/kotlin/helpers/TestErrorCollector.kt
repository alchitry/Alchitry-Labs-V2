package helpers

import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.project.files.SourceFile
import java.io.File

fun testErrorCollector(): ErrorCollector = ErrorCollector(SourceFile(File("test.luc"), true))