package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserSettings

@Composable
fun SettingsScreen(
    settings: UserSettings,
    onSaveSettings: (UserSettings) -> Unit,
    onSeedSampleData: () -> Unit,
    onClearAllData: () -> Unit,
    onRerunWizard: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showClearDialog by remember { mutableStateOf(false) }
    var showSeedSuccessDialog by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("settings_screen")
    ) {
        Text(
            text = stringResource(R.string.privacy_settings),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.privacy_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Guarantee Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = stringResource(R.string.privacy_card_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.privacy_card_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Privacy Controls Section
        Text(
            text = stringResource(R.string.privacy_controls),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Discreet Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.discreet_mode),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(R.string.discreet_mode_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = settings.isDiscreetMode,
                        onCheckedChange = { onSaveSettings(settings.copy(isDiscreetMode = it)) },
                        colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // App PIN Lock Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.app_pin_lock),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (settings.isPinLocked) stringResource(R.string.app_pin_locked_desc) else stringResource(R.string.app_pin_unlocked_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = settings.isPinLocked,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                showPinDialog = true
                            } else {
                                onSaveSettings(settings.copy(isPinLocked = false, pinCode = ""))
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Cycle Calculation Parameters
        Text(
            text = stringResource(R.string.cycle_parameters),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Avg Cycle Length Stepper
                ParameterStepper(
                    title = stringResource(R.string.target_cycle_length),
                    subtitle = stringResource(R.string.target_cycle_subtitle),
                    value = settings.avgCycleLength,
                    unit = stringResource(R.string.days),
                    onDecrease = {
                        if (settings.avgCycleLength > 20) {
                            onSaveSettings(settings.copy(avgCycleLength = settings.avgCycleLength - 1))
                        }
                    },
                    onIncrease = {
                        if (settings.avgCycleLength < 45) {
                            onSaveSettings(settings.copy(avgCycleLength = settings.avgCycleLength + 1))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Avg Period Length Stepper
                ParameterStepper(
                    title = stringResource(R.string.target_period_length),
                    subtitle = stringResource(R.string.target_period_subtitle),
                    value = settings.avgPeriodLength,
                    unit = stringResource(R.string.days),
                    onDecrease = {
                        if (settings.avgPeriodLength > 2) {
                            onSaveSettings(settings.copy(avgPeriodLength = settings.avgPeriodLength - 1))
                        }
                    },
                    onIncrease = {
                        if (settings.avgPeriodLength < 12) {
                            onSaveSettings(settings.copy(avgPeriodLength = settings.avgPeriodLength + 1))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Temperature Unit Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.temp_unit),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(R.string.temp_unit_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("C" to "°C", "F" to "°F").forEach { (unitCode, label) ->
                            val isSelected = settings.temperatureUnit == unitCode
                            Button(
                                onClick = { onSaveSettings(settings.copy(temperatureUnit = unitCode)) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(text = label, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // App Language Selector (English / العربية / System)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.language_setting),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = stringResource(R.string.language_setting_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "system" to stringResource(R.string.lang_system),
                            "en" to stringResource(R.string.lang_en),
                            "ar" to stringResource(R.string.lang_ar)
                        ).forEach { (langCode, label) ->
                            val isSelected = settings.language == langCode
                            Button(
                                onClick = { onSaveSettings(settings.copy(language = langCode)) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("lang_btn_$langCode")
                            ) {
                                Text(
                                    text = label,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Data & Management
        Text(
            text = stringResource(R.string.data_management),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Pre-seed sample cycles
                OutlinedButton(
                    onClick = {
                        onSeedSampleData()
                        showSeedSuccessDialog = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("seed_sample_data_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.load_sample_data))
                }

                // Rerun Setup Wizard
                OutlinedButton(
                    onClick = onRerunWizard,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("rerun_wizard_button")
                ) {
                    Icon(imageVector = Icons.Default.Spa, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.rerun_wizard))
                }

                // Clear all data
                OutlinedButton(
                    onClick = { showClearDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("clear_data_button")
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.erase_all_data))
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Medical disclaimer
        Text(
            text = stringResource(R.string.medical_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Clear Confirmation Dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(text = stringResource(R.string.erase_dialog_title)) },
            text = { Text(text = stringResource(R.string.erase_dialog_desc)) },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllData()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(R.string.erase_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }

    // Seed Success Dialog
    if (showSeedSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSeedSuccessDialog = false },
            title = { Text(text = stringResource(R.string.seed_success_title)) },
            text = { Text(text = stringResource(R.string.seed_success_desc)) },
            confirmButton = {
                Button(onClick = { showSeedSuccessDialog = false }) {
                    Text(text = stringResource(R.string.explore_luna))
                }
            }
        )
    }

    // PIN Setup Dialog
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text(text = stringResource(R.string.pin_dialog_title)) },
            text = {
                Column {
                    Text(text = stringResource(R.string.pin_dialog_desc))
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { if (it.length <= 4) pinInput = it },
                        singleLine = true,
                        placeholder = { Text("••••") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (pinInput.length >= 4) {
                            onSaveSettings(settings.copy(isPinLocked = true, pinCode = pinInput))
                            showPinDialog = false
                        }
                    },
                    enabled = pinInput.length >= 4
                ) {
                    Text(text = stringResource(R.string.set_pin))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun ParameterStepper(
    title: String,
    subtitle: String,
    value: Int,
    unit: String,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onDecrease,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
            }
            Text(
                text = "$value $unit",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.width(68.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(
                onClick = onIncrease,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}
