# OutfitCast — Weather-Driven Clothing & Style Advisor 🧥✨

**OutfitCast** is an intelligent, personality-infused Android weather application built with modern Kotlin and Jetpack Compose. Unlike conventional weather apps that merely display raw numbers and barometric charts, OutfitCast translates meteorological conditions—temperature, "feels-like" wind chill, precipitation, humidity, UV index, and wind gusts—into actionable, stylish, and entertaining clothing recommendations.

---

## 🎯 App Purpose

When people check the weather in the morning, the primary question on their mind is:  
> *"What should I actually wear today?"*

OutfitCast answers this instantly with:
1. **Actionable Layering Advice**: Clear breakdowns for Top, Bottom, Outerwear, Footwear, and Accessories.
2. **Humor & Personality**: Customizable stylist personas ranging from sassy roasts to overprotective parental advice.
3. **Survial & Practicality Metrics**: Quantified real-world metrics like *Sweat Hazard Level*, *Umbrella Necessity*, and *Wind Dishevel Risk*.
4. **Contextual Foresight**: Hourly micro-tips (e.g., *"Bring a hoodie for sunset"*) and 7-day outlooks to plan upcoming wardrobe choices.

---

## 📱 Key Features & Capabilities

- **Real-Time Global Weather Data**: Powered by the high-precision [Open-Meteo API](https://open-meteo.com/), providing real-time forecasts, 24-hour hourly progressions, and 7-day outlooks without requiring proprietary API keys.
- **Smart Clothing Recommendation Engine (`ClothingEngine`)**: A deterministic rules engine that analyzes temperature brackets, humidity-induced heat indexes, precipitation intensity, and wind speeds to construct balanced wardrobe combinations.
- **4 Distinct Stylist Personas**:
  - 💅 **Sassy & Roasting**: Unfiltered, meme-heavy commentary on your weather and fashion choices.
  - 🧣 **Overprotective Parent**: Warm, cautious reminders to never leave the house without a jacket and scarf.
  - 🕶️ **Dramatic Fashionista**: High-fashion critiques that balance runway aesthetics with weather survival.
  - 🧢 **Low-Key Bro**: Casual, minimalist, straight-to-the-point wardrobe tips.
- **Multi-Language Localization (5 Languages)**:
  - 🇺🇸 **English (EN)**
  - 🇩🇪 **Deutsch (DE)**
  - 🇫🇷 **Français (FR)**
  - 🇳🇱 **Nederlands (NL)**
  - 🇪🇸 **Español (ES)**
- **Global City Search & Bookmarks**: Fast search with debounced autocomplete and local persistent storage for saved favorite cities.
- **GPS Location Detection**: Seamless location resolution using Google Play Services `FusedLocationProviderClient` and Android `Geocoder`.
- **Drip Score (0–100) & Survival Indicators**: Fun ratings evaluating how comfortable and stylish you will be in current weather.
- **One-Tap Share**: Copy formatted outfit breakdowns and roast messages directly to the clipboard to share with friends or group chats.
- **Preferences & Custom App Icons**: Choose between Celsius and Fahrenheit, switch languages on the fly, and select your preferred app visual theme.

---

## 🚀 How to Use the App

### 1. Initial Setup & GPS Location
- On first launch, the app requests coarse/fine location permission to automatically resolve your current city.
- If permission is skipped, the app gracefully defaults to a default city (e.g., Amsterdam) or any previously viewed location.
- Tap the **GPS Button** on the top bar or inside the search sheet at any time to re-sync with your live GPS coordinates.

### 2. Reading Your Outfit Breakdown
- **Hero Card**: Displays the current temperature, feels-like indicator, headline verdict, and the stylist's commentary.
- **Recommended Outfit Checklist**: Provides specific garment recommendations categorized into:
  - **Top** (e.g., Breathable Linen Shirt, Thermal Long-Sleeve, Fleece Hoodie)
  - **Bottom** (e.g., Chinos, Cargo Pants, Corduroy Trousers)
  - **Outerwear** (e.g., Packable Rain Shell, Heavy Down Puffer, Denim Jacket)
  - **Footwear** (e.g., Waterproof Boots, Low-top Sneakers, Chelsea Boots)
  - **Essential Accessories** (e.g., UV400 Sunglasses, Compact Umbrella, Wool Beanie)

### 3. Switching Stylist Personas
- Use the horizontal **Persona Selector** below the hero card.
- Selecting any persona instantly updates the commentary, punchlines, and tips throughout the entire interface in real time.

### 4. Hourly Timeline & 7-Day Outlook
- **Hourly Dress Code**: Scroll horizontally across the next 24 hours to view changing temperatures and short micro-tips (e.g., *"Layer up by 7 PM"*).
- **7-Day Outlook**: Plan the week ahead with daily high/low temperatures and clothing summaries.

### 5. Searching & Saving Cities
- Tap the **Location Search Button** (magnifying glass) on the hero card.
- Type any city name to see live geocoding suggestions.
- Tap a city to load its weather.
- Tap the **Bookmark Icon** on the hero card to save the city to your favorites list for quick access.

### 6. Adjusting Preferences (Language, Units & Icons)
- Tap the **Palette/Language Icon** in the top header.
- In the **Preferences Sheet**, you can:
  - Select your preferred language (English, German, French, Dutch, Spanish).
  - Toggle between **Celsius (°C)** and **Fahrenheit (°F)**.
  - Preview and set your favorite custom app icon style.

### 7. Sharing Outfit Advice
- Tap the floating **Share Action Button (FAB)** in the bottom right corner to copy a formatted summary of your outfit advice to the clipboard.

---

## 🛠️ Architecture & Tech Stack

OutfitCast is built adhering to modern Android development standards, Clean Architecture, and Material Design 3 guidelines:

```
app/src/main/java/com/example/
├── data/
│   ├── api/                  # Open-Meteo REST API models & Retrofit client
│   │   ├── OpenMeteoApi.kt
│   │   └── WeatherModels.kt
│   ├── db/                   # Room database for saved favorite locations
│   │   ├── AppDatabase.kt
│   │   ├── LocationDao.kt
│   │   └── SavedLocationEntity.kt
│   ├── domain/               # Core business logic, engine & translations
│   │   ├── AdvicePersona.kt
│   │   ├── AppLanguage.kt
│   │   ├── AppStrings.kt     # Multi-language dictionary
│   │   └── ClothingEngine.kt # Layering & styling algorithm
│   ├── preferences/          # User preferences persistence manager
│   │   └── UserPreferencesManager.kt
│   └── repository/           # Single source of truth for weather & locations
│       └── WeatherRepository.kt
├── ui/
│   ├── components/           # Modular Jetpack Compose UI elements
│   │   ├── CitySearchSheet.kt
│   │   ├── ClothingHeroCard.kt
│   │   ├── FunnyStatsSection.kt
│   │   ├── HourlyOutfitTimeline.kt
│   │   ├── OutfitChecklistCard.kt
│   │   ├── PersonaSelector.kt
│   │   ├── PreferencesSheet.kt
│   │   └── WeeklyOutfitForecast.kt
│   ├── theme/                # Material 3 Color Schemes, Typography, Shapes
│   │   ├── Color.kt
│   │   └── Theme.kt
│   └── WeatherViewModel.kt   # State management & coroutine scopes
└── MainActivity.kt           # Edge-to-edge entry point & permission flow
```

### Technical Highlights:
- **UI Framework**: 100% Jetpack Compose with Material Design 3 (M3).
- **State Management**: Kotlin `StateFlow` and `collectAsStateWithLifecycle()` for lifecycle-aware UI state rendering.
- **Local Persistence**: 
  - **Room Database** (via KSP) for bookmarking favorite locations.
  - **SharedPreferences** for persisting selected language, temperature unit, active persona, and app icon settings across app restarts.
- **Networking**: Retrofit 2 + OkHttp + Moshi for type-safe, asynchronous JSON deserialization.
- **Location Services**: Google Play Services `FusedLocationProviderClient` with balanced power accuracy fallback.
- **Accessibility & Testability**: Complete `Modifier.testTag` tagging across all interactive components and minimum 48dp touch targets.

---

## 💡 How It Was Generated

This application was designed, engineered, and synthesized within **Google AI Studio** using the Gemini developer platform:

1. **Ideation & Domain Modeling**: Formulated the concept of a weather app that prioritizes human decision-making (clothing selection) over raw metrics, incorporating adaptive stylist personalities and survival indices.
2. **Deterministic Clothing Algorithm (`ClothingEngine`)**: Created algorithmic matrices that evaluate multiple weather dimensions (temperature brackets, wind gust thresholds, humidity/precipitation) to generate realistic multi-layer clothing suggestions.
3. **Multi-Tier Localization Architecture**: Structured `AppStrings` and domain enums to support instantaneous client-side translation across 5 major languages without external API latencies.
4. **Adaptive UI Design**: Implemented Material 3 color palettes featuring high-contrast Electric Blue, Coral Orange, Fresh Mint, and Deep Navy with rounded cards, edge-to-edge system insets, and responsive bottom sheets.
5. **Asset & Icon Generation**: Created custom adaptive icon variations and visual assets matching modern 3D claymorphic and minimalist design aesthetics.
6. **Robust Compilation & Verification**: Validated with Gradle Kotlin DSL and local Android compilation tools to ensure clean, error-free builds.

---

*Enjoy dressing for the weather with OutfitCast!* 👕🌦️
