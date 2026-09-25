package build

import com.alchitry.labs2.project.builders.IceStormTimingReportParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class IceStormTimingReportParserTests {
    private fun loadReport(resourcePath: String): IceStormTimingReportParser.TimingReport {
        val text = IceStormTimingReportParserTests::class.java
            .getResourceAsStream(resourcePath)!!
            .bufferedReader().readText()
        return IceStormTimingReportParser.parse(text)
    }

    @Test
    fun testFailReport() {
        val report = loadReport("/timing/alchitry_fail.rpt")

        assertEquals(false, report.constraintsMet)

        assertEquals(1, report.clocks.size)
        val clock = report.clocks.first()
        assertEquals("clk\$SB_IO_IN_\$glb_clk", clock.name)
        assertEquals(100.0, clock.frequency)
        assertEquals(10.0, clock.period)
        assertEquals(-7.728, clock.slack)
        assertEquals(17.728, clock.achievedPeriod)
        assertEquals(56.408, clock.achievedFrequency)

        assertEquals(1, report.failingClocks.size)
        assertEquals(0, report.passingClocks.size)

        val summary = report.summary!!
        assertEquals(-7.728, summary.worstNegativeSlack)
        assertEquals(-7.728, summary.totalNegativeSlack)
        assertEquals(1, summary.setupFailingEndpoints)
        assertEquals(4, summary.setupTotalEndpoints)

        assertEquals(1, report.failingPaths.size)
        val worst = report.worstFailingPath!!
        assertEquals(-7.728, worst.slack)
        assertEquals("cpu.D_reg_q_SB_DFFESR_Q_64_D_SB_LUT4_O_LC/O", worst.source)
        assertEquals("cpu.D_reg_q_SB_DFFESR_Q_64_D_SB_LUT4_O_LC/CEN", worst.destination)
        assertEquals("clk\$SB_IO_IN_\$glb_clk", worst.fromClock)
        assertEquals("clk\$SB_IO_IN_\$glb_clk", worst.toClock)
        assertEquals("Setup", worst.pathType)
        assertEquals(10.0, worst.requirement)
        assertEquals(17.728, worst.dataPathDelay)
        assertEquals(10, worst.logicLevels)
        assertTrue(worst.violated)

        val util = report.utilization!!
        assertEquals(365, util.logicCells?.used)
        assertEquals(7680, util.logicCells?.available)
        assertEquals(0, util.bram?.used)
        assertEquals(32, util.bram?.available)
        assertEquals(0, util.plls?.used)
        assertEquals(2, util.plls?.available)
        assertEquals(3, util.globalBuffers?.used)
        assertEquals(8, util.globalBuffers?.available)
        assertEquals(12, util.ios?.used)
        assertEquals(256, util.ios?.available)
        assertEquals(0, util.warmboot?.used)
        assertEquals(1, util.warmboot?.available)
    }

    @Test
    fun testPassReport() {
        val report = loadReport("/timing/alchitry_pass.rpt")

        assertEquals(true, report.constraintsMet)

        assertEquals(1, report.clocks.size)
        val clock = report.clocks.first()
        assertEquals("clk\$SB_IO_IN_\$glb_clk", clock.name)
        assertEquals(100.0, clock.frequency)
        assertEquals(10.0, clock.period)
        assertEquals(7.263, clock.slack)
        assertEquals(2.737, clock.achievedPeriod)
        assertEquals(365.364, clock.achievedFrequency)

        assertEquals(0, report.failingClocks.size)
        assertEquals(1, report.passingClocks.size)
        assertEquals(0, report.failingPaths.size)

        val summary = report.summary!!
        assertEquals(0.0, summary.worstNegativeSlack)
        assertEquals(0.0, summary.totalNegativeSlack)
        assertEquals(0, summary.setupFailingEndpoints)
        assertEquals(4, summary.setupTotalEndpoints)

        val util = report.utilization!!
        assertEquals(17, util.logicCells?.used)
        assertEquals(7680, util.logicCells?.available)
        assertEquals(0, util.bram?.used)
        assertEquals(32, util.bram?.available)
        assertEquals(1, util.globalBuffers?.used)
        assertEquals(8, util.globalBuffers?.available)
        assertEquals(12, util.ios?.used)
        assertEquals(256, util.ios?.available)
    }

    @Test
    fun testNoClkReport() {
        val report = loadReport("/timing/alchitry_no_clk.rpt")

        assertEquals(true, report.constraintsMet)
        assertEquals(0, report.clocks.size)
        assertEquals(0, report.passingClocks.size)
        assertEquals(0, report.failingClocks.size)
        assertEquals(0, report.failingPaths.size)

        val summary = report.summary!!
        assertEquals(0.0, summary.worstNegativeSlack)
        assertEquals(0.0, summary.totalNegativeSlack)
        assertEquals(0, summary.setupFailingEndpoints)
        assertEquals(1, summary.setupTotalEndpoints)

        val util = report.utilization!!
        assertEquals(1, util.logicCells?.used)
        assertEquals(7680, util.logicCells?.available)
        assertEquals(0, util.bram?.used)
        assertEquals(32, util.bram?.available)
        assertEquals(0, util.globalBuffers?.used)
        assertEquals(8, util.globalBuffers?.available)
        assertEquals(12, util.ios?.used)
        assertEquals(256, util.ios?.available)
    }
}
