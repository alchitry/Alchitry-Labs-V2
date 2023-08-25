package helpers

import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.project.files.FileProvider
import com.alchitry.labs.project.files.SourceFile

fun testErrorCollector(): ErrorCollector = ErrorCollector(SourceFile(FileProvider.DiskFile("test.luc"), true))