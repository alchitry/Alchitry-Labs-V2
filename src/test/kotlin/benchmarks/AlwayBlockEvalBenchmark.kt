package benchmarks

import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

private val waveSource = """
    module wave #(
        RES = 8 : RES > 1,
        CTR_LEN = 25 : CTR_LEN >= RES+1
    )(
        input clk,     // clock
        input rst,     // reset
        output out[8]  // LED output
    ) {


        // counter
        dff ctr[CTR_LEN](.clk(clk),.rst(rst));

        sig acmp[RES];   // intermediate value
        sig result[RES+1]; // intermediate value

        always {
            // increment the counter
            ctr.d = ctr.q +1;
            out = 0;

            // for each output
            repeat (i, 8) {
                // take the top bits of the counter and
                // offset differently them for each output
                result = ctr.q[CTR_LEN-1-:RES+1] + i * ${'$'}pow(2,RES) / 4;

                // invert the result to count down
                acmp = result[RES]? ~result[RES-1:0] : result[RES-1:0];

                // PWM output
                out[i] = acmp > ctr.q[RES-1:0];
            }
        }
    }
""".trimIndent().toSourceFile("wave.luc")

class EvaluationBenchmarks {
    @Test
    fun benchmarkWaveSimulation() = runBlocking {
        val tester = LucidTester(waveSource)
        val reset = Signal(
            "rst",
            SignalDirection.Write,
            null,
            BitListValue(0, 1, false),
            ExprType.Dynamic,
            false
        )

        val clk = Signal(
            "clk",
            SignalDirection.Write,
            null,
            BitListValue(0, 1, false),
            ExprType.Dynamic,
            false
        )
        val leds = Signal(
            "led",
            SignalDirection.Read,
            null,
            BitListValue(0, 8, signed = false),
            ExprType.Dynamic,
            false
        )

        val trees = tester.parseText()
        tester.globalParse(trees)
        val modules = tester.moduleTypeParse(trees)

        val top = ModuleInstance(
            "auSimulator",
            tester.project,
            null,
            modules.first(),
            mapOf(),
            mapOf(),
            mapOf(
                "out" to leds,
                "rst" to reset,
                "clk" to clk
            )
        )

        top.initialWalk()
        top.context.initialize()

        val low = Bit.B0.toBitValue(signed = false)
        val high = Bit.B1.toBitValue(signed = false)

        val startTime = TimeSource.Monotonic.markNow()
        var loopCount = 0
        while (startTime.elapsedNow() < 10.seconds) {
            clk.write(high)
            tester.project.processQueue()
            clk.write(low)
            tester.project.processQueue()
            loopCount += 1
        }
        val rate = loopCount / 10
        val relative = (rate.toFloat() / 845f * 100f).roundToInt()
        println("Evaluation rate: $rate hz")
        println("Relative performance: $relative%")
    }
}