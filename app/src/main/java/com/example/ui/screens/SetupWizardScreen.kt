package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserSettings
import com.example.ui.theme.FertilePurple
import com.example.ui.theme.PeriodCrimson
import java.time.LocalDate

@Composable
fun SetupWizardScreen(
    initialSettings: UserSettings,
    onComplete: (UserSettings, LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(0) }
    val totalSteps = 5

    var language by remember { mutableStateOf(initialSettings.language) }
    var trackingGoal by remember { mutableStateOf(initialSettings.trackingGoal) }
    var avgCycleLength by remember { mutableIntStateOf(initialSettings.avgCycleLength) }
    var avgPeriodLength by remember { mutableIntStateOf(initialSettings.avgPeriodLength) }
    var lastPeriodStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var isDiscreetMode by remember { mutableStateOf(initialSettings.isDiscreetMode) }
    var isPinLocked by remember { mutableStateOf(initialSettings.isPinLocked) }
    var pinCode by remember { mutableStateOf(initialSettings.pinCode) }

    Scaffold(
        topBar = {
            if (step > 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { step-- },
                            modifier = Modifier.testTag("wizard_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.wizard_back)
                            )
                        }

                        Text(
                            text = stringResource(R.string.wizard_step_indicator, step, totalSteps),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        TextButton(
                            onClick = {
                                val finalSettings = initialSettings.copy(
                                    language = language,
                                    trackingGoal = trackingGoal,
                                    avgCycleLength = avgCycleLength,
                                    avgPeriodLength = avgPeriodLength,
                                    isDiscreetMode = isDiscreetMode,
                                    isPinLocked = isPinLocked,
                                    pinCode = pinCode,
                                    isOnboardingCompleted = true
                                )
                                onComplete(finalSettings, lastPeriodStartDate)
                            },
                            modifier = Modifier.testTag("wizard_skip_button")
                        ) {
                            Text(text = stringResource(R.string.wizard_skip), fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (step.toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "wizard_step_animation"
            ) { currentStep ->
                when (currentStep) {
                    0 -> StepWelcome(
                        selectedLanguage = language,
                        onLanguageChange = { language = it },
                        onNext = { step++ }
                    )
                    1 -> StepGoal(
                        selectedGoal = trackingGoal,
                        onSelectGoal = { trackingGoal = it },
                        onNext = { step++ }
                    )
                    2 -> StepCycleLength(
                        cycleLength = avgCycleLength,
                        periodLength = avgPeriodLength,
                        onCycleLengthChange = { avgCycleLength = it },
                        onPeriodLengthChange = { avgPeriodLength = it },
                        onNext = { step++ }
                    )
                    3 -> StepLastPeriod(
                        selectedDate = lastPeriodStartDate,
                        onSelectDate = { lastPeriodStartDate = it },
                        onNext = { step++ }
                    )
                    4 -> StepPrivacy(
                        isDiscreet = isDiscreetMode,
                        onDiscreetChange = { isDiscreetMode = it },
                        isPin = isPinLocked,
                        pin = pinCode,
                        onPinChange = { locked, code ->
                            isPinLocked = locked
                            pinCode = code
                        },
                        onNext = { step++ }
                    )
                    5 -> StepAllSet(
                        cycleLength = avgCycleLength,
                        periodLength = avgPeriodLength,
                        language = language,
                        hasStartDate = lastPeriodStartDate != null,
                        onFinish = {
                            val finalSettings = initialSettings.copy(
                                language = language,
                                trackingGoal = trackingGoal,
                                avgCycleLength = avgCycleLength,
                                avgPeriodLength = avgPeriodLength,
                                isDiscreetMode = isDiscreetMode,
                                isPinLocked = isPinLocked,
                                pinCode = pinCode,
                                isOnboardingCompleted = true
                            )
                            onComplete(finalSettings, lastPeriodStartDate)
                        }
                    )
                }
            }
        }
    }
}

// STEP 0: Welcome, Language & Privacy Guarantee
@Composable
fun StepWelcome(
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Icon Bloom Badge
        Surface(
            color = PeriodCrimson.copy(alpha = 0.12f),
            shape = CircleShape,
            modifier = Modifier.size(96.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = PeriodCrimson,
                    modifier = Modifier.size(54.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.wizard_welcome_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.wizard_welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Language Selection Row right upfront
        Text(
            text = stringResource(R.string.language_setting),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(
                "ar" to "العربية (Arabic)",
                "en" to "English"
            ).forEach { (langCode, label) ->
                val isSelected = selectedLanguage == langCode || (selectedLanguage == "system" && langCode == "ar")
                Button(
                    onClick = { onLanguageChange(langCode) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Privacy Guarantee Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.wizard_privacy_promise),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.wizard_privacy_promise_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("wizard_get_started_btn")
        ) {
            Text(text = stringResource(R.string.wizard_get_started), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

// STEP 1: Main Goal
@Composable
fun StepGoal(
    selectedGoal: String,
    onSelectGoal: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.wizard_goal_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.wizard_goal_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        listOf(
            Triple("cycle", stringResource(R.string.goal_cycle_title), stringResource(R.string.goal_cycle_desc)),
            Triple("fertility", stringResource(R.string.goal_fertility_title), stringResource(R.string.goal_fertility_desc)),
            Triple("wellness", stringResource(R.string.goal_wellness_title), stringResource(R.string.goal_wellness_desc))
        ).forEach { (goalKey, title, desc) ->
            val isSelected = selectedGoal == goalKey
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .then(
                        if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(18.dp))
                        else Modifier
                    )
                    .clickable { onSelectGoal(goalKey) }
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = when (goalKey) {
                                    "cycle" -> Icons.Default.WaterDrop
                                    "fertility" -> Icons.Default.Favorite
                                    else -> Icons.Default.Spa
                                },
                                contentDescription = null,
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("wizard_goal_next_btn")
        ) {
            Text(text = stringResource(R.string.wizard_next), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// STEP 2: Cycle & Period Lengths
@Composable
fun StepCycleLength(
    cycleLength: Int,
    periodLength: Int,
    onCycleLengthChange: (Int) -> Unit,
    onPeriodLengthChange: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.wizard_cycle_length_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.wizard_cycle_length_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$cycleLength " + stringResource(R.string.days),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { if (cycleLength > 20) onCycleLengthChange(cycleLength - 1) },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "−", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { if (cycleLength < 45) onCycleLengthChange(cycleLength + 1) },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = Modifier.size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.wizard_period_length_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.wizard_period_length_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$periodLength " + stringResource(R.string.days),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PeriodCrimson
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { if (periodLength > 2) onPeriodLengthChange(periodLength - 1) },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.size(44.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "−", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { if (periodLength < 12) onPeriodLengthChange(periodLength + 1) },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = Modifier.size(44.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("wizard_length_next_btn")
        ) {
            Text(text = stringResource(R.string.wizard_next), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// STEP 3: Last Period Start Date
@Composable
fun StepLastPeriod(
    selectedDate: LocalDate?,
    onSelectDate: (LocalDate?) -> Unit,
    onNext: () -> Unit
) {
    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.wizard_last_period_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.wizard_last_period_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        listOf(
            today to stringResource(R.string.wizard_today),
            today.minusDays(1) to stringResource(R.string.wizard_yesterday),
            today.minusDays(3) to stringResource(R.string.wizard_days_ago, 3),
            today.minusDays(7) to stringResource(R.string.wizard_weeks_ago, 1),
            today.minusDays(14) to stringResource(R.string.wizard_weeks_ago, 2),
            today.minusDays(21) to stringResource(R.string.wizard_weeks_ago, 3)
        ).forEach { (date, label) ->
            val isSelected = selectedDate == date
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .then(
                        if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                        else Modifier
                    )
                    .clickable { onSelectDate(date) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // "I don't remember" option
        val isNotSure = selectedDate == null
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isNotSure) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                else MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectDate(null) }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.wizard_not_sure),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("wizard_date_next_btn")
        ) {
            Text(text = stringResource(R.string.wizard_next), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// STEP 4: Privacy Settings & PIN
@Composable
fun StepPrivacy(
    isDiscreet: Boolean,
    onDiscreetChange: (Boolean) -> Unit,
    isPin: Boolean,
    pin: String,
    onPinChange: (Boolean, String) -> Unit,
    onNext: () -> Unit
) {
    var enteredPin by remember { mutableStateOf(pin) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.wizard_privacy_step_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.wizard_privacy_step_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.discreet_mode),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.discreet_mode_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = isDiscreet, onCheckedChange = onDiscreetChange)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.app_pin_lock),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.app_pin_unlocked_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isPin,
                        onCheckedChange = { checked ->
                            onPinChange(checked, if (checked) enteredPin else "")
                        }
                    )
                }

                if (isPin) {
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = {
                            if (it.length <= 4) {
                                enteredPin = it
                                onPinChange(true, it)
                            }
                        },
                        label = { Text("4-Digit PIN") },
                        placeholder = { Text("••••") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("wizard_privacy_next_btn")
        ) {
            Text(text = stringResource(R.string.wizard_next), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// STEP 5: Confirmation & All Set
@Composable
fun StepAllSet(
    cycleLength: Int,
    periodLength: Int,
    language: String,
    hasStartDate: Boolean,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CircleShape,
            modifier = Modifier.size(90.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.wizard_ready_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.wizard_ready_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Summary Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryRow(label = stringResource(R.string.target_cycle_length), value = "$cycleLength " + stringResource(R.string.days))
                SummaryRow(label = stringResource(R.string.target_period_length), value = "$periodLength " + stringResource(R.string.days))
                SummaryRow(
                    label = stringResource(R.string.language_setting),
                    value = if (language == "ar") "العربية" else "English"
                )
                SummaryRow(
                    label = stringResource(R.string.privacy_card_title),
                    value = "100% On-Device"
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onFinish,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("wizard_finish_btn")
        ) {
            Text(text = stringResource(R.string.wizard_get_started), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}
