package com.alchitry.labs2.project.builders

import java.util.*

/**
 * Parser for iCEcube2 / Synplify Pro timing reports found in the .srr synthesis log file.
 *
 * It extracts the overall pass/fail status, performance summary (clocks and slack),
 * worst slack in design, ending points with worst slack, and detailed path information.
 */
object IceCubeTimingReportParser {

    /** The design wide timing summary numbers. */
    data class DesignTimingSummary(
        val worstNegativeSlack: Double?,
        val totalNegativeSlack: Double?,
        val setupFailingEndpoints: Int?,
        val setupTotalEndpoints: Int?,
        val worstHoldSlack: Double? = null,
        val totalHoldSlack: Double? = null,
        val holdFailingEndpoints: Int? = null,
        val holdTotalEndpoints: Int? = null
    )

    /** A clock defined in the design from the "Performance Summary" section. */
    data class Clock(
        val name: String,
        val period: Double,
        val frequency: Double,
        val slack: Double?,
        val estimatedPeriod: Double? = null,
        val estimatedFrequency: Double? = null,
        val clockType: String? = null,
        val clockGroup: String? = null
    )

    /** An ending point from the "Ending Points with Worst Slack" section. */
    data class EndingPoint(
        val instance: String,
        val referenceClock: String,
        val type: String?,
        val pin: String?,
        val net: String?,
        val requiredTime: Double?,
        val slack: Double?
    )

    /** A single timing path from the "Worst Path Information" section of the report. */
    data class TimingPath(
        val slack: Double?,
        val violated: Boolean,
        val source: String,
        val destination: String,
        val fromClock: String?,
        val toClock: String?,
        val pathGroup: String? = null,
        val pathType: String? = null,
        val requirement: Double? = null,
        val dataPathDelay: Double? = null,
        val logicLevels: Int? = null
    )

    /** Summary of a hardware resource's usage and capacity. */
    data class ResourceUsage(
        val used: Int,
        val available: Int? = null,
        val explicitPercentage: Double? = null
    ) {
        val percentage: Double?
            get() = explicitPercentage ?: if (available != null && available > 0) {
                (used.toDouble() / available.toDouble()) * 100.0
            } else null
    }

    /** Device utilization statistics extracted from the report. */
    data class DeviceUtilization(
        val logicCells: ResourceUsage? = null,
        val dffs: ResourceUsage? = null,
        val bram: ResourceUsage? = null,
        val plls: ResourceUsage? = null,
        val globalBuffers: ResourceUsage? = null,
        val ios: ResourceUsage? = null,
        val carry: ResourceUsage? = null,
        val cellUsage: Map<String, Int> = emptyMap(),
        val all: Map<String, ResourceUsage> = emptyMap()
    ) {
        val lc: ResourceUsage? get() = logicCells
        val slice: ResourceUsage? get() = logicCells
        val luts: ResourceUsage? get() = logicCells
        val registers: ResourceUsage? get() = dffs
        val ram: ResourceUsage? get() = bram
        val pll: ResourceUsage? get() = plls
        val gb: ResourceUsage? get() = globalBuffers
        val io: ResourceUsage? get() = ios

        operator fun get(key: String): ResourceUsage? = all[key]
    }

    data class TimingReport(
        /** True if timing constraints are met, false if not met, null if timing was unchecked. */
        val constraintsMet: Boolean?,
        val summary: DesignTimingSummary?,
        val clocks: List<Clock>,
        val paths: List<TimingPath>,
        val endingPoints: List<EndingPoint> = emptyList(),
        val utilization: DeviceUtilization? = null
    ) {
        /** All paths that violated their timing requirement, worst first. */
        val failingPaths: List<TimingPath>
            get() = paths.filter { it.violated }
                .sortedBy { it.slack ?: Double.MAX_VALUE }

        /** The single worst failing path, or null if timing was met. */
        val worstFailingPath: TimingPath? get() = failingPaths.firstOrNull()

        /** Clocks whose slack is non-negative. */
        val passingClocks: List<Clock>
            get() = clocks.filter { (it.slack ?: 0.0) >= 0.0 }

        /** Clocks that have negative slack. */
        val failingClocks: List<Clock>
            get() = clocks.filter { (it.slack ?: 0.0) < 0.0 }
    }

