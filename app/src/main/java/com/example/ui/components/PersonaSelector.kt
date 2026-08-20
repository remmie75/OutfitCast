package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.domain.AdvicePersona
import com.example.data.domain.AppLanguage
import com.example.data.domain.AppStrings

@Composable
fun PersonaSelector(
    selectedPersona: AdvicePersona,
    language: AppLanguage = AppLanguage.EN,
    onSelectPersona: (AdvicePersona) -> Unit,
    modifier: Modifier = Modifier
) {
    val sectionTitle = when (language) {
        AppLanguage.DE -> "Styling-Persönlichkeit"
        AppLanguage.FR -> "Personnalité Styliste"
        AppLanguage.NL -> "Stylist Persoonlijkheid"
        AppLanguage.ES -> "Personalidad de Estilo"
        AppLanguage.EN -> "Stylist Persona"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("persona_selector_section")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sectionTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "— ${AppStrings.getPersonaTagline(selectedPersona, language)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(AdvicePersona.values()) { persona ->
                val isSelected = persona == selectedPersona
                val icon: ImageVector = when (persona) {
                    AdvicePersona.SASSY -> Icons.Default.Face
                    AdvicePersona.PARENT -> Icons.Default.FamilyRestroom
                    AdvicePersona.FASHIONISTE -> Icons.Default.Style
                    AdvicePersona.BRO -> Icons.Default.ThumbUp
                }

                val localizedName = AppStrings.getPersonaDisplayName(persona, language)

                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectPersona(persona) },
                    label = {
                        Text(
                            text = localizedName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = localizedName,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("persona_chip_${persona.name}")
                )
            }
        }
    }
}
