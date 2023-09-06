package com.alchitry.labs.hardware.usb.ftdi

import java.util.*

enum class JtagState {
    TEST_LOGIC_RESET,
    RUN_TEST_IDLE,
    SELECT_DR_SCAN,
    CAPTURE_DR,
    SHIFT_DR,
    EXIT1_DR,
    PAUSE_DR,
    EXIT2_DR,
    UPDATE_DR,
    SELECT_IR_SCAN,
    CAPTURE_IR,
    SHIFT_IR,
    EXIT1_IR,
    PAUSE_IR,
    EXIT2_IR,
    UPDATE_IR;

    class Transistions {
        var currentState: JtagState
        var tms: Int
        var moves: Int

        constructor() {
            currentState = TEST_LOGIC_RESET
            tms = 0
            moves = 0
        }

        constructor(t: Transistions) {
            currentState = t.currentState
            tms = t.tms
            moves = t.moves
        }

        override fun toString(): String {
            val str = Integer.toBinaryString(tms)
            return str.substring(str.length - moves, str.length)
        }
    }

    fun next(tms: Boolean): JtagState {
        return when (this) {
            TEST_LOGIC_RESET -> if (tms) TEST_LOGIC_RESET else RUN_TEST_IDLE
            RUN_TEST_IDLE -> if (tms) SELECT_DR_SCAN else RUN_TEST_IDLE
            SELECT_DR_SCAN -> if (tms) SELECT_IR_SCAN else CAPTURE_DR
            CAPTURE_DR -> if (tms) EXIT1_DR else SHIFT_DR
            SHIFT_DR -> if (tms) EXIT1_DR else SHIFT_DR
            EXIT1_DR -> if (tms) UPDATE_DR else PAUSE_DR
            PAUSE_DR -> if (tms) EXIT2_DR else PAUSE_DR
            EXIT2_DR -> if (tms) UPDATE_DR else SHIFT_DR
            UPDATE_DR -> if (tms) SELECT_DR_SCAN else RUN_TEST_IDLE
            SELECT_IR_SCAN -> if (tms) TEST_LOGIC_RESET else CAPTURE_IR
            CAPTURE_IR -> if (tms) EXIT1_IR else SHIFT_IR
            SHIFT_IR -> if (tms) EXIT1_IR else SHIFT_IR
            EXIT1_IR -> if (tms) UPDATE_IR else PAUSE_IR
            PAUSE_IR -> if (tms) EXIT2_IR else PAUSE_IR
            EXIT2_IR -> if (tms) UPDATE_IR else SHIFT_IR
            UPDATE_IR -> if (tms) SELECT_DR_SCAN else RUN_TEST_IDLE
        }
    }

    fun getTransitions(end: JtagState): Transistions {
        return getTransitions(this, end)
    }

    companion object {
        fun getTransitions(start: JtagState, end: JtagState): Transistions {
            val queue: Queue<Transistions> = LinkedList()
            var t = Transistions()
            t.currentState = start
            t.moves = 0
            t.tms = 0
            queue.add(t)
            while (!queue.isEmpty()) {
                t = queue.remove()
                if (t.currentState == end) break
                val s0 = t.currentState.next(false)
                val s1 = t.currentState.next(true)
                t.moves++
                t.tms = t.tms and (1 shl t.moves - 1).inv()
                t.currentState = s0
                queue.add(Transistions(t))
                t.tms = t.tms or (1 shl t.moves - 1)
                t.currentState = s1
                queue.add(t)
            }
            return t
        }
    }
}