    private val worstSlackRegex =
        Regex("""Worst slack in design:\s*(-?(?:\d+\.?\d*|inf)|NA|n/a)""", RegexOption.IGNORE_CASE)
    private val pathHeaderRegex = Regex("""^Path information for path number \d+:""")
    private val requestedPeriodRegex = Regex("""Requested Period:\s*(-?(?:\d+\.?\d*|inf))""")
    private val setupTimeRegex = Regex("""-\s*Setup time:\s*(-?(?:\d+\.?\d*|inf))""")
    private val propagationTimeRegex = Regex("""-\s*Propagation time:\s*(-?(?:\d+\.?\d*|inf))""")
    private val slackRegex = Regex("""^=\s*Slack(?:\s*\(([^)]+)\))?\s*:\s*(-?(?:\d+\.?\d*|inf))""")
    private val logicLevelsRegex = Regex("""Number of logic level\(s\):\s*(\d+)""")
    private val startingPointRegex = Regex("""Starting point:\s*(.+)$""")
    private val endingPointRegex = Regex("""Ending point:\s*(.+)$""")
    private val startClockRegex = Regex("""The start point is clocked by\s+(\S+)""")
    private val endClockRegex = Regex("""The end\s+point is clocked by\s+(\S+)""")

    private fun String.toDoubleOrNullNa(): Double? = when (trim().lowercase(Locale.ROOT)) {
        "na", "n/a", "" -> null
        "inf" -> Double.POSITIVE_INFINITY
        "-inf" -> Double.NEGATIVE_INFINITY
        else -> trim().removeSuffix("ns").removeSuffix("MHz").removeSuffix("mhz").removeSuffix("NS").toDoubleOrNull()
    }

    /**
     * Parses the contents of an iCEcube2 / Synplify Pro .srr log file.
     */
    fun parse(report: String): TimingReport {
        val lines = report.lines()
        val timingLines = extractTimingReportLines(lines)
        val worstSlack = parseWorstSlack(timingLines)
        val clocks = parsePerformanceSummary(timingLines)
        val endingPoints = parseEndingPoints(timingLines)
        val paths = parsePaths(timingLines)

        val constraintsMet = when {
            worstSlack != null -> worstSlack >= 0.0
            clocks.isNotEmpty() -> clocks.all { (it.slack ?: 0.0) >= 0.0 }
            else -> null
        }

        val summary = parseDesignSummary(worstSlack, endingPoints, paths)
        val utilization = parseUtilization(lines, report)

        return TimingReport(
            constraintsMet = constraintsMet,
            summary = summary,
            clocks = clocks,
            paths = paths,
            endingPoints = endingPoints,
            utilization = utilization
        )
    }

