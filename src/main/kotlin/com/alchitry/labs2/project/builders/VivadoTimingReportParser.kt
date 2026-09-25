package com.alchitry.labs2.project.builders

/**
 * Parser for Vivado timing summary reports (report_timing_summary output).
 *
 * It extracts the overall pass/fail status, the design wide timing summary (WNS/TNS/WHS/THS/WPWS/TPWS),
 * the clocks present in the design, the per clock-pair setup/hold/pulse width results, and the
 * individual timing paths (including the worst failing paths) so they can be reported to the user.
 */
object VivadoTimingReportParser {
    /**
     * Summary of a single timing check (setup, hold, or pulse width) for a clock pair.
     * Null values correspond to "NA" entries in the report (check not applicable).
     */
    data class CheckSummary(
        val failingEndpoints: Int?,
        val worstSlack: Double?,
        val totalViolation: Double?
    ) {
        /** True when this check has failing endpoints. Null when the check wasn't performed. */
        val failed: Boolean? get() = failingEndpoints?.let { it > 0 }
    }

    /** The design wide timing summary numbers from the "Design Timing Summary" section. */
    data class DesignTimingSummary(
        val worstNegativeSlack: Double?,
        val totalNegativeSlack: Double?,
        val setupFailingEndpoints: Int?,
        val setupTotalEndpoints: Int?,
        val worstHoldSlack: Double?,
        val totalHoldSlack: Double?,
        val holdFailingEndpoints: Int?,
        val holdTotalEndpoints: Int?,
        val worstPulseWidthSlack: Double?,
        val totalPulseWidthSlack: Double?,
        val pulseWidthFailingEndpoints: Int?,
        val pulseWidthTotalEndpoints: Int?
    )

    /** A clock defined in the design from the "Clock Summary" section. */
    data class Clock(
        val name: String,
        val waveform: String,
        val period: Double,
        val frequency: Double,
        val slack: Double? = null,
        val achievedPeriod: Double? = null,
        val achievedFrequency: Double? = null
    ) {
        val maxFrequency: Double? get() = achievedFrequency
        val estimatedFrequency: Double? get() = achievedFrequency
        val maxPeriod: Double? get() = achievedPeriod
        val estimatedPeriod: Double? get() = achievedPeriod
    }

    /** Setup/hold/pulse width results for a from-clock/to-clock pair from the "Timing Details" section. */
    data class ClockPairSummary(
        val fromClock: String,
        val toClock: String,
        val setup: CheckSummary,
        val hold: CheckSummary,
        val pulseWidth: CheckSummary
    ) {
        /** True when every applicable check for this clock pair has no failing endpoints. */
        val passed: Boolean
            get() = listOfNotNull(setup.failed, hold.failed, pulseWidth.failed).none { it }
    }

    /** A single timing path from the detailed path sections of the report. */
    data class TimingPath(
        val slack: Double?,
        val violated: Boolean,
        val source: String,
        val destination: String,
        val fromClock: String?,
        val toClock: String?,
        val pathGroup: String?,
        val pathType: String?,
        val requirement: Double?,
        val dataPathDelay: Double?,
        val logicLevels: Int?
    )

    data class TimingReport(
        /** True if the report says all user specified timing constraints are met, false if it says they
         * are not met, or null if neither statement was found. */
        val constraintsMet: Boolean?,
        val summary: DesignTimingSummary?,
        val clocks: List<Clock>,
        val clockPairs: List<ClockPairSummary>,
        val paths: List<TimingPath>
    ) {
        /** All paths that violated their timing requirement, worst first. */
        val failingPaths: List<TimingPath>
            get() = paths.filter { it.violated }
                .sortedBy { it.slack ?: Double.MAX_VALUE }

        /** The single worst failing path, or null if timing was met. */
        val worstFailingPath: TimingPath? get() = failingPaths.firstOrNull()

        /** Clocks whose intra-clock checks all passed. */
        val passingClocks: List<Clock>
            get() = clocks.filter { clock ->
                clockPairs.filter { it.fromClock == clock.name || it.toClock == clock.name }
                    .all { it.passed }
            }

        /** Clocks that have at least one failing check associated with them. */
        val failingClocks: List<Clock>
            get() = clocks.filter { clock ->
                clockPairs.filter { it.fromClock == clock.name || it.toClock == clock.name }
                    .any { !it.passed }
            }
    }

    private val slackRegex =
        Regex("""^Slack\s*(?:\((VIOLATED|MET)\))?\s*:\s*(-?(?:\d+\.?\d*|inf))(?:ns)?""")
    private val fieldRegex = Regex("""^\s{2}([A-Za-z ]+?):\s+(.*)$""")
    private val clockRowRegex =
        Regex("""^\s*(\S+)\s+\{([^}]*)}\s+(-?\d+\.?\d*)\s+(-?\d+\.?\d*)\s*$""")
    private val fromClockRegex = Regex("""^From Clock:\s*(\S*)\s*$""")
    private val toClockRegex = Regex("""^\s*To Clock:\s*(\S*)\s*$""")
    private val checkLineRegex =
        Regex("""^(Setup|Hold|PW)\s*:\s+(NA|-?\d+)\s+Failing Endpoints,\s+Worst Slack\s+(NA|-?\d+\.?\d*)(?:ns)?\s*,\s+Total Violation\s+(NA|-?\d+\.?\d*)(?:ns)?""")

