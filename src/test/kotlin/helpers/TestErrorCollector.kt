package helpers

import com.alchitry.labs.parsers.errors.NotationCollector
import com.alchitry.labs.project.files.FileProvider
import com.alchitry.labs.project.files.SourceFile

fun testErrorCollector(): NotationCollector = NotationCollector(SourceFile(FileProvider.DiskFile("test.luc"), true))