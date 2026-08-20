package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.DailyWeather
import com.example.data.domain.AppLanguage
import com.example.data.domain.AppStrings
import com.example.data.domain.ClothingEngine
import com.example.ui.theme.CoralOrange
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun WeeklyOutfitForecast(
    daily: DailyWeather?,
    isCelsius: Boolean,
    language: AppLanguage = AppLanguage.EN,
    modifier: Modifier = Modifier
) {
    if (daily == null || daily.time.isEmpty()) return

    val daysCount = minOf(7, daily.time.size)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("weekly_outfit_forecast"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = AppStrings.getWeeklyOutlookHeader(language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            for (i in 0 until daysCount) {
                val dateRaw = daily.time.getOrNull(i) ?: ""
                val maxTemp = daily.temperatureMax.getOrNull(i) ?: 0.0
                val minTemp = daily.temperatureMin.getOrNull(i) ?: 0.0
                val code = daily.weatherCode.getOrNull(i) ?: 0

                val dayName = formatDayName(dateRaw, i, language)
                val summaryTip = ClothingEngine.getDailyForecastSummary(maxTemp, minTemp, code, language)

                val maxDisplay = if (isCelsius) "${maxTemp.toInt()}°" else "${((maxTemp * 9 / 5) + 32).toInt()}°"
                val minDisplay = if (isCelsius) "${minTemp.toInt()}°" else "${((minTemp * 9 / 5) + 32).toInt()}°"

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Day Label
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(90.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Summary Advice
                    Text(
                        text = summaryTip,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Temp Range
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = maxDisplay,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = CoralOrange
                        )
                        Text(
                            text = " / $minDisplay",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (i < daysCount - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

private fun formatDayName(rawDate: String, index: Int, language: AppLanguage): String {
    if (index == 0) {
        return when (language) {
            AppLanguage.DE -> "Heute"
            AppLanguage.FR -> "Aujourd'hui"
            AppLanguage.NL -> "Vandaag"
            AppLanguage.ES -> "Hoy"
            AppLanguage.EN -> "Today"
        }
    }
    if (index == 1) {
        return when (language) {
            AppLanguage.DE -> "Morgen"
            AppLanguage.FR -> "Demain"
            AppLanguage.NL -> "Morgen"
            AppLanguage.ES -> "Mañana"
            AppLanguage.EN -> "Tomorrow"
        }
    }
    return try {
        val locale = when (language) {
            AppLanguage.DE -> Locale.GERMAN
            AppLanguage.FR -> Locale.FRENCH
            AppLanguage.NL -> Locale("nl", "NL")
            AppLanguage.ES -> Locale("es", "ES")
            AppLanguage.EN -> Locale.ENGLISH
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = sdf.parse(rawDate)
        if (date != null) {
            val outFormat = SimpleDateFormat("EEEE", locale)
            outFormat.format(date).replaceFirstChar { it.uppercase() }
        } else {
            "Day ${index + 1}"
        }
    } catch (e: Exception) {
        "Day ${index + 1}"
    }
}
