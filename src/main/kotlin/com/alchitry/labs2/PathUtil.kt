package com.alchitry.labs2

import java.io.File

object PathUtil {
    fun assembleLinuxPath(vararg pieces: String): String {
        return pieces.filter { it.isNotBlank() }.joinToString("/")
    }

    fun assemblePath(parent: File, vararg pieces: String): String {
        return parent.absolutePath + File.separator + pieces.filter { it.isNotBlank() }.joinToString(File.separator)
    }

    fun assemblePath(vararg pieces: String): String {
        return pieces
            .filter { it.isNotBlank() }
            .joinToString(File.separator) {
                if (it.endsWith(File.separatorChar))
                    it.substring(0, it.length - 1)
                else
                    it
            }
    }

    fun assembleFile(parent: File, vararg pieces: String): File {
        return File(assemblePath(parent, *pieces))
    }

    fun assembleFile(vararg pieces: String): File {
        return File(assemblePath(*pieces))
    }
}