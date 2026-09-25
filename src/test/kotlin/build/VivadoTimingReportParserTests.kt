package build

import com.alchitry.labs2.project.builders.VivadoTimingReportParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VivadoTimingReportParserTests {
    private val report by lazy {
        val text = VivadoTimingReportParserTests::class.java
            .getResourceAsStream("/timing/alchitry_top_timing_summary_routed.rpt")!!
            .bufferedReader().readText()
        VivadoTimingReportParser.parse(text)
    }

    @Test
    fun testConstraintsNotMet() {
        assertEquals(false, report.constraintsMet)
    }

    @Test
    fun testDesignSummary() {
        val summary = report.summary!!
        assertEquals(-0.990, summary.worstNegativeSlack)
        assertEquals(-19.795, summary.totalNegativeSlack)
        assertEquals(42, summary.setupFailingEndpoints)
        assertEquals(2551, summary.setupTotalEndpoints)
        assertEquals(0.118, summary.worstHoldSlack)
        assertEquals(0.0, summary.totalHoldSlack)
        assertEquals(0, summary.holdFailingEndpoints)
        assertEquals(0.408, summary.worstPulseWidthSlack)
        assertEquals(0, summary.pulseWidthFailingEndpoints)
        assertEquals(937, summary.pulseWidthTotalEndpoints)
    }

    @Test
    fun testClockSummary() {
        assertEquals(
            listOf("clk_0", "clk_out_125_clk_wiz_0_1", "clk_out_500_clk_wiz_0_1", "clkfbout_clk_wiz_0_1"),
            report.clocks.map { it.name }
        )
        val clk125 = report.clocks.first { it.name == "clk_out_125_clk_wiz_0_1" }
        assertEquals(8.0, clk125.period)
        assertEquals(125.0, clk125.frequency)
        assertEquals("0.000 4.000", clk125.waveform)
        assertEquals(-0.990, clk125.slack)
        assertEquals(8.990, clk125.achievedPeriod)
        assertEquals(111.235, clk125.achievedFrequency)
        assertEquals(111.235, clk125.maxFrequency)

        val clk0 = report.clocks.first { it.name == "clk_0" }
        assertEquals(10.0, clk0.period)
        assertEquals(100.0, clk0.frequency)
        assertEquals(null, clk0.slack)
        assertEquals(null, clk0.achievedFrequency)
    }

    @Test
    fun testFailedReportAchievedFrequency() {
        val text = VivadoTimingReportParserTests::class.java
            .getResourceAsStream("/timing/fc_testing_timing_summary_routed.rpt")!!
            .bufferedReader().readText()
        val parsed = VivadoTimingReportParser.parse(text)
        assertEquals(false, parsed.constraintsMet)

        val clk125 = parsed.clocks.first { it.name == "clk_out_125_clk_wiz_0_1" }
        assertEquals(8.0, clk125.period)
        assertEquals(125.0, clk125.frequency)
        assertEquals(-0.065, clk125.slack)
        assertEquals(8.065, clk125.achievedPeriod)
        assertEquals(123.993, clk125.achievedFrequency)
        assertEquals(123.993, clk125.maxFrequency)

        val failingClocks = parsed.failingClocks
        assertEquals(listOf("clk_out_125_clk_wiz_0_1"), failingClocks.map { it.name })
    }

    @Test
    fun testClockPairs() {
        val clk125 = report.clockPairs.first {
            it.fromClock == "clk_out_125_clk_wiz_0_1" && it.toClock == "clk_out_125_clk_wiz_0_1"
        }
        assertEquals(false, clk125.passed)
        assertEquals(42, clk125.setup.failingEndpoints)
        assertEquals(-0.990, clk125.setup.worstSlack)
        assertEquals(-19.795, clk125.setup.totalViolation)
        assertEquals(0, clk125.hold.failingEndpoints)
        assertEquals(0.118, clk125.hold.worstSlack)
        assertEquals(0, clk125.pulseWidth.failingEndpoints)

        val clk0 = report.clockPairs.first { it.fromClock == "clk_0" && it.toClock == "clk_0" }
        assertEquals(true, clk0.passed)
        assertEquals(null, clk0.setup.failingEndpoints)
        assertEquals(3.000, clk0.pulseWidth.worstSlack)
    }

    @Test
    fun testPassingAndFailingClocks() {
        assertEquals(listOf("clk_out_125_clk_wiz_0_1"), report.failingClocks.map { it.name })
        assertEquals(
            listOf("clk_0", "clk_out_500_clk_wiz_0_1", "clkfbout_clk_wiz_0_1"),
            report.passingClocks.map { it.name }
        )
    }

    @Test
    fun testWorstFailingPath() {
        val worst = report.worstFailingPath!!
        assertEquals(-0.990, worst.slack)
        assertEquals("quad/D_count_q_reg[6]_replica/C", worst.source)
        assertEquals("vc/D_velocity_filted_q2_i_22_psdsp_3/D", worst.destination)
        assertEquals("clk_out_125_clk_wiz_0_1", worst.pathGroup)
        assertEquals("Setup (Max at Slow Process Corner)", worst.pathType)
        assertEquals(8.0, worst.requirement)
        assertEquals(8.686, worst.dataPathDelay)
        assertEquals(10, worst.logicLevels)
        assertEquals("clk_out_125_clk_wiz_0_1", worst.fromClock)
        assertEquals("clk_out_125_clk_wiz_0_1", worst.toClock)
    }

    @Test
    fun testFailingPathsSorted() {
        val failing = report.failingPaths
        assertEquals(10, failing.size) // report was generated with -max_paths 10
        assert(failing.all { it.violated })
        assert(failing.all { (it.slack ?: 0.0) < 0.0 })
        assertEquals(failing.map { it.slack }, failing.map { it.slack }.sortedBy { it })
    }
}
