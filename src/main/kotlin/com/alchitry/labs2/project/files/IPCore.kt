package com.alchitry.labs2.project.files

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("IPCore")
data class IPCore(
    val name: String,
    val stub: SourceFile?,
    val files: Set<FileProvider.DiskFile>
)