    private fun parseUtilization(lines: List<String>, report: String): DeviceUtilization? {
        val cellUsage = mutableMapOf<String, Int>()
        val cellUsageIndex = lines.indexOfFirst { it.trim().startsWith("Cell usage:") }
        if (cellUsageIndex != -1) {
            for (i in (cellUsageIndex + 1) until lines.size) {
                val line = lines[i]
                val match = Regex("""^\s*(\S+)\s+(\d+)\s+uses?""").matchEntire(line)
                if (match != null) {
                    cellUsage[match.groupValues[1]] = match.groupValues[2].toIntOrNull() ?: 0
                } else if (line.isBlank() || line.trim().startsWith("I/O") || line.trim().startsWith("@") || line.trim()
                        .startsWith("Mapper") || line.trim().startsWith("#")
                ) {
                    break
                }
            }
        }

        val lutMatch = Regex("""Total\s+LUTs:\s*(\d+)(?:\s*\(([\d.]+)%\))?""").find(report)
        val lutUsed = lutMatch?.groupValues?.getOrNull(1)?.toIntOrNull() ?: cellUsage["SB_LUT4"]
        val lutPct = lutMatch?.groupValues?.getOrNull(2)?.toDoubleOrNull()
        val lutUsage = lutUsed?.let { ResourceUsage(used = it, explicitPercentage = lutPct) }

        val regMatch = Regex("""Register bits not including I/Os:\s*(\d+)(?:\s*\(([\d.]+)%\))?""").find(report)
        val regUsed = regMatch?.groupValues?.getOrNull(1)?.toIntOrNull()
            ?: cellUsage.filterKeys { it.startsWith("SB_DFF") }.values.sum().takeIf { it > 0 }
        val regPct = regMatch?.groupValues?.getOrNull(2)?.toDoubleOrNull()
        val regUsage = regUsed?.let { ResourceUsage(used = it, explicitPercentage = regPct) }

        val ioMatch = Regex("""I/O ports:\s*(\d+)""").find(report)
            ?: Regex("""I/O primitives:\s*(\d+)""").find(report)
        val ioUsed = ioMatch?.groupValues?.getOrNull(1)?.toIntOrNull()
            ?: ((cellUsage["SB_IO"] ?: 0) + (cellUsage["SB_GB_IO"] ?: 0)).takeIf { it > 0 }
        val ioUsage = ioUsed?.let { ResourceUsage(used = it) }

        val bramAvailMatch = Regex("""blockrams\s*=\s*(\d+)""").find(report)
        val bramAvail = bramAvailMatch?.groupValues?.getOrNull(1)?.toIntOrNull()
        val bramUsed = cellUsage.filterKeys { it.startsWith("SB_RAM") }.values.sum()
        val bramUsage = if (bramAvail != null || bramUsed > 0 || cellUsage.isNotEmpty()) {
            ResourceUsage(used = bramUsed, available = bramAvail)
        } else null

        val gbUsed = (cellUsage["SB_GB"] ?: 0) + (cellUsage["SB_GB_IO"] ?: 0)
        val gbUsage = if (gbUsed > 0 || cellUsage.containsKey("SB_GB") || cellUsage.containsKey("SB_GB_IO")) {
            ResourceUsage(used = gbUsed)
        } else null

        val carryUsed = cellUsage["SB_CARRY"]
        val carryUsage = carryUsed?.let { ResourceUsage(used = it) }

        val all = mutableMapOf<String, ResourceUsage>()
        lutUsage?.let { all["LUT"] = it; all["SB_LUT4"] = it }
        regUsage?.let { all["DFF"] = it; all["REG"] = it }
        bramUsage?.let { all["BRAM"] = it; all["RAM"] = it }
        ioUsage?.let { all["IO"] = it }
        gbUsage?.let { all["SB_GB"] = it }
        carryUsage?.let { all["SB_CARRY"] = it }
        cellUsage.forEach { (k, v) ->
            if (!all.containsKey(k)) {
                all[k] = ResourceUsage(used = v)
            }
        }

        if (cellUsage.isEmpty() && lutUsage == null && regUsage == null && bramUsage == null && ioUsage == null) {
            return null
        }

        return DeviceUtilization(
            logicCells = lutUsage,
            dffs = regUsage,
            bram = bramUsage,
            globalBuffers = gbUsage,
            ios = ioUsage,
            carry = carryUsage,
            cellUsage = cellUsage,
            all = all
        )
    }

    private fun extractTimingReportLines(lines: List<String>): List<String> {
        val startIndex = lines.indexOfFirst { it.contains("START OF TIMING REPORT") }
        if (startIndex == -1) return lines
        val endIndex = lines.indexOfFirst { it.contains("END OF TIMING REPORT") }
        return if (endIndex > startIndex) {
            lines.subList(startIndex, endIndex)
        } else {
            lines.drop(startIndex)
        }
    }

    private fun parseWorstSlack(lines: List<String>): Double? {
        for (line in lines) {
            worstSlackRegex.find(line)?.let { match ->
                return match.groupValues[1].toDoubleOrNullNa()
            }
        }
        return null
    }

