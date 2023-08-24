package com.alchitry.labs.project.files

import com.alchitry.labs.Log
import com.alchitry.labs.project.Locations
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.NoSuchFileException

interface ProjectFile {
    val file: File
    val name: String

    fun isLibFile(skipExistCheck: Boolean = false): Boolean {
        return try {
            val libVersion =
                this::class.java.getResource("${Locations.components}/${file.name}")?.toURI()?.let { File(it) }
                    ?: return false
            if (!skipExistCheck && !libVersion.exists())
                false
            else
                Files.isSameFile(libVersion.toPath(), file.toPath())
        } catch (e: NoSuchFileException) {
            false
        } catch (e: IOException) {
            Log.printError("Failed to read file!", e)
            false
        }
    }

    fun getContents(): String {
        return String(Files.readAllBytes(file.toPath()))
    }
}