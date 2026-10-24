package com.example.data.model

import java.time.LocalDate

enum class FlowIntensity(val displayName: String, val level: Int) {
    NONE("No Flow", 0),
    SPOTTING("Spotting", 1),
    LIGHT("Light", 2),
    MEDIUM("Medium", 3),
    HEAVY("Heavy", 4);

    fun localized(isAr: Boolean): String = if (isAr) when (this) {
        NONE -> "بدون تدفق"
        SPOTTING -> "تبقيع"
        LIGHT -> "خفيف"
        MEDIUM -> "متوسط"
        HEAVY -> "غزير"
    } else displayName
}

enum class CrampLevel(val displayName: String, val severity: Int) {
    NONE("No Cramps", 0),
    MILD("Mild", 1),
    MODERATE("Moderate", 2),
    SEVERE("Severe", 3);

    fun localized(isAr: Boolean): String = if (isAr) when (this) {
        NONE -> "بدون تقلصات"
        MILD -> "خفيفة"
        MODERATE -> "متوسطة"
        SEVERE -> "شديدة"
    } else displayName
}

enum class MoodType(val displayName: String, val emoji: String) {
    HAPPY("Happy", "😊"),
    CALM("Calm", "😌"),
    ENERGETIC("Energetic", "⚡"),
    SENSITIVE("Sensitive", "🥺"),
    IRRITABLE("Irritable", "😤"),
    ANXIOUS("Anxious", "😰"),
    TIRED("Exhausted", "😴"),
    SAD("Low / Sad", "😢");

    fun localized(isAr: Boolean): String = if (isAr) when (this) {
        HAPPY -> "سعيدة"
        CALM -> "هادئة"
        ENERGETIC -> "مفعمة بالنشاط"
        SENSITIVE -> "حساسة"
        IRRITABLE -> "متوترة"
        ANXIOUS -> "قلقة"
        TIRED -> "مرهقة"
        SAD -> "حزينة"
    } else displayName
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
    INSOMNIA("Insomnia", "🌙");

    fun localized(isAr: Boolean): String = if (isAr) when (this) {
        HEADACHE -> "صداع"
        BLOATING -> "انتفاخ"
        BREAST_TENDERNESS -> "ألم الثدي"
        FATIGUE -> "تعب وإرهاق"
        BACKACHE -> "ألم الظهر"
        ACNE -> "حب الشباب"
        NAUSEA -> "غثيان"
        CRAVINGS -> "رغبة بالحلويات"
        INSOMNIA -> "أرق"
    } else displayName
}

enum class CervicalMucus(val displayName: String) {
    NONE("None / Not tracked"),
    DRY("Dry"),
    STICKY("Sticky"),
    CREAMY("Creamy"),
    EGG_WHITE("Egg White (Fertile)"),
    WATERY("Watery");

    fun localized(isAr: Boolean): String = if (isAr) when (this) {
        NONE -> "بدون تتبع"
        DRY -> "جافة"
        STICKY -> "لزجة"
        CREAMY -> "كريمية"
        EGG_WHITE -> "بياض البيض (خصوبة)"
        WATERY -> "مائية"
    } else displayName
}

enum class IntimacyType(val displayName: String) {
    NONE("None"),
    PROTECTED("Protected"),
    UNPROTECTED("Unprotected"),
    HIGH_LIBIDO("High Libido");

    fun localized(isAr: Boolean): String = if (isAr) when (this) {
        NONE -> "لا يوجد"
        PROTECTED -> "محمية"
        UNPROTECTED -> "غير محمية"
        HIGH_LIBIDO -> "رغبة عالية"
    } else displayName
}

enum class PregnancyChance(val displayName: String, val level: String) {
    LOW("Low chance of pregnancy", "Low"),
    MEDIUM("Medium chance of pregnancy", "Medium"),
    HIGH("High chance of pregnancy", "High"),
    PEAK("Peak fertility today", "Peak");

    fun localizedLevel(isAr: Boolean): String = if (isAr) when (this) {
        LOW -> "منخفضة"
        MEDIUM -> "متوسطة"
        HIGH -> "مرتفعة"
        PEAK -> "ذروة"
    } else level
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
    );

    fun localizedTitle(isAr: Boolean): String = if (isAr) when (this) {
        MENSTRUAL -> "مرحلة الحيض"
        FOLLICULAR -> "المرحلة الجريبية"
        OVULATION -> "مرحلة الإباضة"
        LUTEAL -> "المرحلة الأصفونية"
    } else title

    fun localizedSubtitle(isAr: Boolean): String = if (isAr) when (this) {
        MENSTRUAL -> "الأيام 1 – 5 • وقت الراحة وتجديد الطاقة"
        FOLLICULAR -> "الأيام 6 – 12 • تصاعد الحيوية والإبداع"
        OVULATION -> "الأيام 13 – 16 • ذروة الخصوبة والتألق"
        LUTEAL -> "الأيام 17 – 28 • الهدوء والاستعداد للدورة"
    } else subtitle

    fun localizedHormones(isAr: Boolean): String = if (isAr) when (this) {
        MENSTRUAL -> "ينخفض الإستروجين والبروجستيرون لأدنى مستوياتهما لتجديد بطانة الرحم."
        FOLLICULAR -> "يحفز هرمون FSH نمو الجريبات؛ ويرتفع الإستروجين تدريجياً لتعزيز المزاج."
        OVULATION -> "ذروة هرمون LH تطلق البويضة. يصل الإستروجين والتستوستيرون لأعلى تركيز شهري."
        LUTEAL -> "يرتفع البروجستيرون لدعم الجسم، ثم يتراجع تدريجياً قبل بداية الدورة."
    } else hormoneSummary

    fun localizedEnergy(isAr: Boolean): String = if (isAr) when (this) {
        MENSTRUAL -> "هادئة ومسترخية"
        FOLLICULAR -> "حيوية ونشاط متصاعد"
        OVULATION -> "أعلى طاقة وثقة عالية"
        LUTEAL -> "تباطؤ تدريجي وحاجة للراحة"
    } else energyLevel

    fun localizedNutrition(isAr: Boolean): String = if (isAr) when (this) {
        MENSTRUAL -> "أطعمة غنية بالحديد (سبانخ، عدس)، مشروبات دافئة، والمغنيسيوم."
        FOLLICULAR -> "أطعمة طازجة، خضروات ورقية، أطعمة مخمرة، وبروتينات خفيفة."
        OVULATION -> "توتيات غنية بمضادات الأكسدة، خضار ورقية، وترطيب مستمر بالماء."
        LUTEAL -> "كربوهيدرات معقدة (بطاطا حلوة، شوفان)، بذور، وشوكولاتة داكنة."
    } else nutritionTip

    fun localizedWorkout(isAr: Boolean): String = if (isAr) when (this) {
        MENSTRUAL -> "تمارين استطالة لطيفة، يوغا مهدئة، مشي خفيف، أو راحة تامة."
        FOLLICULAR -> "تمارين تقوية، كارديو، مشي نشط، وتجربة رياضات جديدة."
        OVULATION -> "تمارين عالية الكثافة (HIIT)، تدريب أوزان، ورياضات جماعية."
        LUTEAL -> "بيلاتس معتدل، يوغا انسيابية، سباحة، وتمارين إطالة."
    } else workoutTip
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
    val reminderEnabled: Boolean = true,
    val language: String = "system" // "system", "ar", "en"
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
    val cycleRegularityScore: Int
)
