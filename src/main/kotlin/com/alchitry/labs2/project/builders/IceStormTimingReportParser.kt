package com.alchitry.labs2.project.builders

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.roundToLong

/**
 * Parser for nextpnr timing reports (JSON format found in alchitry.rpt).
 *
 * It extracts the overall pass/fail status, performance summary (fmax per clock),
 * critical paths with delay breakdowns, and design timing summary.
 */
object IceStormTimingReportParser {

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

    /** A clock defined in the design from the fmax section. */
    data class Clock(
        val name: String,
        val period: Double,
        val frequency: Double,
        val slack: Double?,
        val achievedPeriod: Double? = null,
        val achievedFrequency: Double? = null
    )

    /** A single timing path from the critical_paths section of the report. */
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
        val warmboot: ResourceUsage? = null,
        val all: Map<String, ResourceUsage> = emptyMap()
    ) {
        val lc: ResourceUsage? get() = logicCells
        val slice: ResourceUsage? get() = logicCells
        val luts: ResourceUsage? get() = logicCells
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

    @Serializable
    private data class NextPnrReportJson(
        val critical_paths: List<CriticalPathJson>? = null,
        val fmax: Map<String, FmaxInfoJson>? = null,
        val utilization: Map<String, ResourceUsageJson>? = null
    )

    @Serializable
    private data class ResourceUsageJson(
        val available: Int? = null,
        val used: Int? = null
    )

    @Serializable
    private data class FmaxInfoJson(
        val achieved: Double? = null,
        val constraint: Double? = null
    )

    @Serializable
    private data class CriticalPathJson(
        val from: String? = null,
        val to: String? = null,
        val path: List<PathStepJson>? = null
    )

    @Serializable
    private data class PathStepJson(
        val delay: Double? = null,
        val type: String? = null,
        val net: String? = null,
        val from: PathEndpointJson? = null,
        val to: PathEndpointJson? = null
    )

    @Serializable
    private data class PathEndpointJson(
        val cell: String? = null,
        val loc: List<Int>? = null,
        val port: String? = null
    )

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun round3(value: Double): Double = (value * 1000.0).roundToLong() / 1000.0

    private fun cleanClockName(raw: String?): String? {
        if (raw == null || raw == "<async>" || raw.isBlank()) return null
        return raw.removePrefix("posedge ").removePrefix("negedge ").trim()
    }

    /**
     * Parses the contents of a nextpnr alchitry.rpt JSON report file.
     */
    fun parse(report: String): TimingReport {
        val parsed = try {
            jsonParser.decodeFromString<NextPnrReportJson>(report)
        } catch (_: Exception) {
            return TimingReport(
                constraintsMet = null,
                summary = null,
                clocks = emptyList(),
                paths = emptyList()
            )
        }

        val fmaxMap = parsed.fmax ?: emptyMap()

        val clocks = fmaxMap.map { (clockName, fmaxInfo) ->
            val achieved = fmaxInfo.achieved
            val constraint = fmaxInfo.constraint
            val reqFreq = constraint ?: (achieved ?: 0.0)
            val reqPeriod = if (reqFreq > 0.0) round3(1000.0 / reqFreq) else 0.0
            val estPeriod = if (achieved != null && achieved > 0.0) round3(1000.0 / achieved) else null
            val slack = if (constraint != null && constraint > 0.0 && estPeriod != null) {
                round3(reqPeriod - estPeriod)
            } else null

            Clock(
                name = clockName,
                period = reqPeriod,
                frequency = reqFreq,
                slack = slack,
                achievedPeriod = estPeriod,
                achievedFrequency = achieved?.let { round3(it) }
            )
        }

        val paths = (parsed.critical_paths ?: emptyList()).map { cp ->
            val steps = cp.path ?: emptyList()
            val totalDelay = round3(steps.sumOf { it.delay ?: 0.0 })
            val logicLevels = steps.count { it.type == "logic" }
            val first = steps.firstOrNull()?.from
            val last = steps.lastOrNull()?.to
            val source = when {
                first?.cell != null && first.port != null -> "${first.cell}/${first.port}"
                first?.cell != null -> first.cell
                else -> ""
            }
            val destination = when {
                last?.cell != null && last.port != null -> "${last.cell}/${last.port}"
                last?.cell != null -> last.cell
                else -> ""
            }
            val fromClock = cleanClockName(cp.from)
            val toClock = cleanClockName(cp.to)
            val matchedClock = toClock?.let { fmaxMap[it] } ?: fromClock?.let { fmaxMap[it] }
            val reqFreq = matchedClock?.constraint
            val requirement = if (reqFreq != null && reqFreq > 0.0) round3(1000.0 / reqFreq) else null
            val slack = if (requirement != null) round3(requirement - totalDelay) else null
            val violated = slack != null && slack < 0.0
            val pathType =
                if (steps.any { it.type == "setup" } || (fromClock != null && toClock != null)) "Setup" else null

            TimingPath(
                slack = slack,
                violated = violated,
                source = source,
                destination = destination,
                fromClock = fromClock,
                toClock = toClock,
                pathGroup = toClock ?: fromClock,
                pathType = pathType,
                requirement = requirement,
                dataPathDelay = totalDelay,
                logicLevels = logicLevels
            )
        }

        val constraintsMet = when {
            clocks.isEmpty() && paths.none { it.violated } -> true
            clocks.isNotEmpty() -> clocks.all { (it.slack ?: 0.0) >= 0.0 } && paths.none { it.violated }
            else -> true
        }

        val failingPaths = paths.filter { it.violated }
        val worstPathSlack = failingPaths.minOfOrNull { it.slack ?: 0.0 }
        val clockWorstSlack = clocks.mapNotNull { it.slack }.filter { it < 0.0 }.minOrNull()
        val wns = worstPathSlack ?: clockWorstSlack ?: 0.0
        val tns = failingPaths.mapNotNull { it.slack }.sum().let { round3(it) }.takeIf { it < 0.0 } ?: 0.0
        val setupFailingEndpoints = if (failingPaths.isNotEmpty()) failingPaths.size else if (wns < 0.0) 1 else 0
        val totalEndpoints = paths.size.takeIf { it > 0 }

        val summary = DesignTimingSummary(
            worstNegativeSlack = if (wns < 0.0) wns else 0.0,
            totalNegativeSlack = tns,
            setupFailingEndpoints = setupFailingEndpoints,
            setupTotalEndpoints = totalEndpoints
        )

        val utilizationJson = parsed.utilization ?: emptyMap()
        val utilizationMap = utilizationJson.mapValues { (_, v) ->
            ResourceUsage(
                used = v.used ?: 0,
                available = v.available
            )
        }
        val utilization = if (utilizationMap.isNotEmpty()) {
            val lc = utilizationMap["ICESTORM_LC"] ?: utilizationMap["LC"] ?: utilizationMap["SLICE"]
            val bram = utilizationMap["ICESTORM_RAM"] ?: utilizationMap["RAM"] ?: utilizationMap["BRAM"]
            val plls = utilizationMap["ICESTORM_PLL"] ?: utilizationMap["PLL"]
            val gb = utilizationMap["SB_GB"] ?: utilizationMap["GB"]
            val io = utilizationMap["SB_IO"] ?: utilizationMap["IO"]
            val warmboot = utilizationMap["SB_WARMBOOT"] ?: utilizationMap["WARMBOOT"]
            DeviceUtilization(
                logicCells = lc,
                bram = bram,
                plls = plls,
                globalBuffers = gb,
                ios = io,
                warmboot = warmboot,
                all = utilizationMap
            )
        } else null

        return TimingReport(
            constraintsMet = constraintsMet,
            summary = summary,
            clocks = clocks,
            paths = paths,
            utilization = utilization
        )
    }
}