    private fun parsePerformanceSummary(lines: List<String>): List<Clock> {
        val startIndex = lines.indexOfFirst { it.trim() == "Performance Summary" }
        if (startIndex == -1) return emptyList()

        val dividerIndex = lines.drop(startIndex).indexOfFirst { it.trim().startsWith("---") }
        if (dividerIndex == -1) return emptyList()

        val tableStart = startIndex + dividerIndex + 1
        val clocks = mutableListOf<Clock>()

        for (i in tableStart until lines.size) {
            val line = lines[i].trim()
            if (line.startsWith("===") || line.startsWith("***") || line.isEmpty() && clocks.isNotEmpty()) {
                break
            }
            if (line.isBlank() || line.startsWith("---")) continue

            val tokens = line.split(Regex("""\s+"""))
            if (tokens.isEmpty()) continue

            val name = tokens[0]
            val rest = tokens.drop(1)
                .filter { it != "MHz" && it != "ns" && it != "mhz" && it != "NS" }
                .map { it.removeSuffix("MHz").removeSuffix("mhz").removeSuffix("ns").removeSuffix("NS") }

            val reqFreq = rest.getOrNull(0)?.toDoubleOrNullNa()
            val estFreq = rest.getOrNull(1)?.toDoubleOrNullNa()
            val reqPeriod = rest.getOrNull(2)?.toDoubleOrNullNa()
            val estPeriod = rest.getOrNull(3)?.toDoubleOrNullNa()
            val slack = rest.getOrNull(4)?.toDoubleOrNullNa()
            val clockType = rest.getOrNull(5)
            val clockGroup = rest.getOrNull(6)

            val period = reqPeriod ?: (reqFreq?.let { 1000.0 / it } ?: 0.0)
            val frequency = reqFreq ?: (reqPeriod?.let { 1000.0 / it } ?: 0.0)

            clocks.add(
                Clock(
                    name = name,
                    period = period,
                    frequency = frequency,
                    slack = slack,
                    estimatedPeriod = estPeriod,
                    estimatedFrequency = estFreq,
                    clockType = clockType,
                    clockGroup = clockGroup
                )
            )
        }

        return clocks
    }

    private fun parseEndingPoints(lines: List<String>): List<EndingPoint> {
        val startIndex = lines.indexOfFirst { it.trim() == "Ending Points with Worst Slack" }
        if (startIndex == -1) return emptyList()

        val dividerIndex = lines.drop(startIndex).indexOfFirst { it.trim().startsWith("---") }
        if (dividerIndex == -1) return emptyList()

        val tableStart = startIndex + dividerIndex + 1
        val endingPoints = mutableListOf<EndingPoint>()

        for (i in tableStart until lines.size) {
            val line = lines[i].trim()
            if (line.startsWith("===") || line.startsWith("***") || line.isEmpty() && endingPoints.isNotEmpty()) {
                break
            }
            if (line.isBlank() || line.startsWith("---")) continue

            val tokens = line.split(Regex("""\s+"""))
            if (tokens.size < 2) continue

            val instance = tokens[0]
            val startingClock = tokens.getOrNull(1) ?: ""
            val type = tokens.getOrNull(2)
            val pin = tokens.getOrNull(3)
            val net = tokens.getOrNull(4)
            val requiredTime = tokens.getOrNull(tokens.size - 2)?.toDoubleOrNullNa()
            val slack = tokens.last().toDoubleOrNullNa()

            endingPoints.add(
                EndingPoint(
                    instance = instance,
                    referenceClock = startingClock,
                    type = type,
                    pin = pin,
                    net = net,
                    requiredTime = requiredTime,
                    slack = slack
                )
            )
        }

        return endingPoints
    }

