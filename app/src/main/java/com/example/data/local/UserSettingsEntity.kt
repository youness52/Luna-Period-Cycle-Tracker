package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.UserSettings

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val avgCycleLength: Int = 28,
    val avgPeriodLength: Int = 5,
    val lutealPhaseLength: Int = 14,
    val temperatureUnit: String = "C",
    val isDiscreetMode: Boolean = false,
    val isPinLocked: Boolean = false,
    val pinCode: String = "",
    val reminderEnabled: Boolean = true,
    val language: String = "system",
    val isOnboardingCompleted: Boolean = false,
    val trackingGoal: String = "cycle"
) {
    fun toDomain(): UserSettings {
        return UserSettings(
            avgCycleLength = avgCycleLength,
            avgPeriodLength = avgPeriodLength,
            lutealPhaseLength = lutealPhaseLength,
            temperatureUnit = temperatureUnit,
            isDiscreetMode = isDiscreetMode,
            isPinLocked = isPinLocked,
            pinCode = pinCode,
            reminderEnabled = reminderEnabled,
            language = language,
            isOnboardingCompleted = isOnboardingCompleted,
            trackingGoal = trackingGoal
        )
    }

    companion object {
        fun fromDomain(settings: UserSettings): UserSettingsEntity {
            return UserSettingsEntity(
                id = 1,
                avgCycleLength = settings.avgCycleLength,
                avgPeriodLength = settings.avgPeriodLength,
                lutealPhaseLength = settings.lutealPhaseLength,
                temperatureUnit = settings.temperatureUnit,
                isDiscreetMode = settings.isDiscreetMode,
                isPinLocked = settings.isPinLocked,
                pinCode = settings.pinCode,
                reminderEnabled = settings.reminderEnabled,
                language = settings.language,
                isOnboardingCompleted = settings.isOnboardingCompleted,
                trackingGoal = settings.trackingGoal
            )
        }
    }
}
