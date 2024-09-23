package com.alchitry.labs2.project

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ProjectVersion.Companion::class)
data class ProjectVersion(
    val major: Int,
    val minor: Int
) : Comparable<ProjectVersion> {
    companion object : KSerializer<ProjectVersion> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("version", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): ProjectVersion {
            val version = decoder.decodeString().split(".")
            check(version.size == 2) { "Version number must be MAJOR.MINOR format!" }
            val major = version[0].toIntOrNull() ?: error("Major version was not a number: \"${version[0]}\"")
            val minor = version[1].toIntOrNull() ?: error("Minor version was not a number: \"${version[0]}\"")
            return ProjectVersion(major, minor)
        }

        override fun serialize(encoder: Encoder, value: ProjectVersion) {
            encoder.encodeString("${value.major}.${value.minor}")
        }
    }

    override fun compareTo(other: ProjectVersion): Int {
        return when {
            this.major < other.major -> -1
            this.major > other.major -> 1
            else -> when {
                this.minor < other.minor -> -1
                this.minor > other.minor -> 1
                else -> 0
            }
        }
    }

    override fun toString(): String {
        return "V$major.$minor"
    }

    fun isLatest(): Boolean = this == ProjectData.latestVersion
}