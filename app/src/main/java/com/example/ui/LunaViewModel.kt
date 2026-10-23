package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CycleHistoryItem
import com.example.data.model.CycleInsightSummary
import com.example.data.model.DayLog
import com.example.data.model.FlowIntensity
import com.example.data.model.UserSettings
import com.example.data.repository.CycleRepository
import com.example.domain.CycleEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class LunaTab(val title: String) {
    TODAY("Today"),
    CALENDAR("Calendar"),
    INSIGHTS("Insights"),
    SETTINGS("Privacy & Settings")
}

class LunaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CycleRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = CycleRepository(db.dayLogDao(), db.userSettingsDao())
    }

    val allLogs: StateFlow<List<DayLog>> = repository.getAllLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val settings: StateFlow<UserSettings> = repository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettings()
        )

    val currentInsights: StateFlow<CycleInsightSummary> = combine(allLogs, settings) { logs, sets ->
        CycleEngine.computeInsights(logs, sets, LocalDate.now())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CycleEngine.computeInsights(emptyList(), UserSettings(), LocalDate.now())
    )

    val cycleHistory: StateFlow<List<CycleHistoryItem>> = allLogs.map { logs ->
        val periodDays = logs.filter { it.isPeriod }.map { it.date }.sorted()
        val episodes = CycleEngine.extractPeriodEpisodes(periodDays)
        CycleEngine.computeCycleHistory(episodes, LocalDate.now())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _activeTab = MutableStateFlow(LunaTab.TODAY)
    val activeTab: StateFlow<LunaTab> = _activeTab.asStateFlow()

    private val _isLogSheetOpen = MutableStateFlow(false)
    val isLogSheetOpen: StateFlow<Boolean> = _isLogSheetOpen.asStateFlow()

    private val _logSheetDate = MutableStateFlow(LocalDate.now())
    val logSheetDate: StateFlow<LocalDate> = _logSheetDate.asStateFlow()

    private val _isPinUnlocked = MutableStateFlow(false)
    val isPinUnlocked: StateFlow<Boolean> = _isPinUnlocked.asStateFlow()

    fun selectTab(tab: LunaTab) {
        _activeTab.value = tab
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun openLogSheet(date: LocalDate) {
        _logSheetDate.value = date
        _isLogSheetOpen.value = true
    }

    fun closeLogSheet() {
        _isLogSheetOpen.value = false
    }

    fun quickTogglePeriodToday() {
        val today = LocalDate.now()
        val existing = allLogs.value.find { it.date == today }
        viewModelScope.launch {
            if (existing != null) {
                val newPeriodState = !existing.isPeriod
                val updated = existing.copy(
                    isPeriod = newPeriodState,
                    flowIntensity = if (newPeriodState) FlowIntensity.MEDIUM else FlowIntensity.NONE,
                    updatedAt = System.currentTimeMillis()
                )
                repository.saveDayLog(updated)
            } else {
                val newLog = DayLog(
                    date = today,
                    isPeriod = true,
                    flowIntensity = FlowIntensity.MEDIUM
                )
                repository.saveDayLog(newLog)
            }
        }
    }

    fun saveDayLog(log: DayLog) {
        viewModelScope.launch {
            repository.saveDayLog(log)
            closeLogSheet()
        }
    }

    fun deleteDayLog(date: LocalDate) {
        viewModelScope.launch {
            repository.deleteDayLog(date)
            closeLogSheet()
        }
    }

    fun saveSettings(newSettings: UserSettings) {
        viewModelScope.launch {
            repository.saveSettings(newSettings)
        }
    }

    fun seedSampleData() {
        viewModelScope.launch {
            repository.seedSampleCycles()
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    fun unlockPin(pin: String): Boolean {
        if (pin == settings.value.pinCode) {
            _isPinUnlocked.value = true
            return true
        }
        return false
    }

    fun lockApp() {
        _isPinUnlocked.value = false
    }
}
