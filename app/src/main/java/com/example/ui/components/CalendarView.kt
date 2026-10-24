package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DayLog
import com.example.data.model.UserSettings
import com.example.domain.CycleEngine
import com.example.ui.theme.FertileLight
import com.example.ui.theme.FertilePurple
import com.example.ui.theme.OvulationAmber
import com.example.ui.theme.PeriodCrimson
import com.example.ui.theme.PeriodLight
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalendarView(
    allLogs: List<DayLog>,
    settings: UserSettings,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    onOpenLogger: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    val today = LocalDate.now()
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0] ?: Locale.getDefault()
    val isAr = locale.language == "ar"

    // Compute status map for current month view
    val startOfMonth = currentMonth.atDay(1)
    val endOfMonth = currentMonth.atEndOfMonth()
    val statusMap = remember(allLogs, settings, currentMonth) {
        CycleEngine.getDateStatusMap(
            allLogs = allLogs,
            settings = settings,
            startMonth = startOfMonth.minusWeeks(1),
            endMonth = endOfMonth.plusWeeks(1)
        )
    }

    val selectedLog = allLogs.find { it.date == selectedDate }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Month Navigation Header
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { currentMonth = currentMonth.minusMonths(1) },
                        modifier = Modifier.testTag("prev_month_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Month"
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale)),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = {
                                currentMonth = YearMonth.from(today)
                                onSelectDate(today)
                            },
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("jump_today_button")
                        ) {
                            Text(text = stringResource(R.string.today), fontSize = 12.sp)
                        }

                        IconButton(
                            onClick = { currentMonth = currentMonth.plusMonths(1) },
                            modifier = Modifier.testTag("next_month_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Month"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Days of Week Header
                Row(modifier = Modifier.fillMaxWidth()) {
                    val daysOfWeek = listOf(
                        DayOfWeek.SUNDAY,
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY,
                        DayOfWeek.SATURDAY
                    )
                    daysOfWeek.forEach { dayOfWeek ->
                        Text(
                            text = dayOfWeek.getDisplayName(TextStyle.SHORT, locale).take(3),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Month Calendar Grid
                val firstDayOfMonth = currentMonth.atDay(1)
                val daysInMonth = currentMonth.lengthOfMonth()
                val dayOffset = firstDayOfMonth.dayOfWeek.value % 7
                val totalCells = ((dayOffset + daysInMonth + 6) / 7) * 7

                for (weekIndex in 0 until (totalCells / 7)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (dayCol in 0..6) {
                            val cellIndex = weekIndex * 7 + dayCol
                            val dayNumber = cellIndex - dayOffset + 1

                            if (dayNumber in 1..daysInMonth) {
                                val cellDate = currentMonth.atDay(dayNumber)
                                val status = statusMap[cellDate] ?: CycleEngine.DateStatus()
                                val isSelected = cellDate == selectedDate
                                val isToday = cellDate == today

                                CalendarDayCell(
                                    date = cellDate,
                                    dayNumber = dayNumber,
                                    status = status,
                                    isSelected = isSelected,
                                    isToday = isToday,
                                    onSelect = { onSelectDate(cellDate) },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Legend
                CalendarLegend()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Date Details Card
        SelectedDateCard(
            date = selectedDate,
            dayLog = selectedLog,
            status = statusMap[selectedDate] ?: CycleEngine.DateStatus(),
            locale = locale,
            isAr = isAr,
            onOpenLogger = { onOpenLogger(selectedDate) }
        )
    }
}

@Composable
fun CalendarDayCell(
    date: LocalDate,
    dayNumber: Int,
    status: CycleEngine.DateStatus,
    isSelected: Boolean,
    isToday: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        status.isPeriodLogged -> PeriodCrimson
        status.isPredictedPeriod -> PeriodLight.copy(alpha = 0.25f)
        status.isOvulationPeak -> OvulationAmber.copy(alpha = 0.35f)
        status.isFertileWindow -> FertileLight
        else -> Color.Transparent
    }

    val textColor = when {
        status.isPeriodLogged -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                } else if (isToday) {
                    Modifier.border(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                } else if (status.isPredictedPeriod) {
                    Modifier.border(1.dp, PeriodLight.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                } else {
                    Modifier
                }
            )
            .clickable { onSelect() }
            .testTag("calendar_day_${date}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dayNumber.toString(),
                fontSize = 13.sp,
                fontWeight = if (isToday || isSelected || status.isPeriodLogged) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )

            // Dot indicators row
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                if (status.isPeriodLogged) {
                    Box(
                        modifier = Modifier
                            .size(3.5.dp)
                            .background(Color.White, CircleShape)
                    )
                }
                if (status.hasSymptoms) {
                    Box(
                        modifier = Modifier
                            .size(3.5.dp)
                            .background(
                                if (status.isPeriodLogged) Color.White.copy(alpha = 0.8f) else OvulationAmber,
                                CircleShape
                            )
                    )
                }
                if (status.hasMoods || status.hasNotes) {
                    Box(
                        modifier = Modifier
                            .size(3.5.dp)
                            .background(
                                if (status.isPeriodLogged) Color.White.copy(alpha = 0.8f) else FertilePurple,
                                CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = PeriodCrimson, label = stringResource(R.string.legend_period))
        LegendItem(color = PeriodLight, isBorder = true, label = stringResource(R.string.legend_predicted))
        LegendItem(color = FertileLight, borderTint = FertilePurple, label = stringResource(R.string.legend_fertile))
        LegendItem(color = OvulationAmber, label = stringResource(R.string.legend_ovulation))
    }
}

@Composable
fun LegendItem(
    color: Color,
    label: String,
    isBorder: Boolean = false,
    borderTint: Color? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (isBorder) Modifier.border(1.dp, PeriodCrimson, CircleShape)
                    else if (borderTint != null) Modifier.border(1.dp, borderTint, CircleShape)
                    else Modifier
                )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectedDateCard(
    date: LocalDate,
    dayLog: DayLog?,
    status: CycleEngine.DateStatus,
    locale: Locale,
    isAr: Boolean,
    onOpenLogger: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", locale)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("selected_date_summary_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = date.format(formatter),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when {
                            status.isPeriodLogged -> stringResource(R.string.menstruation_logged)
                            status.isPredictedPeriod -> stringResource(R.string.predicted_period_day)
                            status.isOvulationPeak -> stringResource(R.string.estimated_ovulation_day)
                            status.isFertileWindow -> stringResource(R.string.fertile_window)
                            else -> stringResource(R.string.cycle_day)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = onOpenLogger,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("edit_log_button")
                ) {
                    Icon(
                        imageVector = if (dayLog != null) Icons.Default.Edit else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (dayLog != null) stringResource(R.string.edit_log) else stringResource(R.string.log_day),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (dayLog == null || (!dayLog.isPeriod && dayLog.symptoms.isEmpty() && dayLog.moods.isEmpty() && dayLog.notes.isBlank() && dayLog.waterGlasses == 0)) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.no_symptoms_logged),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Flow status
                    if (dayLog.isPeriod) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = PeriodCrimson,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.flow_label, dayLog.flowIntensity.localized(isAr)),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = PeriodCrimson
                            )
                        }
                    }

                    // Cramps
                    if (dayLog.crampLevel != com.example.data.model.CrampLevel.NONE) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "💥", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.cramps_label, dayLog.crampLevel.localized(isAr)),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Symptoms
                    if (dayLog.symptoms.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.symptoms_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            dayLog.symptoms.forEach { symptom ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${symptom.emoji} ${symptom.localized(isAr)}",
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Moods
                    if (dayLog.moods.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.mood_prefix),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            dayLog.moods.forEach { mood ->
                                Surface(
                                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${mood.emoji} ${mood.localized(isAr)}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Hydration & Sleep
                    if (dayLog.waterGlasses > 0 || dayLog.sleepHours > 0) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            if (dayLog.waterGlasses > 0) {
                                Text(
                                    text = "💧 " + stringResource(R.string.water_glasses, dayLog.waterGlasses),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (dayLog.sleepHours > 0) {
                                Text(
                                    text = "🌙 ${dayLog.sleepHours}h",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (dayLog.bbt != null) {
                                Text(
                                    text = "🌡️ ${dayLog.bbt}°",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Notes
                    if (dayLog.notes.isNotBlank()) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📝 ${dayLog.notes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
