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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CervicalMucus
import com.example.data.model.CrampLevel
import com.example.data.model.DayLog
import com.example.data.model.FlowIntensity
import com.example.data.model.IntimacyType
import com.example.data.model.MoodType
import com.example.data.model.SymptomType
import com.example.ui.theme.PeriodCrimson
import com.example.ui.theme.WaterBlue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LogDayBottomSheet(
    date: LocalDate,
    existingLog: DayLog?,
    temperatureUnit: String,
    onSave: (DayLog) -> Unit,
    onDelete: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isPeriod by remember { mutableStateOf(existingLog?.isPeriod ?: false) }
    var flowIntensity by remember { mutableStateOf(existingLog?.flowIntensity ?: FlowIntensity.NONE) }
    var crampLevel by remember { mutableStateOf(existingLog?.crampLevel ?: CrampLevel.NONE) }
    var selectedSymptoms by remember { mutableStateOf(existingLog?.symptoms ?: emptySet()) }
    var selectedMoods by remember { mutableStateOf(existingLog?.moods ?: emptySet()) }
    var cervicalMucus by remember { mutableStateOf(existingLog?.cervicalMucus ?: CervicalMucus.NONE) }
    var intimacy by remember { mutableStateOf(existingLog?.intimacy ?: IntimacyType.NONE) }
    var bbtInput by remember { mutableStateOf(existingLog?.bbt?.toString() ?: "") }
    var waterGlasses by remember { mutableIntStateOf(existingLog?.waterGlasses ?: 0) }
    var sleepHours by remember { mutableFloatStateOf(existingLog?.sleepHours ?: 7f) }
    var notes by remember { mutableStateOf(existingLog?.notes ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Daily Symptom Log",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_sheet_button")) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 1: Menstruation & Flow
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isPeriod) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = PeriodCrimson,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Period Flow",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Toggle bleeding
                        Button(
                            onClick = {
                                isPeriod = !isPeriod
                                if (isPeriod && flowIntensity == FlowIntensity.NONE) {
                                    flowIntensity = FlowIntensity.MEDIUM
                                } else if (!isPeriod) {
                                    flowIntensity = FlowIntensity.NONE
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPeriod) PeriodCrimson else MaterialTheme.colorScheme.outlineVariant,
                                contentColor = if (isPeriod) Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("toggle_period_bleeding")
                        ) {
                            Text(text = if (isPeriod) "Bleeding" else "No Flow", fontSize = 12.sp)
                        }
                    }

                    if (isPeriod) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                FlowIntensity.SPOTTING to "Spotting",
                                FlowIntensity.LIGHT to "Light",
                                FlowIntensity.MEDIUM to "Medium",
                                FlowIntensity.HEAVY to "Heavy"
                            ).forEach { (flow, label) ->
                                val isSelected = flowIntensity == flow
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) PeriodCrimson else MaterialTheme.colorScheme.surface)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) PeriodCrimson else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            flowIntensity = flow
                                            isPeriod = true
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Cramps & Pain
            Text(
                text = "Cramps & Pain Intensity",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CrampLevel.values().forEach { level ->
                    val isSelected = crampLevel == level
                    FilterChip(
                        selected = isSelected,
                        onClick = { crampLevel = level },
                        label = { Text(level.displayName, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 3: Physical Symptoms
            Text(
                text = "Physical Symptoms",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SymptomType.values().forEach { symptom ->
                    val isSelected = selectedSymptoms.contains(symptom)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedSymptoms = if (isSelected) {
                                selectedSymptoms - symptom
                            } else {
                                selectedSymptoms + symptom
                            }
                        },
                        label = { Text("${symptom.emoji} ${symptom.displayName}", fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 4: Moods & Emotions
            Text(
                text = "Mood & Mind",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MoodType.values().forEach { mood ->
                    val isSelected = selectedMoods.contains(mood)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedMoods = if (isSelected) {
                                selectedMoods - mood
                            } else {
                                selectedMoods + mood
                            }
                        },
                        label = { Text("${mood.emoji} ${mood.displayName}", fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 5: Cervical Mucus
            Text(
                text = "Cervical Fluid / Mucus",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CervicalMucus.values().forEach { mucus ->
                    val isSelected = cervicalMucus == mucus
                    FilterChip(
                        selected = isSelected,
                        onClick = { cervicalMucus = mucus },
                        label = { Text(mucus.displayName, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 6: Intimacy & Sex Drive
            Text(
                text = "Intimacy & Libido",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IntimacyType.values().forEach { intimacyType ->
                    val isSelected = intimacy == intimacyType
                    FilterChip(
                        selected = isSelected,
                        onClick = { intimacy = intimacyType },
                        label = { Text(intimacyType.displayName, fontSize = 11.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 7: Hydration, Sleep & Temperature
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Water Tracker
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Water Intake", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = { if (waterGlasses > 0) waterGlasses-- },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease water", tint = WaterBlue)
                            }
                            Text(
                                text = "💧 $waterGlasses",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            IconButton(
                                onClick = { waterGlasses++ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase water", tint = WaterBlue)
                            }
                        }
                    }
                }

                // Sleep hours
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Sleep Rest", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = { if (sleepHours > 1) sleepHours -= 0.5f },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease sleep")
                            }
                            Text(
                                text = "🌙 ${sleepHours}h",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            IconButton(
                                onClick = { if (sleepHours < 16) sleepHours += 0.5f },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase sleep")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Basal Body Temp
            OutlinedTextField(
                value = bbtInput,
                onValueChange = { bbtInput = it },
                label = { Text("Basal Body Temp (°$temperatureUnit)") },
                placeholder = { Text(if (temperatureUnit == "C") "36.6" else "97.8") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Private Daily Notes & Medication") },
                placeholder = { Text("Record any thoughts, vitamins taken, or body sensations...") },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Button(
                onClick = {
                    val parsedBbt = bbtInput.toDoubleOrNull()
                    val log = DayLog(
                        date = date,
                        isPeriod = isPeriod,
                        flowIntensity = if (isPeriod) flowIntensity else FlowIntensity.NONE,
                        crampLevel = crampLevel,
                        symptoms = selectedSymptoms,
                        moods = selectedMoods,
                        cervicalMucus = cervicalMucus,
                        intimacy = intimacy,
                        bbt = parsedBbt,
                        waterGlasses = waterGlasses,
                        sleepHours = sleepHours,
                        notes = notes.trim(),
                        updatedAt = System.currentTimeMillis()
                    )
                    onSave(log)
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_day_log_button")
            ) {
                Text(text = "Save Entry", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            if (existingLog != null) {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { onDelete(date) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("delete_day_log_button")
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Clear Log For This Day")
                }
            }
        }
    }
}