    private fun parseDesignSummary(
        worstSlack: Double?,
        endingPoints: List<EndingPoint>,
        paths: List<TimingPath>
    ): DesignTimingSummary? {
        val wns =
            worstSlack ?: endingPoints.mapNotNull { it.slack }.minOrNull() ?: paths.mapNotNull { it.slack }.minOrNull()
        val failingEndpoints = endingPoints.filter { (it.slack ?: 0.0) < 0.0 }
        val setupFailingEndpoints = if (failingEndpoints.isNotEmpty()) {
            failingEndpoints.size
        } else if (wns != null && wns < 0.0) {
            paths.count { it.violated }
        } else {
            0
        }
        val totalEndpoints = endingPoints.size.takeIf { it > 0 } ?: paths.size.takeIf { it > 0 }

        val tns = if (failingEndpoints.isNotEmpty()) {
            val sum = failingEndpoints.mapNotNull { it.slack }.sum()
            Math.round(sum * 1000.0) / 1000.0
        } else if (wns != null && wns < 0.0) {
            val sum = paths.filter { it.violated }.mapNotNull { it.slack }.sum()
            Math.round(sum * 1000.0) / 1000.0
        } else {
            0.0
        }

        if (wns == null && setupFailingEndpoints == 0 && totalEndpoints == null) return null

        return DesignTimingSummary(
            worstNegativeSlack = if (wns != null && wns < 0.0) wns else 0.0,
            totalNegativeSlack = if (tns < 0.0) tns else 0.0,
            setupFailingEndpoints = setupFailingEndpoints,
            setupTotalEndpoints = totalEndpoints
        )
    }

    private fun parsePaths(lines: List<String>): List<TimingPath> {
        val paths = mutableListOf<TimingPath>()

        var inPath = false
        var slack: Double? = null
        var source = ""
        var destination = ""
        var fromClock: String? = null
        var toClock: String? = null
        var requirement: Double? = null
        var propagationTime: Double? = null
        var logicLevels: Int? = null
        var isSetup = false

        fun flush() {
            if (inPath) {
                val violated = (slack ?: 0.0) < 0.0
                val pathType = if (isSetup) "Setup" else null
                paths.add(
                    TimingPath(
                        slack = slack,
                        violated = violated,
                        source = source,
                        destination = destination,
                        fromClock = fromClock,
                        toClock = toClock,
                        pathType = pathType,
                        requirement = requirement,
                        dataPathDelay = propagationTime,
                        logicLevels = logicLevels
                    )
                )
            }
            inPath = false
            slack = null
            source = ""
            destination = ""
            fromClock = null
            toClock = null
            requirement = null
            propagationTime = null
            logicLevels = null
            isSetup = false
        }

        for (line in lines) {
            val trimmed = line.trim()
            if (pathHeaderRegex.find(trimmed) != null) {
                flush()
                inPath = true
                continue
            }

            if (!inPath) continue

            if (trimmed.startsWith("===") && trimmed.length > 20) {
                continue
            }

            slackRegex.find(trimmed)?.let {
                slack = it.groupValues[2].toDoubleOrNullNa()
                return@let
            }
            startingPointRegex.find(trimmed)?.let {
                source = it.groupValues[1].trim()
                return@let
            }
            endingPointRegex.find(trimmed)?.let {
                destination = it.groupValues[1].trim()
                return@let
            }
            startClockRegex.find(trimmed)?.let {
                fromClock = it.groupValues[1].trim()
                return@let
            }
            endClockRegex.find(trimmed)?.let {
                toClock = it.groupValues[1].trim()
                return@let
            }
            logicLevelsRegex.find(trimmed)?.let {
                logicLevels = it.groupValues[1].toIntOrNull()
                return@let
            }
            propagationTimeRegex.find(trimmed)?.let {
                propagationTime = it.groupValues[1].toDoubleOrNullNa()
                return@let
            }
            requestedPeriodRegex.find(trimmed)?.let {
                requirement = it.groupValues[1].toDoubleOrNullNa()
                return@let
            }
            setupTimeRegex.find(trimmed)?.let {
                isSetup = true
                return@let
            }
        }
        flush()
        return paths
    }
}