    private fun String.toDoubleOrNullNa(): Double? = when (trim()) {
        "NA", "n/a", "" -> null
        "inf" -> Double.POSITIVE_INFINITY
        "-inf" -> Double.NEGATIVE_INFINITY
        else -> trim().removeSuffix("ns").toDoubleOrNull()
    }

    private fun String.toIntOrNullNa(): Int? = when (trim()) {
        "NA", "n/a", "" -> null
        else -> trim().toIntOrNull()
    }

    private fun round3(value: Double): Double = kotlin.math.round(value * 1000.0) / 1000.0

    /** Parses the contents of a Vivado timing summary report. */
    fun parse(report: String): TimingReport {
        val lines = report.lines()

        val constraintsMet = when {
            report.contains("All user specified timing constraints are met.") -> true
            report.contains("Timing constraints are not met.") -> false
            else -> null
        }

        val summary = parseDesignSummary(lines)
        val rawClocks = parseClockSummary(lines)
        val clockPairs = parseClockPairs(lines)
        val intraClockWns = parseIntraClockTableWns(lines)
        val paths = parsePaths(lines)

        val clocks = rawClocks.map { clock ->
            val intraPair = clockPairs.firstOrNull { it.fromClock == clock.name && it.toClock == clock.name }
            val pairSlack = intraPair?.setup?.worstSlack
            val intraTableSlack = intraClockWns[clock.name]
            val anyPairSlack = if (pairSlack == null && intraTableSlack == null) {
                clockPairs.filter { it.fromClock == clock.name || it.toClock == clock.name }
                    .mapNotNull { it.setup.worstSlack }
                    .minOrNull()
            } else null

            val slack = pairSlack ?: intraTableSlack ?: anyPairSlack
            val achievedPeriod = if (slack != null) round3(clock.period - slack) else null
            val achievedFrequency =
                if (achievedPeriod != null && achievedPeriod > 0.0) round3(1000.0 / achievedPeriod) else null

            clock.copy(
                slack = slack?.let { round3(it) },
                achievedPeriod = achievedPeriod,
                achievedFrequency = achievedFrequency
            )
        }

        return TimingReport(
            constraintsMet = constraintsMet,
            summary = summary,
            clocks = clocks,
            clockPairs = clockPairs,
            paths = paths
        )
    }

    private fun parseIntraClockTableWns(lines: List<String>): Map<String, Double> {
        val section = sectionLines(lines, "Intra Clock Table")
        val headerIndex = section.indexOfFirst { it.contains("Clock") && it.contains("WNS(ns)") }
        if (headerIndex < 0 || headerIndex + 1 >= section.size) return emptyMap()

        val divider = section[headerIndex + 1]
        val colStarts = mutableListOf<Int>()
        var inCol = false
        for (idx in divider.indices) {
            val c = divider[idx]
            if (c == '-' && !inCol) {
                colStarts.add(idx)
                inCol = true
            } else if (c != '-') {
                inCol = false
            }
        }
        if (colStarts.size < 2) return emptyMap()

        val nameStart = colStarts[0]
        val nameEnd = colStarts[1]
        val wnsStart = colStarts[1]
        val wnsEnd = if (colStarts.size > 2) colStarts[2] else divider.length

        val result = mutableMapOf<String, Double>()
        for (i in (headerIndex + 2) until section.size) {
            val line = section[i]
            if (line.isBlank() || line.trim().startsWith("-") || line.trim().startsWith("|")) continue
            val name = if (line.length > nameStart) {
                val end = if (line.length > nameEnd) nameEnd else line.length
                line.substring(nameStart, end).trim()
            } else ""
            if (name.isEmpty()) continue

            val wnsStr = if (line.length > wnsStart) {
                val end = if (line.length > wnsEnd) wnsEnd else line.length
                line.substring(wnsStart, end).trim()
            } else ""
            wnsStr.toDoubleOrNullNa()?.let {
                result[name] = it
            }
        }
        return result
    }

    private fun sectionLines(lines: List<String>, header: String): List<String> {
        val start = lines.indexOfFirst { it.trim() == "| $header" }
        if (start < 0) return emptyList()
        val end = lines.drop(start + 1).indexOfFirst { it.trim().startsWith("| ") && !it.trim().startsWith("| -") }
        return if (end < 0) lines.drop(start + 1) else lines.subList(start + 1, start + 1 + end)
    }

