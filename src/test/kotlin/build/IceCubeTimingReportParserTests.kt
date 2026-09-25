package build

import com.alchitry.labs2.project.builders.IceCubeTimingReportParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class IceCubeTimingReportParserTests {
    private val report by lazy {
        val text = IceCubeTimingReportParserTests::class.java
            .getResourceAsStream("/timing/alchitry_top.srr")!!
            .bufferedReader().readText()
        IceCubeTimingReportParser.parse(text)
    }

    @Test
    fun testConstraintsNotMet() {
        assertEquals(false, report.constraintsMet)
    }

    @Test
    fun testClockSummary() {
        assertEquals(listOf("clk_0"), report.clocks.map { it.name })
        val clk0 = report.clocks.first()
        assertEquals(10.0, clk0.period)
        assertEquals(100.0, clk0.frequency)
        assertEquals(30.635, clk0.estimatedPeriod)
        assertEquals(32.6, clk0.estimatedFrequency)
        assertEquals(-20.635, clk0.slack)
        assertEquals("declared", clk0.clockType)
        assertEquals("default_clkgroup", clk0.clockGroup)
    }

    @Test
    fun testPassingAndFailingClocks() {
        assertEquals(listOf("clk_0"), report.failingClocks.map { it.name })
        assertEquals(emptyList<IceCubeTimingReportParser.Clock>(), report.passingClocks)
    }

    @Test
    fun testDesignSummary() {
        val summary = report.summary!!
        assertEquals(-20.635, summary.worstNegativeSlack)
        assertEquals(10, summary.setupFailingEndpoints)
        assertEquals(10, summary.setupTotalEndpoints)
        assertTrue(summary.totalNegativeSlack != null && summary.totalNegativeSlack < 0.0)
    }

    @Test
    fun testEndingPoints() {
        assertEquals(10, report.endingPoints.size)
        val first = report.endingPoints.first()
        assertEquals("cpu.D_reg_q_0_[2]", first.instance)
        assertEquals("clk_0", first.referenceClock)
        assertEquals("SB_DFFSR", first.type)
        assertEquals("D", first.pin)
        assertEquals("D_reg_d_0_[2]", first.net)
        assertEquals(9.895, first.requiredTime)
        assertEquals(-20.635, first.slack)
    }

    @Test
    fun testWorstFailingPath() {
        val worst = report.worstFailingPath!!
        assertEquals(-20.635, worst.slack)
        assertEquals("cpu.D_reg_q_0_[4] / Q", worst.source)
        assertEquals("cpu.D_reg_q_0_[2] / D", worst.destination)
        assertEquals("clk_0", worst.fromClock)
        assertEquals("clk_0", worst.toClock)
        assertEquals("Setup", worst.pathType)
        assertEquals(10.0, worst.requirement)
        assertEquals(30.529, worst.dataPathDelay)
        assertEquals(16, worst.logicLevels)
    }

    @Test
    fun testFailingPathsSorted() {
        val failing = report.failingPaths
        assertEquals(5, failing.size)
        assertTrue(failing.all { it.violated })
        assertTrue(failing.all { (it.slack ?: 0.0) < 0.0 })
        assertEquals(failing.map { it.slack }, failing.map { it.slack }.sortedBy { it })
    }

    @Test
    fun testPassingReport() {
        val passingReportText = """
            ##### START OF TIMING REPORT #####[
            Performance Summary
            *******************

            Worst slack in design: 2.500

                               Requested     Estimated     Requested     Estimated                 Clock        Clock           
            Starting Clock     Frequency     Frequency     Period        Period        Slack       Type         Group           
            --------------------------------------------------------------------------------------------------------------------
            clk_0              100.0 MHz     120.0 MHz     10.000        8.333         2.500       declared     default_clkgroup
            ====================================================================================================================

            Ending Points with Worst Slack
            ******************************

                                   Starting                                              Required            
            Instance               Reference     Type         Pin     Net                Time         Slack  
                                   Clock                                                                     
            -------------------------------------------------------------------------------------------------
            cpu.D_reg_q_0_[2]      clk_0         SB_DFFSR     D       D_reg_d_0_[2]      9.895        2.500
            =================================================================================================
            ##### END OF TIMING REPORT #####]
        """.trimIndent()

        val parsed = IceCubeTimingReportParser.parse(passingReportText)
        assertEquals(true, parsed.constraintsMet)
        assertEquals(1, parsed.clocks.size)
        assertEquals(1, parsed.passingClocks.size)
        assertEquals(0, parsed.failingClocks.size)
        assertEquals(0, parsed.failingPaths.size)
        assertEquals(0, parsed.summary?.setupFailingEndpoints)
        assertEquals(0.0, parsed.summary?.worstNegativeSlack)
    }

    @Test
    fun testUtilization() {
        val util = report.utilization!!
        assertEquals(1295, util.logicCells?.used)
        assertEquals(16.0, util.logicCells?.percentage)
        assertEquals(140, util.dffs?.used)
        assertEquals(1.0, util.dffs?.percentage)
        assertEquals(0, util.bram?.used)
        assertEquals(32, util.bram?.available)
        assertEquals(12, util.ios?.used)
        assertEquals(1, util.globalBuffers?.used)
        assertEquals(29, util.carry?.used)
        assertEquals(128, util.cellUsage["SB_DFFSR"])
        assertEquals(8, util.cellUsage["SB_DFFESR"])
        assertEquals(4, util.cellUsage["SB_DFFS"])
        assertEquals(1295, util.cellUsage["SB_LUT4"])
    }
}
