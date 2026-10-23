package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CycleInsightSummary
import com.example.data.model.CyclePhase
import com.example.ui.theme.FertilePurple
import com.example.ui.theme.FollicularTeal
import com.example.ui.theme.LutealIndigo
import com.example.ui.theme.OvulationAmber
import com.example.ui.theme.PeriodCrimson
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CycleRing(
    insights: CycleInsightSummary,
    isDiscreetMode: Boolean,
    onQuickTogglePeriod: () -> Unit,
    onOpenLogger: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalDays = insights.totalCycleDays.coerceAtLeast(20)
    val currentDay = insights.cycleDay.coerceIn(1, totalDays + 5)
    val progress = (currentDay.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 900),
        label = "cycle_progress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(270.dp),
            contentAlignment = Alignment.Center
        ) {
            // Draw Circular Track and Phase Arcs
            val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            val periodLength = insights.avgPeriodLength
            val lutealStart = totalDays - 14

            Canvas(modifier = Modifier.size(240.dp)) {
                val strokeWidth = 18.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                val arcSize = Size(radius * 2, radius * 2)

                // Background Ring Track
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Period Arc (Start at top: -90 degrees)
                val periodSweep = (periodLength.toFloat() / totalDays) * 360f
                drawArc(
                    color = PeriodCrimson,
                    startAngle = -90f,
                    sweepAngle = periodSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Fertile Window Arc
                val fertileDayStart = (totalDays - 14) - 5
                val fertileStartAngle = -90f + (fertileDayStart.toFloat() / totalDays) * 360f
                val fertileSweep = (6f / totalDays) * 360f
                drawArc(
                    color = FertilePurple.copy(alpha = 0.75f),
                    startAngle = fertileStartAngle,
                    sweepAngle = fertileSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )

                // Ovulation Peak Mark
                val ovDay = totalDays - 14
                val ovAngle = -90f + (ovDay.toFloat() / totalDays) * 360f
                drawArc(
                    color = OvulationAmber,
                    startAngle = ovAngle - 2f,
                    sweepAngle = 4f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth + 4.dp.toPx())
                )

                // Indicator dot for current cycle day
                val angleRad = Math.toRadians((-90.0 + (animatedProgress * 360.0)))
                val center = Offset(size.width / 2, size.height / 2)
                val dotX = center.x + (radius * cos(angleRad)).toFloat()
                val dotY = center.y + (radius * sin(angleRad)).toFloat()

                // Dot Outer Halo
                drawCircle(
                    color = Color.White,
                    radius = 12.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
                // Dot Core
                drawCircle(
                    color = when (insights.currentPhase) {
                        CyclePhase.MENSTRUAL -> PeriodCrimson
                        CyclePhase.FOLLICULAR -> FollicularTeal
                        CyclePhase.OVULATION -> OvulationAmber
                        CyclePhase.LUTEAL -> LutealIndigo
                    },
                    radius = 8.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
            }

            // Dial Center Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                // Phase Badge
                val phaseTitle = if (isDiscreetMode) {
                    when (insights.currentPhase) {
                        CyclePhase.MENSTRUAL -> "Flow Phase"
                        CyclePhase.FOLLICULAR -> "Phase I"
                        CyclePhase.OVULATION -> "Peak Phase"
                        CyclePhase.LUTEAL -> "Phase II"
                    }
                } else {
                    insights.currentPhase.title
                }

                Surface(
                    color = when (insights.currentPhase) {
                        CyclePhase.MENSTRUAL -> PeriodCrimson.copy(alpha = 0.12f)
                        CyclePhase.FOLLICULAR -> FollicularTeal.copy(alpha = 0.12f)
                        CyclePhase.OVULATION -> OvulationAmber.copy(alpha = 0.15f)
                        CyclePhase.LUTEAL -> LutealIndigo.copy(alpha = 0.12f)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = phaseTitle,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = when (insights.currentPhase) {
                            CyclePhase.MENSTRUAL -> PeriodCrimson
                            CyclePhase.FOLLICULAR -> FollicularTeal
                            CyclePhase.OVULATION -> OvulationAmber
                            CyclePhase.LUTEAL -> LutealIndigo
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Big Day Number
                Text(
                    text = "Day ${insights.cycleDay}",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Subtitle / Next Period countdown
                val subtitleText = when {
                    insights.isPeriodActiveToday -> "Menstruation active"
                    insights.daysUntilPeriod > 0 -> "Period in ${insights.daysUntilPeriod} days"
                    insights.daysUntilPeriod == 0 -> "Period expected today"
                    else -> "Period late by ${-insights.daysUntilPeriod} days"
                }

                Text(
                    text = subtitleText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Fertility indicator pill
                if (!isDiscreetMode) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = FertilePurple.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = if (insights.pregnancyChance.level == "Peak" || insights.pregnancyChance.level == "High") {
                                        OvulationAmber
                                    } else {
                                        FertilePurple
                                    },
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${insights.pregnancyChance.level} Fertility",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FertilePurple
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Action Buttons
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onQuickTogglePeriod,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (insights.isPeriodActiveToday) PeriodCrimson else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (insights.isPeriodActiveToday) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("quick_period_toggle_button")
            ) {
                Icon(
                    imageVector = if (insights.isPeriodActiveToday) Icons.Default.Check else Icons.Default.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (insights.isPeriodActiveToday) "Period Logged" else "Log Period",
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedButton(
                onClick = onOpenLogger,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("open_symptom_logger_button")
            ) {
                Text(
                    text = "Log Symptoms",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
