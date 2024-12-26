package com.alchitry.labs2.hardware.usb

import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.hardware.usb.ftdi.LatticeSpi
import com.alchitry.labs2.hardware.usb.ftdi.XilinxJtag
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
                    Board.AlchitryCu, Board.AlchitryCuV2 -> LatticeSpi.init(ftdi)
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
                try {
                    it.writeBin(binFile, flash)
                } catch (t: Throwable) {
                    println(t)
                    throw t
                }
            }
        }
    }
}