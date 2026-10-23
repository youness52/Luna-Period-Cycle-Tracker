package com.example.data.model

import java.time.LocalDate

enum class FlowIntensity(val displayName: String, val level: Int) {
    NONE("No Flow", 0),
    SPOTTING("Spotting", 1),
    LIGHT("Light", 2),
    MEDIUM("Medium", 3),
    HEAVY("Heavy", 4)
}

enum class CrampLevel(val displayName: String, val severity: Int) {
    NONE("No Cramps", 0),
    MILD("Mild", 1),
    MODERATE("Moderate", 2),
    SEVERE("Severe", 3)
}

enum class MoodType(val displayName: String, val emoji: String) {
    HAPPY("Happy", "😊"),
    CALM("Calm", "😌"),
    ENERGETIC("Energetic", "⚡"),
    SENSITIVE("Sensitive", "🥺"),
    IRRITABLE("Irritable", "😤"),
    ANXIOUS("Anxious", "😰"),
    TIRED("Exhausted", "😴"),
    SAD("Low / Sad", "😢")
}

enum class SymptomType(val displayName: String, val emoji: String) {
    HEADACHE("Headache", "🤕"),
    BLOATING("Bloating", "🎈"),
    BREAST_TENDERNESS("Tender Breasts", "🌸"),
    FATIGUE("Fatigue", "💤"),
    BACKACHE("Lower Back", "🩹"),
    ACNE("Acne", "✨"),
    NAUSEA("Nausea", "🤢"),
    CRAVINGS("Food Cravings", "🍫"),
    INSOMNIA("Insomnia", "🌙")
}

enum class CervicalMucus(val displayName: String) {
    NONE("None / Not tracked"),
    DRY("Dry"),
    STICKY("Sticky"),
    CREAMY("Creamy"),
    EGG_WHITE("Egg White (Fertile)"),
    WATERY("Watery")
}

enum class IntimacyType(val displayName: String) {
    NONE("None"),
    PROTECTED("Protected"),
    UNPROTECTED("Unprotected"),
    HIGH_LIBIDO("High Libido")
}

enum class PregnancyChance(val displayName: String, val level: String) {
    LOW("Low chance of pregnancy", "Low"),
    MEDIUM("Medium chance of pregnancy", "Medium"),
    HIGH("High chance of pregnancy", "High"),
    PEAK("Peak fertility today", "Peak")
}

enum class CyclePhase(
    val title: String,
    val subtitle: String,
    val hormoneSummary: String,
    val energyLevel: String,
    val nutritionTip: String,
    val workoutTip: String
) {
    MENSTRUAL(
        title = "Menstrual Phase",
        subtitle = "Days 1 – 5 • Time to rest and renew",
        hormoneSummary = "Estrogen and progesterone drop to their lowest levels. The uterine lining sheds.",
        energyLevel = "Low to reflective",
        nutritionTip = "Focus on iron-rich foods (spinach, lentils), warm herbal teas, and magnesium.",
        workoutTip = "Gentle stretching, restorative yin yoga, slow walks, or complete rest."
    ),
    FOLLICULAR(
        title = "Follicular Phase",
        subtitle = "Days 6 – 12 • Energy & creativity rising",
        hormoneSummary = "FSH stimulates follicles; estrogen progressively increases, boosting serotonin.",
        energyLevel = "High & vibrant",
        nutritionTip = "Light, fresh vibrant vegetables, sprouted greens, fermented foods, and lean proteins.",
        workoutTip = "Strength training, cardio, hiking, and exploring new workout routines."
    ),
    OVULATION(
        title = "Ovulatory Phase",
        subtitle = "Days 13 – 16 • Peak fertility & confidence",
        hormoneSummary = "LH surge triggers egg release. Estrogen and testosterone reach monthly peak.",
        energyLevel = "Maximum confidence & endurance",
        nutritionTip = "Antioxidant-rich berries, leafy cruciferous greens, and plenty of hydration.",
        workoutTip = "High-Intensity Interval Training (HIIT), heavy weight lifting, or social group fitness."
    ),
    LUTEAL(
        title = "Luteal Phase",
        subtitle = "Days 17 – 28 • Winding down & nesting",
        hormoneSummary = "Progesterone rises to support potential pregnancy, then tapers off before menstruation.",
        energyLevel = "Gradually slowing down",
        nutritionTip = "Complex carbohydrates (sweet potatoes, oats), seeds (sunflower, sesame), and dark chocolate.",
        workoutTip = "Moderate Pilates, steady-state cardio, swimming, or flow yoga."
    )
}

data class DayLog(
    val date: LocalDate,
    val isPeriod: Boolean = false,
    val flowIntensity: FlowIntensity = FlowIntensity.NONE,
    val crampLevel: CrampLevel = CrampLevel.NONE,
    val symptoms: Set<SymptomType> = emptySet(),
    val moods: Set<MoodType> = emptySet(),
    val cervicalMucus: CervicalMucus = CervicalMucus.NONE,
    val intimacy: IntimacyType = IntimacyType.NONE,
    val bbt: Double? = null,
    val waterGlasses: Int = 0,
    val sleepHours: Float = 0f,
    val notes: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

data class UserSettings(
    val avgCycleLength: Int = 28,
    val avgPeriodLength: Int = 5,
    val lutealPhaseLength: Int = 14,
    val temperatureUnit: String = "C",
    val isDiscreetMode: Boolean = false,
    val isPinLocked: Boolean = false,
    val pinCode: String = "",
    val reminderEnabled: Boolean = true
)

data class CycleHistoryItem(
    val cycleNumber: Int,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val cycleLengthDays: Int,
    val periodLengthDays: Int,
    val isCurrent: Boolean = false
)

data class CycleInsightSummary(
    val currentPhase: CyclePhase,
    val cycleDay: Int,
    val totalCycleDays: Int,
    val daysUntilPeriod: Int,
    val nextPeriodDate: LocalDate,
    val nextOvulationDate: LocalDate,
    val fertileWindowStart: LocalDate,
    val fertileWindowEnd: LocalDate,
    val pregnancyChance: PregnancyChance,
    val isPeriodActiveToday: Boolean,
    val avgCycleLength: Int,
    val avgPeriodLength: Int,
    val cycleRegularityScore: Int // 0-100%
)
