package com.alchitry.labs2.project

import kotlinx.serialization.Serializable

@Serializable
data class AlchitryLabsProjectData(
    val project: ProjectData,
    val template: ProjectTemplate? = null
)