    private fun parseDesignSummary(lines: List<String>): DesignTimingSummary? {
        val section = sectionLines(lines, "Design Timing Summary")
        val dividerIndex = section.indexOfFirst { it.trim().startsWith("-------") && it.contains("  ") }
        if (dividerIndex < 0) return null
        val dataLine = section.drop(dividerIndex + 1).firstOrNull { it.isNotBlank() } ?: return null
        val tokens = dataLine.trim().split(Regex("""\s+"""))
        if (tokens.size < 12) return null
        return DesignTimingSummary(
            worstNegativeSlack = tokens[0].toDoubleOrNullNa(),
            totalNegativeSlack = tokens[1].toDoubleOrNullNa(),
            setupFailingEndpoints = tokens[2].toIntOrNullNa(),
            setupTotalEndpoints = tokens[3].toIntOrNullNa(),
            worstHoldSlack = tokens[4].toDoubleOrNullNa(),
            totalHoldSlack = tokens[5].toDoubleOrNullNa(),
            holdFailingEndpoints = tokens[6].toIntOrNullNa(),
            holdTotalEndpoints = tokens[7].toIntOrNullNa(),
            worstPulseWidthSlack = tokens[8].toDoubleOrNullNa(),
            totalPulseWidthSlack = tokens[9].toDoubleOrNullNa(),
            pulseWidthFailingEndpoints = tokens[10].toIntOrNullNa(),
            pulseWidthTotalEndpoints = tokens[11].toIntOrNullNa()
        )
    }

    private fun parseClockSummary(lines: List<String>): List<Clock> {
        return sectionLines(lines, "Clock Summary").mapNotNull { line ->
            clockRowRegex.matchEntire(line)?.let { match ->
                Clock(
                    name = match.groupValues[1],
                    waveform = match.groupValues[2].trim(),
                    period = match.groupValues[3].toDouble(),
                    frequency = match.groupValues[4].toDouble()
                )
            }
        }
    }

    private fun parseClockPairs(lines: List<String>): List<ClockPairSummary> {
        val pairs = mutableListOf<ClockPairSummary>()
        var fromClock: String? = null
        var toClock: String? = null
        var setup: CheckSummary? = null
        var hold: CheckSummary? = null
        var pw: CheckSummary? = null

        fun flush() {
            val from = fromClock
            val to = toClock
            if (from != null && to != null && (setup != null || hold != null || pw != null)) {
                val empty = CheckSummary(null, null, null)
                pairs.add(ClockPairSummary(from, to, setup ?: empty, hold ?: empty, pw ?: empty))
            }
            fromClock = null
            toClock = null
            setup = null
            hold = null
            pw = null
        }

        for (line in lines) {
            fromClockRegex.matchEntire(line)?.let {
                flush()
                fromClock = it.groupValues[1]
                return@let
            } ?: toClockRegex.matchEntire(line)?.let {
                if (fromClock != null) toClock = it.groupValues[1]
            } ?: checkLineRegex.find(line.trim())?.let { match ->
                val summary = CheckSummary(
                    failingEndpoints = match.groupValues[2].toIntOrNullNa(),
                    worstSlack = match.groupValues[3].toDoubleOrNullNa(),
                    totalViolation = match.groupValues[4].toDoubleOrNullNa()
                )
                when (match.groupValues[1]) {
                    "Setup" -> setup = summary
                    "Hold" -> hold = summary
                    "PW" -> pw = summary
                }
            }
        }
        flush()
        return pairs
    }

    private fun parsePaths(lines: List<String>): List<TimingPath> {
        val paths = mutableListOf<TimingPath>()
        var fromClock: String? = null
        var toClock: String? = null

        var index = 0
        while (index < lines.size) {
            val line = lines[index]

            fromClockRegex.matchEntire(line)?.let { fromClock = it.groupValues[1].ifEmpty { null } }
            toClockRegex.matchEntire(line)?.let { toClock = it.groupValues[1].ifEmpty { null } }

            val slackMatch = slackRegex.find(line.trim())
            if (slackMatch != null && line.startsWith("Slack")) {
                val violated = slackMatch.groupValues[1] == "VIOLATED"
                val slack = slackMatch.groupValues[2].toDoubleOrNullNa()

                val fields = mutableMapOf<String, String>()
                var cursor = index + 1
                while (cursor < lines.size) {
                    val fieldLine = lines[cursor]
                    if (fieldLine.startsWith("Slack") || fieldLine.isBlank() && cursor + 1 < lines.size &&
                        lines[cursor + 1].trim().startsWith("Location")
                    ) break
                    val match = fieldRegex.matchEntire(fieldLine)
                    if (match != null) {
                        fields.putIfAbsent(match.groupValues[1].trim(), match.groupValues[2].trim())
                    } else if (!fieldLine.startsWith("    ") && fieldLine.isNotBlank()) {
                        break
                    }
                    cursor++
                }

                paths.add(
                    TimingPath(
                        slack = slack,
                        violated = violated,
                        source = fields["Source"] ?: "",
                        destination = fields["Destination"] ?: "",
                        fromClock = fromClock,
                        toClock = toClock,
                        pathGroup = fields["Path Group"],
                        pathType = fields["Path Type"],
                        requirement = fields["Requirement"]?.substringBefore("(")?.trim()?.toDoubleOrNullNa(),
                        dataPathDelay = fields["Data Path Delay"]?.substringBefore("(")?.trim()?.toDoubleOrNullNa(),
                        logicLevels = fields["Logic Levels"]?.substringBefore("(")?.trim()?.toIntOrNull()
                    )
                )
                index = cursor
                continue
            }
            index++
        }
        return paths
    }
}
