package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CervicalMucus
import com.example.data.model.CrampLevel
import com.example.data.model.DayLog
import com.example.data.model.FlowIntensity
import com.example.data.model.IntimacyType
import com.example.data.model.MoodType
import com.example.data.model.SymptomType
import java.time.LocalDate

@Entity(tableName = "day_logs")
data class DayLogEntity(
    @PrimaryKey
    val dateString: String, // format yyyy-MM-dd
    val isPeriod: Boolean,
    val flowIntensity: String,
    val crampLevel: String,
    val symptoms: String, // comma-separated enum names
    val moods: String, // comma-separated enum names
    val cervicalMucus: String,
    val intimacy: String,
    val bbt: Double?,
    val waterGlasses: Int,
    val sleepHours: Float,
    val notes: String,
    val updatedAt: Long
) {
    fun toDomain(): DayLog {
        val parsedDate = try {
            LocalDate.parse(dateString)
        } catch (_: Exception) {
            LocalDate.now()
        }

        val parsedSymptoms = if (symptoms.isBlank()) {
            emptySet()
        } else {
            symptoms.split(",")
                .mapNotNull { name ->
                    try { SymptomType.valueOf(name.trim()) } catch (_: Exception) { null }
                }.toSet()
        }

        val parsedMoods = if (moods.isBlank()) {
            emptySet()
        } else {
            moods.split(",")
                .mapNotNull { name ->
                    try { MoodType.valueOf(name.trim()) } catch (_: Exception) { null }
                }.toSet()
        }

        val parsedFlow = try {
            FlowIntensity.valueOf(flowIntensity)
        } catch (_: Exception) {
            if (isPeriod) FlowIntensity.MEDIUM else FlowIntensity.NONE
        }

        val parsedCramp = try {
            CrampLevel.valueOf(crampLevel)
        } catch (_: Exception) {
            CrampLevel.NONE
        }

        val parsedMucus = try {
            CervicalMucus.valueOf(cervicalMucus)
        } catch (_: Exception) {
            CervicalMucus.NONE
        }

        val parsedIntimacy = try {
            IntimacyType.valueOf(intimacy)
        } catch (_: Exception) {
            IntimacyType.NONE
        }

        return DayLog(
            date = parsedDate,
            isPeriod = isPeriod,
            flowIntensity = parsedFlow,
            crampLevel = parsedCramp,
            symptoms = parsedSymptoms,
            moods = parsedMoods,
            cervicalMucus = parsedMucus,
            intimacy = parsedIntimacy,
            bbt = bbt,
            waterGlasses = waterGlasses,
            sleepHours = sleepHours,
            notes = notes,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromDomain(log: DayLog): DayLogEntity {
            return DayLogEntity(
                dateString = log.date.toString(),
                isPeriod = log.isPeriod,
                flowIntensity = log.flowIntensity.name,
                crampLevel = log.crampLevel.name,
                symptoms = log.symptoms.joinToString(",") { it.name },
                moods = log.moods.joinToString(",") { it.name },
                cervicalMucus = log.cervicalMucus.name,
                intimacy = log.intimacy.name,
                bbt = log.bbt,
                waterGlasses = log.waterGlasses,
                sleepHours = log.sleepHours,
                notes = log.notes,
                updatedAt = log.updatedAt
            )
        }
    }
}
