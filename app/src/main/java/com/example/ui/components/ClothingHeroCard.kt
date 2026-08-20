package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.CurrentWeather
import com.example.data.domain.ClothingRecommendation
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.FreshMint
import com.example.ui.theme.VibrantIndigo

import androidx.compose.material.icons.filled.MyLocation

@Composable
fun ClothingHeroCard(
    cityName: String,
    countryName: String,
    weather: CurrentWeather,
    recommendation: ClothingRecommendation,
    isCelsius: Boolean,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onOpenSearch: () -> Unit,
    onToggleUnit: () -> Unit,
    onUseMyLocation: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val tempDisplay = if (isCelsius) {
        "${weather.temperature.toInt()}°C"
    } else {
        "${((weather.temperature * 9 / 5) + 32).toInt()}°F"
    }

    val feelsLikeDisplay = if (isCelsius) {
        "Feels like ${weather.apparentTemperature.toInt()}°C"
    } else {
        "Feels like ${((weather.apparentTemperature * 9 / 5) + 32).toInt()}°F"
    }

    val cardGradient = Brush.verticalGradient(
        colors = listOf(
            VibrantIndigo,
            Color(0xFF3F51B5),
            Color(0xFF303F9F)
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("clothing_hero_card"),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // City Name Pill
                    Surface(
                        onClick = onOpenSearch,
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White,
                        modifier = Modifier.testTag("city_selector_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                modifier = Modifier.size(18.dp),
                                tint = CoralOrange
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (countryName.isNotEmpty()) "$cityName, $countryName" else cityName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search City",
                                modifier = Modifier.size(16.dp),
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Action Controls
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (onUseMyLocation != null) {
                            // GPS My Location Button
                            IconButton(
                                onClick = onUseMyLocation,
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                    .size(38.dp)
                                    .testTag("my_location_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "Current GPS Location",
                                    tint = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        // Temperature Unit Toggle
                        Surface(
                            onClick = onToggleUnit,
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White,
                            modifier = Modifier.testTag("unit_toggle_button")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isCelsius) "°F" else "°C",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Bookmark Favorite Button
                        IconButton(
                            onClick = onToggleSave,
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                .size(38.dp)
                                .testTag("bookmark_button")
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark location",
                                tint = if (isSaved) CoralOrange else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mascot Graphic & Drip Score Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Mascot Image
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = recommendation.mascotResId),
                            contentDescription = "Mascot Outfit Illustration",
                            modifier = Modifier
                                .size(110.dp)
                                .padding(4.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Weather Stats & Drip Score Badge
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = tempDisplay,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = feelsLikeDisplay,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Text(
                            text = recommendation.weatherConditionName,
                            style = MaterialTheme.typography.labelLarge,
                            color = FreshMint,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Drip Score Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CoralOrange,
                            contentColor = Color.White
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Drip Score",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Drip Score: ${recommendation.dripScore}/100",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Verdict Title & Roast Quote Banner
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = recommendation.verdictTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = "Quote",
                                tint = CoralOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = recommendation.roastMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.95f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
