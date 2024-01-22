package helpers

import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.SourceFile

fun testErrorCollector(): NotationCollector = NotationCollector(SourceFile(FileProvider.DiskFile("test.luc"), true))