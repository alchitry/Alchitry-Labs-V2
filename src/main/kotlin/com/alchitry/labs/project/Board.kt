package com.alchitry.labs.project

import com.alchitry.labs.allSealedObjects


sealed class Board {
    companion object {
        fun fromName(name: String): Board? =
            Board::class.allSealedObjects().firstOrNull { it.name.equals(name, ignoreCase = true) }

        val All: List<Board> get() = Board::class.allSealedObjects()
    }

    abstract val name: String
    abstract val fpgaName: String

    data object AlchitryAu : Board() {
        override val name = "Au"
        override val fpgaName = "xc7a35tftg256-1"
    }

    data object AlchitryAuPlus : Board() {
        override val name = "Au+"
        override val fpgaName = "xc7a100tftg256-1"
    }

    data object AlchitryCu : Board() {
        override val name = "Cu"
        override val fpgaName = "ICE40HX8K-CB132IC"
    }
}



