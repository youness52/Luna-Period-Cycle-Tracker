package com.example.domain

import com.example.data.model.CycleHistoryItem
import com.example.data.model.CycleInsightSummary
import com.example.data.model.CyclePhase
import com.example.data.model.DayLog
import com.example.data.model.PregnancyChance
import com.example.data.model.UserSettings
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
import kotlin.math.sqrt

object CycleEngine {

    /**
     * Groups period days into contiguous period episodes.
     * Returns a list of Pair(startDate, endDate) for each period episode, sorted chronologically.
     */
    fun extractPeriodEpisodes(periodDays: List<LocalDate>): List<Pair<LocalDate, LocalDate>> {
        if (periodDays.isEmpty()) return emptyList()
        val sorted = periodDays.distinct().sorted()

        val episodes = mutableListOf<Pair<LocalDate, LocalDate>>()
        var episodeStart = sorted.first()
        var episodeEnd = episodeStart

        for (i in 1 until sorted.size) {
            val current = sorted[i]
            val gap = ChronoUnit.DAYS.between(episodeEnd, current)
            if (gap <= 2) { // Allow up to 1-day spotting gap within same period
                episodeEnd = current
            } else {
                episodes.add(Pair(episodeStart, episodeEnd))
                episodeStart = current
                episodeEnd = current
            }
        }
        episodes.add(Pair(episodeStart, episodeEnd))
        return episodes
    }

    /**
     * Computes historical completed cycles from period episodes.
     */
    fun computeCycleHistory(
        episodes: List<Pair<LocalDate, LocalDate>>,
        today: LocalDate = LocalDate.now()
    ): List<CycleHistoryItem> {
        if (episodes.isEmpty()) return emptyList()

        val history = mutableListOf<CycleHistoryItem>()
        for (i in 0 until episodes.size) {
            val currentEpisode = episodes[i]
            val startDate = currentEpisode.first
            val periodLength = (ChronoUnit.DAYS.between(startDate, currentEpisode.second) + 1).toInt()

            if (i < episodes.size - 1) {
                val nextStart = episodes[i + 1].first
                val cycleLength = ChronoUnit.DAYS.between(startDate, nextStart).toInt()
                history.add(
                    CycleHistoryItem(
                        cycleNumber = i + 1,
                        startDate = startDate,
                        endDate = nextStart.minusDays(1),
                        cycleLengthDays = cycleLength,
                        periodLengthDays = periodLength,
                        isCurrent = false
                    )
                )
            } else {
                // Current cycle
                val daysSoFar = ChronoUnit.DAYS.between(startDate, today).toInt() + 1
                history.add(
                    CycleHistoryItem(
                        cycleNumber = i + 1,
                        startDate = startDate,
                        endDate = null,
                        cycleLengthDays = daysSoFar,
                        periodLengthDays = periodLength,
                        isCurrent = true
                    )
                )
            }
        }
        return history.reversed() // Most recent first
    }

    /**
     * Calculates the average cycle length and period length from historical completed cycles,
     * falling back to user settings if not enough data.
     */
    fun computeAverages(
        history: List<CycleHistoryItem>,
        settings: UserSettings
    ): Pair<Int, Int> {
        val completed = history.filter { !it.isCurrent && it.cycleLengthDays in 18..55 }
        val avgCycle = if (completed.isNotEmpty()) {
            completed.map { it.cycleLengthDays }.average().roundToInt()
        } else {
            settings.avgCycleLength
        }

        val avgPeriod = if (history.isNotEmpty()) {
            history.map { it.periodLengthDays }.filter { it in 1..14 }.average().roundToInt()
        } else {
            settings.avgPeriodLength
        }

        return Pair(avgCycle.coerceIn(21, 45), avgPeriod.coerceIn(2, 10))
    }

    /**
     * Calculates cycle regularity score (0-100%).
     */
    fun computeRegularityScore(history: List<CycleHistoryItem>): Int {
        val completed = history.filter { !it.isCurrent && it.cycleLengthDays in 18..55 }
        if (completed.size < 2) return 85 // Default healthy score for new users

        val mean = completed.map { it.cycleLengthDays }.average()
        val variance = completed.map { (it.cycleLengthDays - mean) * (it.cycleLengthDays - mean) }.average()
        val stdDev = sqrt(variance)

        return when {
            stdDev <= 1.5 -> 98
            stdDev <= 2.5 -> 92
            stdDev <= 3.5 -> 85
            stdDev <= 5.0 -> 74
            stdDev <= 7.0 -> 60
            else -> 48
        }
    }

