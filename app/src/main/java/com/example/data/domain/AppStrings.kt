package com.example.data.domain

object AppStrings {

    fun getAppName(lang: AppLanguage = AppLanguage.EN): String = "OutfitCast"

    fun getAppTagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Wetter als ehrliche Outfit-Beratung"
        AppLanguage.FR -> "La météo en conseils vestimentaires"
        AppLanguage.NL -> "Het weer als hilarisch kledingadvies"
        AppLanguage.ES -> "El clima como consejos de ropa"
        AppLanguage.EN -> "Weather as Clothing Advice"
    }

    fun getAppSlogan(lang: AppLanguage): String = getAppTagline(lang)

    fun getLoadingTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Lokales Drip-Potenzial wird analysiert... 👕"
        AppLanguage.FR -> "Analyse du potentiel vestimentaire... 👕"
        AppLanguage.NL -> "Lokaal kledingadvies berekenen... 👕"
        AppLanguage.ES -> "Analizando potencial de estilo local... 👕"
        AppLanguage.EN -> "Analyzing local drip potential... 👕"
    }

    fun getLoadingMessage(lang: AppLanguage): String = getLoadingTitle(lang)

    fun getLoadingSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Langweilige Wetterdaten werden in humorvolle Outfit-Tipps verwandelt"
        AppLanguage.FR -> "Conversion des chiffres météo ennuyeux en conseils de style décalés"
        AppLanguage.NL -> "Saaie weerscijfers omzetten in hilarisch kledingadvies"
        AppLanguage.ES -> "Convirtiendo aburridos números del clima en consejos de vestimenta divertidos"
        AppLanguage.EN -> "Converting boring weather numbers into funny outfit advice"
    }

    fun getErrorTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Mode-Notfall! 🚨"
        AppLanguage.FR -> "Urgence Mode! 🚨"
        AppLanguage.NL -> "Kledingcrisis! 🚨"
        AppLanguage.ES -> "¡Emergencia de Estilo! 🚨"
        AppLanguage.EN -> "Fashion Emergency! 🚨"
    }

    fun getFashionEmergencyTitle(lang: AppLanguage): String = getErrorTitle(lang)

    fun getRetryButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Wetter erneut laden"
        AppLanguage.FR -> "Réessayer la météo"
        AppLanguage.NL -> "Weer opnieuw ophalen"
        AppLanguage.ES -> "Reintentar clima"
        AppLanguage.EN -> "Retry Weather Fetch"
    }

    fun getRetryLabel(lang: AppLanguage): String = getRetryButton(lang)

    fun getPersonaDisplayName(persona: AdvicePersona, lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> when (persona) {
            AdvicePersona.SASSY -> "Frech & Spöttisch"
            AdvicePersona.PARENT -> "Überfürsorgliche Eltern"
            AdvicePersona.FASHIONISTE -> "Dramatische Fashionista"
            AdvicePersona.BRO -> "Minimalistischer Kumpel"
        }
        AppLanguage.FR -> when (persona) {
            AdvicePersona.SASSY -> "Insolent & Piquant"
            AdvicePersona.PARENT -> "Parent Poule"
            AdvicePersona.FASHIONISTE -> "Fashionista Dramatique"
            AdvicePersona.BRO -> "Pote Relax"
        }
        AppLanguage.NL -> when (persona) {
            AdvicePersona.SASSY -> "Brutaal & Pittig"
            AdvicePersona.PARENT -> "Bezorgde Ouder"
            AdvicePersona.FASHIONISTE -> "Dramatische Fashionista"
            AdvicePersona.BRO -> "Nuchtere Gast"
        }
        AppLanguage.ES -> when (persona) {
            AdvicePersona.SASSY -> "Sarcástico & Picante"
            AdvicePersona.PARENT -> "Padre Protector"
            AdvicePersona.FASHIONISTE -> "Fashionista Dramática"
            AdvicePersona.BRO -> "Colega Práctico"
        }
        AppLanguage.EN -> persona.displayName
    }

    fun getPersonaTagline(persona: AdvicePersona, lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> when (persona) {
            AdvicePersona.SASSY -> "Pikante Meme-Sprüche & null Filter"
            AdvicePersona.PARENT -> "Hast du eine Jacke mit?! Du erkältest dich noch!"
            AdvicePersona.FASHIONISTE -> "Haute Couture oder modische Katastrophe?"
            AdvicePersona.BRO -> "Kurze Hose, Hoodie, fertig."
        }
        AppLanguage.FR -> when (persona) {
            AdvicePersona.SASSY -> "Commentaires tranchants sans aucun filtre"
            AdvicePersona.PARENT -> "Tu as pris une veste?! Ne va pas attraper froid!"
            AdvicePersona.FASHIONISTE -> "Haute couture ou désastre vestimentaire?"
            AdvicePersona.BRO -> "Short, sweat à capuche, réglé."
        }
        AppLanguage.NL -> when (persona) {
            AdvicePersona.SASSY -> "Scherpe grappen & geen blad voor de mond"
            AdvicePersona.PARENT -> "Heb je een jas mee?! Straks vat je kou!"
            AdvicePersona.FASHIONISTE -> "Haute couture of modieuze ramp?"
            AdvicePersona.BRO -> "Korte broek, hoodie, klaar."
        }
        AppLanguage.ES -> when (persona) {
            AdvicePersona.SASSY -> "Comentarios ácidos y sin filtro alguno"
            AdvicePersona.PARENT -> "¡¿Llevas una chaqueta?! ¡Que vas a coger frío!"
            AdvicePersona.FASHIONISTE -> "¿Haute couture o desastre total?"
            AdvicePersona.BRO -> "Pantalón corto, sudadera y listo."
        }
        AppLanguage.EN -> persona.tagline
    }

    fun getChecklistHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Empfohlener Outfit-Check 🧥"
        AppLanguage.FR -> "Checklist Tenue Conseillée 🧥"
        AppLanguage.NL -> "Aanbevolen Outfit Checklist 🧥"
        AppLanguage.ES -> "Checklist de Atuendo Recomendado 🧥"
        AppLanguage.EN -> "Recommended Outfit Check 🧥"
    }

    fun getChecklistSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Tippe auf ein Kleidungsstück, um es als getragen zu markieren"
        AppLanguage.FR -> "Appuyez sur un vêtement pour le marquer comme porté"
        AppLanguage.NL -> "Tik op een kledingstuk om het als gedragen af te vinken"
        AppLanguage.ES -> "Toca una prenda para marcarla como puesta"
        AppLanguage.EN -> "Tap items as you put them on to verify your drip"
    }

    fun getCategoryTop(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Oberteil"
        AppLanguage.FR -> "Haut"
        AppLanguage.NL -> "Bovenkleding"
        AppLanguage.ES -> "Parte Superior"
        AppLanguage.EN -> "Top"
    }

    fun getCategoryBottom(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Hose / Rock"
        AppLanguage.FR -> "Bas / Pantalon"
        AppLanguage.NL -> "Broek / Rok"
        AppLanguage.ES -> "Parte Inferior"
        AppLanguage.EN -> "Bottom"
    }

    fun getCategoryOuterwear(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Jacke / Mantel"
        AppLanguage.FR -> "Veste / Manteau"
        AppLanguage.NL -> "Jas / Buitenlaag"
        AppLanguage.ES -> "Abrigo / Chaqueta"
        AppLanguage.EN -> "Outerwear"
    }

    fun getCategoryFootwear(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Schuhe"
        AppLanguage.FR -> "Chaussures"
        AppLanguage.NL -> "Schoenen"
        AppLanguage.ES -> "Calzado"
        AppLanguage.EN -> "Footwear"
    }

    fun getCategoryAccessories(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Accessoires"
        AppLanguage.FR -> "Accessoires"
        AppLanguage.NL -> "Accessoires"
        AppLanguage.ES -> "Accesorios"
        AppLanguage.EN -> "Accessories"
    }

    fun getSurvivalMetricsHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Kleidungs-Überlebens-Metriken 📊"
        AppLanguage.FR -> "Métriques de Survie Vestimentaire 📊"
        AppLanguage.NL -> "Kleding Overlevingsstatistieken 📊"
        AppLanguage.ES -> "Métricas de Supervivencia de Ropa 📊"
        AppLanguage.EN -> "Clothing Survival Metrics 📊"
    }

    fun getHourlyHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Stündlicher Dresscode ⏰"
        AppLanguage.FR -> "Dress Code Heure par Heure ⏰"
        AppLanguage.NL -> "Dresscode per Uur ⏰"
        AppLanguage.ES -> "Código de Vestimenta por Hora ⏰"
        AppLanguage.EN -> "Hourly Dress Code ⏰"
    }

    fun getHourlyDressCodeHeader(lang: AppLanguage): String = getHourlyHeader(lang)

    fun getNext24Hours(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Nächste 24 Stunden"
        AppLanguage.FR -> "Prochaines 24 heures"
        AppLanguage.NL -> "Komende 24 uur"
        AppLanguage.ES -> "Próximas 24 horas"
        AppLanguage.EN -> "Next 24 Hours"
    }

    fun getWeeklyHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "7-Tage Outfit-Ausblick 📅"
        AppLanguage.FR -> "Perspectives Tenue sur 7 Jours 📅"
        AppLanguage.NL -> "7-Dagen Outfit Vooruitzicht 📅"
        AppLanguage.ES -> "Pronóstico de Atuendos 7 Días 📅"
        AppLanguage.EN -> "7-Day Outfit Outlook 📅"
    }

    fun getWeeklyOutlookHeader(lang: AppLanguage): String = getWeeklyHeader(lang)

    fun getSearchSheetTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Stadt finden 🌍"
        AppLanguage.FR -> "Trouver votre ville 🌍"
        AppLanguage.NL -> "Stad zoeken 🌍"
        AppLanguage.ES -> "Buscar Ciudad 🌍"
        AppLanguage.EN -> "Find Your City 🌍"
    }

    fun getSearchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Stadt eingeben (z.B. Berlin, Paris, Wien)"
        AppLanguage.FR -> "Entrez une ville (ex: Paris, Lyon, Bruxelles)"
        AppLanguage.NL -> "Typ een stad (bijv. Amsterdam, Utrecht, Antwerpen)"
        AppLanguage.ES -> "Escribe una ciudad (ej. Madrid, Barcelona, CDMX)"
        AppLanguage.EN -> "Type city name (e.g. Paris, Tokyo, London)"
    }

    fun getUseMyLocation(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Meinen aktuellen Standort nutzen (GPS)"
        AppLanguage.FR -> "Utiliser ma position actuelle (GPS)"
        AppLanguage.NL -> "Mijn huidige locatie gebruiken (GPS)"
        AppLanguage.ES -> "Usar mi ubicación actual (GPS)"
        AppLanguage.EN -> "Use My Current Location (GPS)"
    }

    fun getUseMyLocationLabel(lang: AppLanguage): String = getUseMyLocation(lang)

    fun getSavedCities(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Gespeicherte Städte 📌"
        AppLanguage.FR -> "Villes Enregistrées 📌"
        AppLanguage.NL -> "Opgeslagen Steden 📌"
        AppLanguage.ES -> "Ciudades Guardadas 📌"
        AppLanguage.EN -> "Saved Cities 📌"
    }

    fun getSavedCitiesTitle(lang: AppLanguage): String = getSavedCities(lang)

    fun getSearchResults(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Suchergebnisse"
        AppLanguage.FR -> "Résultats de recherche"
        AppLanguage.NL -> "Zoekresultaten"
        AppLanguage.ES -> "Resultados de búsqueda"
        AppLanguage.EN -> "Search Results"
    }

    fun getSearchResultsTitle(lang: AppLanguage): String = getSearchResults(lang)

    fun getPreferencesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Einstellungen & Personalisierung ⚙️"
        AppLanguage.FR -> "Préférences & Personnalisation ⚙️"
        AppLanguage.NL -> "Voorkeuren & Instellingen ⚙️"
        AppLanguage.ES -> "Preferencias & Ajustes ⚙️"
        AppLanguage.EN -> "Preferences & Settings ⚙️"
    }

    fun getLanguageSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "App-Sprache"
        AppLanguage.FR -> "Langue de l'application"
        AppLanguage.NL -> "Taal van de app"
        AppLanguage.ES -> "Idioma de la aplicación"
        AppLanguage.EN -> "App Language"
    }

    fun getTemperatureUnitSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Temperatureinheit"
        AppLanguage.FR -> "Unité de température"
        AppLanguage.NL -> "Temperatuureenheid"
        AppLanguage.ES -> "Unidad de temperatura"
        AppLanguage.EN -> "Temperature Unit"
    }

    fun getAppIconSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "App-Icon auswählen (3 Vorschläge)"
        AppLanguage.FR -> "Icône de l'application (3 suggestions)"
        AppLanguage.NL -> "Kies app-icoon (3 opties)"
        AppLanguage.ES -> "Elegir icono de app (3 sugerencias)"
        AppLanguage.EN -> "App Icon Style (Choose from 3)"
    }

    fun getCopiedToast(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Outfit-Tipp in die Zwischenablage kopiert! 📋"
        AppLanguage.FR -> "Conseil tenue copié dans le presse-papiers! 📋"
        AppLanguage.NL -> "Kledingadvies naar klembord gekopieerd! 📋"
        AppLanguage.ES -> "¡Consejo de atuendo copiado al portapapeles! 📋"
        AppLanguage.EN -> "Copied hilarious outfit advice to clipboard! 📋"
    }

    fun getPreferencesSavedToast(lang: AppLanguage): String = when (lang) {
        AppLanguage.DE -> "Einstellungen erfolgreich gespeichert! ✨"
        AppLanguage.FR -> "Préférences enregistrées avec succès! ✨"
        AppLanguage.NL -> "Voorkeuren succesvol opgeslagen! ✨"
        AppLanguage.ES -> "¡Preferencias guardadas exitosamente! ✨"
        AppLanguage.EN -> "Preferences saved successfully! ✨"
    }
}
