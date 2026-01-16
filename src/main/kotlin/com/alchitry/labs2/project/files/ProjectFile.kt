package com.alchitry.labs2.project.files

import com.alchitry.labs2.project.Language
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextTokenizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CharStreams

@Serializable
sealed class ProjectFile(val file: FileProvider) {
    @Transient
    protected val lock = Mutex(false)

    val name: String get() = file.name
    val isLibFile: Boolean get() = file is Component
    val isReadOnly: Boolean get() = file.readOnly
    abstract val language: Language

    val textTokenizer: TextTokenizer get() = language.tokenizer

    suspend fun readText(): String = lock.withLock {
        withContext(Dispatchers.IO) {
            file.readText()
        }
    }

    suspend fun writeText(text: String) = lock.withLock {
        withContext(Dispatchers.IO) {
            file.writeText(text)
        }
    }

    suspend fun toCharStream(): CharStream = CharStreams.fromString(readText(), name)
}

