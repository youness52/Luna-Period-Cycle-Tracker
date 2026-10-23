package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DayLog
import com.example.ui.components.LogDayBottomSheet
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.InsightsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TodayScreen
import java.time.LocalDate

@Composable
fun LunaMainScreen(viewModel: LunaViewModel) {
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val allLogs by viewModel.allLogs.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val insights by viewModel.currentInsights.collectAsStateWithLifecycle()
    val cycleHistory by viewModel.cycleHistory.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val isLogSheetOpen by viewModel.isLogSheetOpen.collectAsStateWithLifecycle()
    val logSheetDate by viewModel.logSheetDate.collectAsStateWithLifecycle()
    val isPinUnlocked by viewModel.isPinUnlocked.collectAsStateWithLifecycle()

    val today = LocalDate.now()
    val todayLog = allLogs.find { it.date == today }
    val activeSheetLog = allLogs.find { it.date == logSheetDate }

    // If PIN is enabled and not yet unlocked, show PIN lock gate
    if (settings.isPinLocked && !isPinUnlocked) {
        PinLockScreen(
            onUnlock = { pin -> viewModel.unlockPin(pin) }
        )
        return
    }

    // Handle system back navigation to return to TODAY tab
    BackHandler(enabled = activeTab != LunaTab.TODAY) {
        viewModel.selectTab(LunaTab.TODAY)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("luna_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = activeTab == LunaTab.TODAY,
                    onClick = { viewModel.selectTab(LunaTab.TODAY) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == LunaTab.TODAY) Icons.Filled.Spa else Icons.Outlined.Spa,
                            contentDescription = "Today"
                        )
                    },
                    label = { Text(text = "Today", fontSize = 12.sp) },
                    modifier = Modifier.testTag("tab_today")
                )

                NavigationBarItem(
                    selected = activeTab == LunaTab.CALENDAR,
                    onClick = { viewModel.selectTab(LunaTab.CALENDAR) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == LunaTab.CALENDAR) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                            contentDescription = "Calendar"
                        )
                    },
                    label = { Text(text = "Calendar", fontSize = 12.sp) },
                    modifier = Modifier.testTag("tab_calendar")
                )

                NavigationBarItem(
                    selected = activeTab == LunaTab.INSIGHTS,
                    onClick = { viewModel.selectTab(LunaTab.INSIGHTS) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == LunaTab.INSIGHTS) Icons.Filled.QueryStats else Icons.Outlined.QueryStats,
                            contentDescription = "Insights"
                        )
                    },
                    label = { Text(text = "Insights", fontSize = 12.sp) },
                    modifier = Modifier.testTag("tab_insights")
                )

                NavigationBarItem(
                    selected = activeTab == LunaTab.SETTINGS,
                    onClick = { viewModel.selectTab(LunaTab.SETTINGS) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == LunaTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    },
                    label = { Text(text = "Privacy", fontSize = 12.sp) },
                    modifier = Modifier.testTag("tab_settings")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                LunaTab.TODAY -> {
                    TodayScreen(
                        insights = insights,
                        todayLog = todayLog,
                        settings = settings,
                        onQuickTogglePeriod = { viewModel.quickTogglePeriodToday() },
                        onOpenLogger = { date -> viewModel.openLogSheet(date) },
                        onUpdateTodayWater = { glasses ->
                            val current = todayLog ?: DayLog(date = today)
                            viewModel.saveDayLog(current.copy(waterGlasses = glasses))
                        }
                    )
                }

                LunaTab.CALENDAR -> {
                    CalendarScreen(
                        allLogs = allLogs,
                        settings = settings,
                        selectedDate = selectedDate,
                        onSelectDate = { date -> viewModel.selectDate(date) },
                        onOpenLogger = { date -> viewModel.openLogSheet(date) }
                    )
                }

                LunaTab.INSIGHTS -> {
                    InsightsScreen(
                        insights = insights,
                        cycleHistory = cycleHistory,
                        allLogs = allLogs,
                        settings = settings
                    )
                }

                LunaTab.SETTINGS -> {
                    SettingsScreen(
                        settings = settings,
                        onSaveSettings = { newSets -> viewModel.saveSettings(newSets) },
                        onSeedSampleData = { viewModel.seedSampleData() },
                        onClearAllData = { viewModel.clearAllData() }
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet for Logging Day
    if (isLogSheetOpen) {
        LogDayBottomSheet(
            date = logSheetDate,
            existingLog = activeSheetLog,
            temperatureUnit = settings.temperatureUnit,
            onSave = { log -> viewModel.saveDayLog(log) },
            onDelete = { date -> viewModel.deleteDayLog(date) },
            onDismiss = { viewModel.closeLogSheet() }
        )
    }
}

@Composable
fun PinLockScreen(onUnlock: (String) -> Boolean) {
    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Luna Is Locked",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Enter your 4-digit PIN to access private cycle data",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = enteredPin,
                onValueChange = {
                    if (it.length <= 4) {
                        enteredPin = it
                        isError = false
                    }
                },
                placeholder = { Text("••••") },
                singleLine = true,
                isError = isError,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(180.dp)
                    .testTag("pin_input_field")
            )

            if (isError) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Incorrect PIN code. Try again.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val success = onUnlock(enteredPin)
                    if (!success) {
                        isError = true
                    }
                },
                enabled = enteredPin.length >= 4,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(180.dp)
                    .height(48.dp)
                    .testTag("unlock_button")
            ) {
                Text(text = "Unlock", fontWeight = FontWeight.Bold)
            }
        }
    }
}
