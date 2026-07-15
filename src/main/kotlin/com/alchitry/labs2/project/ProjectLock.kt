package com.alchitry.labs2.project

import com.alchitry.labs2.Env
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileLock
import java.nio.file.Files
import java.nio.file.LinkOption

class ProjectAlreadyOpenException(message: String) : Exception(message)

object ProjectLock {
    private var lockFile: RandomAccessFile? = null
    private var fileLock: FileLock? = null

    private fun lockFileFor(projectFile: File): File =
        File(projectFile.parentFile, ".${projectFile.nameWithoutExtension}.lock")

    /**
     * Checks if the project file is locked by another instance.
     * Returns true if the project appears to be open elsewhere.
     */
    fun isProjectLocked(projectFile: File): Boolean {
        val lock = lockFileFor(projectFile)
        if (Env.isWindows && !lock.exists()) { // mark hidden on Windows
            try {
                lock.createNewFile()
                Files.setAttribute(lock.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS)
            } catch (_: Exception) {
            }
        }
        return try {
            val raf = RandomAccessFile(lock, "rw")
            val fl = raf.channel.tryLock()
            if (fl != null) {
                fl.release()
                raf.close()
                false
            } else {
                raf.close()
                true
            }
        } catch (_: Exception) {
            // If we can't check, assume it might be locked
            true
        }
    }

    fun acquireLock(projectFile: File) {
        val lock = lockFileFor(projectFile)
        if (Env.isWindows && !lock.exists()) { // mark hidden on Windows
            try {
                lock.createNewFile()
                Files.setAttribute(lock.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS)
            } catch (_: Exception) {
            }
        }
        try {
            val raf = RandomAccessFile(lock, "rw")
            val fl = raf.channel.tryLock()
            if (fl != null) {
                lockFile = raf
                fileLock = fl
            } else {
                raf.close()
            }

        } catch (_: Exception) {
            // Best effort locking
        }
    }

    fun releaseLock() {
        try {
            fileLock?.release()
            fileLock = null
            lockFile?.close()
            lockFile = null
        } catch (_: Exception) {
            // Best effort cleanup
        }
    }
}
