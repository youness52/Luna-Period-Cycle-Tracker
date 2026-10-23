package com.example.data.repository

import com.example.data.local.DayLogDao
import com.example.data.local.DayLogEntity
import com.example.data.local.UserSettingsDao
import com.example.data.local.UserSettingsEntity
import com.example.data.model.CervicalMucus
import com.example.data.model.CrampLevel
import com.example.data.model.DayLog
import com.example.data.model.FlowIntensity
import com.example.data.model.IntimacyType
import com.example.data.model.MoodType
import com.example.data.model.SymptomType
import com.example.data.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class CycleRepository(
    private val dayLogDao: DayLogDao,
    private val userSettingsDao: UserSettingsDao
) {
    fun getAllLogs(): Flow<List<DayLog>> {
        return dayLogDao.getAllLogs().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getLogForDate(date: LocalDate): Flow<DayLog?> {
        return dayLogDao.getLogForDate(date.toString()).map { it?.toDomain() }
    }

    suspend fun saveDayLog(log: DayLog) {
        dayLogDao.insertOrUpdate(DayLogEntity.fromDomain(log))
    }

    suspend fun deleteDayLog(date: LocalDate) {
        dayLogDao.deleteLog(date.toString())
    }

    fun getSettings(): Flow<UserSettings> {
        return userSettingsDao.getSettings().map {
            it?.toDomain() ?: UserSettings()
        }
    }

    suspend fun saveSettings(settings: UserSettings) {
        userSettingsDao.saveSettings(UserSettingsEntity.fromDomain(settings))
    }

    suspend fun clearAllData() {
        dayLogDao.clearAllLogs()
    }

    /**
     * Seeds 3 past cycles so the user immediately gets intuitive insights,
     * charts, and predictions without having to backfill months manually.
     */
    suspend fun seedSampleCycles() {
        val today = LocalDate.now()
        val sampleLogs = mutableListOf<DayLogEntity>()

        // Cycle 1: 56 days ago (started 56 days ago, lasted 5 days)
        val cycle1Start = today.minusDays(56)
        val flow1 = listOf(FlowIntensity.HEAVY, FlowIntensity.HEAVY, FlowIntensity.MEDIUM, FlowIntensity.LIGHT, FlowIntensity.SPOTTING)
        flow1.forEachIndexed { idx, flow ->
            val date = cycle1Start.plusDays(idx.toLong())
            sampleLogs.add(
                DayLogEntity.fromDomain(
                    DayLog(
                        date = date,
                        isPeriod = true,
                        flowIntensity = flow,
                        crampLevel = if (idx < 2) CrampLevel.MODERATE else CrampLevel.MILD,
                        symptoms = if (idx == 0) setOf(SymptomType.HEADACHE, SymptomType.FATIGUE) else setOf(SymptomType.BLOATING),
                        moods = setOf(MoodType.SENSITIVE, MoodType.TIRED),
                        waterGlasses = 6
                    )
                )
            )
        }

        // Cycle 2: 28 days ago (started 28 days ago, lasted 5 days)
        val cycle2Start = today.minusDays(28)
        val flow2 = listOf(FlowIntensity.HEAVY, FlowIntensity.HEAVY, FlowIntensity.MEDIUM, FlowIntensity.LIGHT, FlowIntensity.LIGHT)
        flow2.forEachIndexed { idx, flow ->
            val date = cycle2Start.plusDays(idx.toLong())
            sampleLogs.add(
                DayLogEntity.fromDomain(
                    DayLog(
                        date = date,
                        isPeriod = true,
                        flowIntensity = flow,
                        crampLevel = if (idx == 0) CrampLevel.SEVERE else CrampLevel.MILD,
                        symptoms = setOf(SymptomType.BLOATING, SymptomType.BACKACHE),
                        moods = setOf(MoodType.IRRITABLE),
                        waterGlasses = 8
                    )
                )
            )
        }

        // Fertile window in cycle 2 (around day 14)
        val ov2 = cycle2Start.plusDays(14)
        sampleLogs.add(
            DayLogEntity.fromDomain(
                DayLog(
                    date = ov2,
                    isPeriod = false,
                    symptoms = setOf(SymptomType.BREAST_TENDERNESS),
                    moods = setOf(MoodType.ENERGETIC, MoodType.HAPPY),
                    cervicalMucus = CervicalMucus.EGG_WHITE,
                    intimacy = IntimacyType.PROTECTED,
                    waterGlasses = 7
                )
            )
        )

        // Cycle 3: Started 2 days ago or current cycle
        val cycle3Start = today.minusDays(2)
        val flow3 = listOf(FlowIntensity.HEAVY, FlowIntensity.MEDIUM, FlowIntensity.LIGHT)
        flow3.forEachIndexed { idx, flow ->
            val date = cycle3Start.plusDays(idx.toLong())
            sampleLogs.add(
                DayLogEntity.fromDomain(
                    DayLog(
                        date = date,
                        isPeriod = true,
                        flowIntensity = flow,
                        crampLevel = if (idx == 0) CrampLevel.MODERATE else CrampLevel.MILD,
                        symptoms = setOf(SymptomType.FATIGUE),
                        moods = setOf(MoodType.CALM),
                        waterGlasses = 7
                    )
                )
            )
        }

        dayLogDao.insertAll(sampleLogs)
    }
}
