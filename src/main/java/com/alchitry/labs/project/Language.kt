package com.alchitry.labs.project

import com.alchitry.labs.allSealedObjects

sealed class Language {
    abstract val name: String
    abstract val extension: String

    companion object {
        val all by lazy { Language::class.allSealedObjects() }
        fun fromName(name: String) = all.firstOrNull { it.name == name }
        fun fromExt(ext: String) = all.firstOrNull { it.extension == ext }
    }
}

sealed class HDL : Language() {
    companion object {
        val all by lazy { HDL::class.allSealedObjects() }
        fun fromName(name: String) = all.firstOrNull { it.name == name }
        fun fromExt(ext: String) = all.firstOrNull { it.extension == ext }
    }
}

sealed class ConstraintLang : Language() {
    companion object {
        val all by lazy { ConstraintLang::class.allSealedObjects() }
        fun fromName(name: String) = all.firstOrNull { it.name == name }
        fun fromExt(ext: String) = all.firstOrNull { it.extension == ext }
    }
}

object Languages {
    object Verilog : HDL() {
        override val name: String = "verilog"
        override val extension: String = "v"
    }

    object Lucid : HDL() {
        override val name: String = "lucid"
        override val extension: String = "luc"
    }

    object ACF : ConstraintLang() {
        override val name: String = "acf"
        override val extension: String = "acf"
    }

    object UCF : ConstraintLang() {
        override val name: String = "ucf"
        override val extension: String = "ucf"
    }

    object XDC : ConstraintLang() {
        override val name: String = "xdc"
        override val extension: String = "xdc"
    }
}


