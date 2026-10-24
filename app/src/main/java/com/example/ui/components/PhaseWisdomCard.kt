package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.CyclePhase
import com.example.ui.theme.FollicularTeal
import com.example.ui.theme.LutealIndigo
import com.example.ui.theme.OvulationAmber
import com.example.ui.theme.PeriodCrimson

@Composable
fun PhaseWisdomCard(
    phase: CyclePhase,
    isDiscreetMode: Boolean,
    isAr: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val accentColor = when (phase) {
        CyclePhase.MENSTRUAL -> PeriodCrimson
        CyclePhase.FOLLICULAR -> FollicularTeal
        CyclePhase.OVULATION -> OvulationAmber
        CyclePhase.LUTEAL -> LutealIndigo
    }

    val displayTitle = if (isDiscreetMode) {
        if (isAr) {
            when (phase) {
                CyclePhase.MENSTRUAL -> "نصائح المرحلة الأولى"
                CyclePhase.FOLLICULAR -> "نصائح مرحلة النشاط"
                CyclePhase.OVULATION -> "نصائح مرحلة الذروة"
                CyclePhase.LUTEAL -> "نصائح مرحلة الهدوء"
            }
        } else {
            when (phase) {
                CyclePhase.MENSTRUAL -> "Flow Phase Insights"
                CyclePhase.FOLLICULAR -> "Phase I Insights"
                CyclePhase.OVULATION -> "Peak Phase Insights"
                CyclePhase.LUTEAL -> "Phase II Insights"
            }
        }
    } else {
        phase.localizedTitle(isAr)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .testTag("phase_wisdom_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = displayTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = phase.localizedSubtitle(isAr),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Energy Snapshot
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "⚡ الطاقة: " else "⚡ Energy: ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = phase.localizedEnergy(isAr),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (!isDiscreetMode) {
                        WisdomSection(
                            icon = "🧬",
                            title = if (isAr) "ديناميكية الهرمونات" else "Hormone Dynamics",
                            content = phase.localizedHormones(isAr)
                        )
                    }
                    WisdomSection(
                        icon = "🥗",
                        title = if (isAr) "التغذية والأطعمة الداعمة" else "Nourishment & Nutrition",
                        content = phase.localizedNutrition(isAr)
                    )
                    WisdomSection(
                        icon = "🧘‍♀️",
                        title = if (isAr) "النشاط البدني والراحة" else "Movement & Rest",
                        content = phase.localizedWorkout(isAr)
                    )
                }
            }

            if (!isExpanded) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isAr) "المسي لعرض نصائح الهرمونات، التغذية، والرياضة" else "Tap to view hormone, food & movement advice",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun WisdomSection(
    icon: String,
    title: String,
    content: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
        )
    }
}
