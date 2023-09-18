package com.alchitry.labs.hardware.usb

import com.alchitry.labs.hardware.usb.ftdi.LatticeSpi
import com.alchitry.labs.hardware.usb.ftdi.XilinxJtag
import com.alchitry.labs.project.Board
import java.io.File

interface BoardLoader {
    suspend fun eraseFlash()
    suspend fun writeBin(binFile: File, flash: Boolean)

    companion object {
        private suspend inline fun useLoader(board: Board, boardIdx: Int, block: (BoardLoader) -> Unit): Boolean {
            UsbUtil.openFtdiDevice(board, boardIdx).use { ftdi ->
                if (ftdi == null)
                    return false
                val loader = when (board) {
                    is Board.XilinxBoard -> XilinxJtag.init(ftdi, board)
                    Board.AlchitryCu -> LatticeSpi.init(ftdi)
                }
                block(loader)
            }
            return true
        }

        suspend fun erase(board: Board, boardIdx: Int): Boolean {
            return useLoader(board, boardIdx) {
                it.eraseFlash()
            }
        }

        suspend fun load(board: Board, boardIdx: Int, binFile: File, flash: Boolean): Boolean {
            return useLoader(board, boardIdx) {
                it.writeBin(binFile, flash)
            }
        }
    }
}