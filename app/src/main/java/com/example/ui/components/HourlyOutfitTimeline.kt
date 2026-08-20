package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.HourlyWeather
import com.example.data.domain.AppLanguage
import com.example.data.domain.AppStrings
import com.example.data.domain.ClothingEngine
import com.example.ui.theme.FreshMint
import com.example.ui.theme.VibrantIndigo

@Composable
fun HourlyOutfitTimeline(
    hourly: HourlyWeather?,
    isCelsius: Boolean,
    language: AppLanguage = AppLanguage.EN,
    modifier: Modifier = Modifier
) {
    if (hourly == null || hourly.time.isEmpty()) return

    val count = minOf(24, hourly.time.size)

    val subtitle = when (language) {
        AppLanguage.DE -> "Nächste 24 Stunden"
        AppLanguage.FR -> "Prochaines 24h"
        AppLanguage.NL -> "Volgende 24 uur"
        AppLanguage.ES -> "Próximas 24 horas"
        AppLanguage.EN -> "Next 24 Hours"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hourly_outfit_timeline")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = AppStrings.getHourlyDressCodeHeader(language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(count) { index ->
                val timeStr = hourly.time.getOrNull(index) ?: ""
                val temp = hourly.temperature.getOrNull(index) ?: 0.0
                val code = hourly.weatherCode.getOrNull(index) ?: 0

                val hourLabel = formatHourLabel(timeStr, index, language)
                val tempDisplay = if (isCelsius) "${temp.toInt()}°" else "${((temp * 9 / 5) + 32).toInt()}°"
                val microTip = ClothingEngine.getHourlyMicroTip(index, temp, code, language)

                Card(
                    modifier = Modifier
                        .width(130.dp)
                        .testTag("hourly_item_$index"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (index == 0) VibrantIndigo else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (index == 0) 4.dp else 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = hourLabel,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (index == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = tempDisplay,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (index == 0) FreshMint else MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (index == 0) Color.White.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = microTip,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (index == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatHourLabel(rawTime: String, index: Int, language: AppLanguage): String {
    if (index == 0) {
        return when (language) {
            AppLanguage.DE -> "Jetzt"
            AppLanguage.FR -> "Maintenant"
            AppLanguage.NL -> "Nu"
            AppLanguage.ES -> "Ahora"
            AppLanguage.EN -> "Now"
        }
    }
    return try {
        val parts = rawTime.split("T")
        if (parts.size > 1) {
            val hourMin = parts[1].split(":")
            val hour = hourMin[0].toInt()
            when {
                hour == 0 -> "12 AM"
                hour < 12 -> "${hour} AM"
                hour == 12 -> "12 PM"
                else -> "${hour - 12} PM"
            }
        } else {
            "+${index}h"
        }
    } catch (e: Exception) {
        "+${index}h"
    }
}
