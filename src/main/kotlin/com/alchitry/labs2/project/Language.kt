package com.alchitry.labs2.project

import com.alchitry.labs2.allSealedObjects
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextTokenizer
import com.alchitry.labs2.ui.alchitry_text_field.styles.acf.AcfTokenizer
import com.alchitry.labs2.ui.alchitry_text_field.styles.lucid.LucidTokenizer
import com.alchitry.labs2.ui.alchitry_text_field.styles.verilog.VerilogTokenizer

sealed class Language {
    abstract val name: String
    abstract val extension: String
    abstract val tokenizer: TextTokenizer

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
    data object Verilog : HDL() {
        override val name: String = "verilog"
        override val extension: String = "v"
        override val tokenizer = VerilogTokenizer
    }

    data object Lucid : HDL() {
        override val name: String = "lucid"
        override val extension: String = "luc"
        override val tokenizer = LucidTokenizer
    }

    data object ACF : ConstraintLang() {
        override val name: String = "acf"
        override val extension: String = "acf"
        override val tokenizer = AcfTokenizer
    }

    data object XDC : ConstraintLang() {
        override val name: String = "xdc"
        override val extension: String = "xdc"
        override val tokenizer: TextTokenizer
            get() = TODO("Not yet implemented")
    }

    data object SDC : ConstraintLang() {
        override val name: String = "sdc"
        override val extension: String = "sdc"
        override val tokenizer: TextTokenizer
            get() = TODO("Not yet implemented")
    }

    data object PCF : ConstraintLang() {
        override val name: String = "pcf"
        override val extension: String = "pcf"
        override val tokenizer: TextTokenizer
            get() = TODO("Not yet implemented")
    }
}


