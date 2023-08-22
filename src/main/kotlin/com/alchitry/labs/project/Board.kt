package com.alchitry.labs.project

import com.alchitry.labs.allSealedObjects


sealed class Board {
    companion object {
        fun fromName(name: String): Board? = Board::class.allSealedObjects().firstOrNull { it.name == name }
    }

    abstract val name: String
    abstract val fpgaName: String

    object AlchitryAu : Board() {
        override val name = "Alchitry Au"
        override val fpgaName = "xc7a35tftg256-1"
    }

    object AlchitryAuPlus : Board() {
        override val name = "Alchitry Au+"
        override val fpgaName = "xc7a100tftg256-1"
    }

    object AlchitryCu : Board() {
        override val name = "Alchitry Cu"
        override val fpgaName = "ICE40HX8K-CB132IC"
    }

    object Mojo : Board() {
        override val name = "Mojo"
        override val fpgaName = "xc6slx9-2tqg144"
    }
}