    /**
     * Generates a comprehensive summary for today and upcoming predictions.
     */
    fun computeInsights(
        allLogs: List<DayLog>,
        settings: UserSettings,
        today: LocalDate = LocalDate.now()
    ): CycleInsightSummary {
        val periodDays = allLogs.filter { it.isPeriod }.map { it.date }.sorted()
        val episodes = extractPeriodEpisodes(periodDays)
        val history = computeCycleHistory(episodes, today)
        val (avgCycle, avgPeriod) = computeAverages(history, settings)
        val regularity = computeRegularityScore(history)

        val lastEpisode = episodes.lastOrNull()

        // Base reference date for current cycle
        val cycleStartDate: LocalDate
        val isPeriodActiveToday: Boolean

        if (lastEpisode != null) {
            val (lastStart, lastEnd) = lastEpisode
            val daysSinceStart = ChronoUnit.DAYS.between(lastStart, today).toInt()
            
            // Check if current day is within active period or recent period
            val todayLog = allLogs.find { it.date == today }
            isPeriodActiveToday = todayLog?.isPeriod == true || 
                (!today.isBefore(lastStart) && !today.isAfter(lastEnd))

            cycleStartDate = if (daysSinceStart >= avgCycle + 14 && !isPeriodActiveToday) {
                // The user hasn't logged period in a while, simulate projected cycle anchor
                val cyclesElapsed = daysSinceStart / avgCycle
                lastStart.plusDays((cyclesElapsed * avgCycle).toLong())
            } else {
                lastStart
            }
        } else {
            // No periods logged yet: default to cycle day 1 being today minus (avgCycle/2)
            cycleStartDate = today.minusDays((avgCycle / 2).toLong())
            isPeriodActiveToday = false
        }

        val daysIntoCycle = ChronoUnit.DAYS.between(cycleStartDate, today).toInt()
        val cycleDay = (daysIntoCycle + 1).coerceAtLeast(1)

        val nextPeriodDate = cycleStartDate.plusDays(avgCycle.toLong())
        val daysUntilPeriod = ChronoUnit.DAYS.between(today, nextPeriodDate).toInt()

        // Ovulation is calculated based on luteal phase duration
        val nextOvulationDate = nextPeriodDate.minusDays(settings.lutealPhaseLength.toLong())
        val fertileStart = nextOvulationDate.minusDays(5)
        val fertileEnd = nextOvulationDate.plusDays(1)

        // Phase determination
        val phase: CyclePhase = when {
            isPeriodActiveToday || cycleDay <= avgPeriod -> CyclePhase.MENSTRUAL
            today.isBefore(fertileStart) -> CyclePhase.FOLLICULAR
            !today.isBefore(fertileStart) && !today.isAfter(fertileEnd) -> CyclePhase.OVULATION
            else -> CyclePhase.LUTEAL
        }

        // Pregnancy chance determination
        val pregnancyChance = when {
            today == nextOvulationDate -> PregnancyChance.PEAK
            today == nextOvulationDate.minusDays(1) || today == nextOvulationDate.minusDays(2) -> PregnancyChance.HIGH
            !today.isBefore(fertileStart) && !today.isAfter(fertileEnd) -> PregnancyChance.MEDIUM
            else -> PregnancyChance.LOW
        }

        return CycleInsightSummary(
            currentPhase = phase,
            cycleDay = cycleDay,
            totalCycleDays = avgCycle,
            daysUntilPeriod = daysUntilPeriod,
            nextPeriodDate = nextPeriodDate,
            nextOvulationDate = nextOvulationDate,
            fertileWindowStart = fertileStart,
            fertileWindowEnd = fertileEnd,
            pregnancyChance = pregnancyChance,
            isPeriodActiveToday = isPeriodActiveToday,
            avgCycleLength = avgCycle,
            avgPeriodLength = avgPeriod,
            cycleRegularityScore = regularity
        )
    }

    /**
     * For calendar rendering: determines predictions for a given date.
     */
    data class DateStatus(
        val isPeriodLogged: Boolean = false,
        val flow: com.example.data.model.FlowIntensity = com.example.data.model.FlowIntensity.NONE,
        val isPredictedPeriod: Boolean = false,
        val isFertileWindow: Boolean = false,
        val isOvulationPeak: Boolean = false,
        val hasSymptoms: Boolean = false,
        val hasMoods: Boolean = false,
        val hasNotes: Boolean = false
    )

    fun getDateStatusMap(
        allLogs: List<DayLog>,
        settings: UserSettings,
        startMonth: LocalDate,
        endMonth: LocalDate
    ): Map<LocalDate, DateStatus> {
        val map = mutableMapOf<LocalDate, DateStatus>()
        val logByDate = allLogs.associateBy { it.date }

        val periodDays = allLogs.filter { it.isPeriod }.map { it.date }.sorted()
        val episodes = extractPeriodEpisodes(periodDays)
        val history = computeCycleHistory(episodes, LocalDate.now())
        val (avgCycle, avgPeriod) = computeAverages(history, settings)

        // Predict upcoming 6 cycles from the last known period start or today
        val lastEpisode = episodes.lastOrNull()
        val baseStart = lastEpisode?.first ?: LocalDate.now()

        val predictedPeriodDays = mutableSetOf<LocalDate>()
        val fertileDays = mutableSetOf<LocalDate>()
        val ovulationDays = mutableSetOf<LocalDate>()

        // Project next 6 cycles into future
        for (i in 0..6) {
            val cycleStart = baseStart.plusDays((i * avgCycle).toLong())
            for (p in 0 until avgPeriod) {
                predictedPeriodDays.add(cycleStart.plusDays(p.toLong()))
            }
            val ovDate = cycleStart.plusDays((avgCycle - settings.lutealPhaseLength).toLong())
            ovulationDays.add(ovDate)
            for (f in -5..1) {
                fertileDays.add(ovDate.plusDays(f.toLong()))
            }
        }

        var cur = startMonth
        while (!cur.isAfter(endMonth)) {
            val log = logByDate[cur]
            val isLogged = log?.isPeriod == true
            val isPred = !isLogged && cur.isAfter(LocalDate.now().minusDays(1)) && predictedPeriodDays.contains(cur)
            val isOv = ovulationDays.contains(cur)
            val isFertile = fertileDays.contains(cur)

            map[cur] = DateStatus(
                isPeriodLogged = isLogged,
                flow = log?.flowIntensity ?: com.example.data.model.FlowIntensity.NONE,
                isPredictedPeriod = isPred,
                isFertileWindow = isFertile,
                isOvulationPeak = isOv,
                hasSymptoms = (log?.symptoms?.isNotEmpty() == true) || (log?.crampLevel != com.example.data.model.CrampLevel.NONE),
                hasMoods = log?.moods?.isNotEmpty() == true,
                hasNotes = !log?.notes.isNullOrBlank()
            )
            cur = cur.plusDays(1)
        }

        return map
    }
}
