package com.example

import com.example.data.model.CyclePhase
import com.example.data.model.DayLog
import com.example.data.model.FlowIntensity
import com.example.data.model.UserSettings
import com.example.domain.CycleEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class CycleEngineTest {

    @Test
    fun testExtractPeriodEpisodes() {
        val days = listOf(
            LocalDate.of(2026, 8, 1),
            LocalDate.of(2026, 8, 2),
            LocalDate.of(2026, 8, 3),
            LocalDate.of(2026, 8, 4),
            // next period
            LocalDate.of(2026, 8, 29),
            LocalDate.of(2026, 8, 30),
            LocalDate.of(2026, 8, 31)
        )

        val episodes = CycleEngine.extractPeriodEpisodes(days)
        assertEquals(2, episodes.size)
        assertEquals(LocalDate.of(2026, 8, 1), episodes[0].first)
        assertEquals(LocalDate.of(2026, 8, 4), episodes[0].second)
        assertEquals(LocalDate.of(2026, 8, 29), episodes[1].first)
        assertEquals(LocalDate.of(2026, 8, 31), episodes[1].second)
    }

    @Test
    fun testComputeCycleHistory() {
        val episodes = listOf(
            Pair(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5)),
            Pair(LocalDate.of(2026, 8, 29), LocalDate.of(2026, 9, 2))
        )

        val history = CycleEngine.computeCycleHistory(episodes, LocalDate.of(2026, 9, 10))
        assertEquals(2, history.size)
        // First item in reversed list is the current cycle
        val current = history[0]
        assertTrue(current.isCurrent)
        assertEquals(LocalDate.of(2026, 8, 29), current.startDate)

        // Previous cycle
        val prev = history[1]
        assertEquals(28, prev.cycleLengthDays)
        assertEquals(5, prev.periodLengthDays)
    }

    @Test
    fun testComputeInsights() {
        val logs = listOf(
            DayLog(date = LocalDate.now(), isPeriod = true, flowIntensity = FlowIntensity.HEAVY)
        )
        val settings = UserSettings(avgCycleLength = 28, avgPeriodLength = 5)
        val insights = CycleEngine.computeInsights(logs, settings, LocalDate.now())

        assertNotNull(insights)
        assertEquals(CyclePhase.MENSTRUAL, insights.currentPhase)
        assertTrue(insights.isPeriodActiveToday)
    }

    @Test
    fun testArabicLocalization() {
        assertEquals("مرحلة الحيض", CyclePhase.MENSTRUAL.localizedTitle(true))
        assertEquals("غزير", FlowIntensity.HEAVY.localized(true))
        assertEquals("صداع", com.example.data.model.SymptomType.HEADACHE.localized(true))
        assertEquals("سعيدة", com.example.data.model.MoodType.HAPPY.localized(true))
    }
}
