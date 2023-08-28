package helpers

import com.alchitry.labs.parsers.errors.ErrorCollector
import com.alchitry.labs.project.files.FileProvider
import com.alchitry.labs.project.files.SourceFile

fun testErrorCollector(): ErrorCollector = ErrorCollector(SourceFile(FileProvider.DiskFile("test.luc"), true))