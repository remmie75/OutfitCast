package com.example.data.domain

import com.example.R
import com.example.data.api.CurrentWeather

enum class AdvicePersona(val displayName: String, val tagline: String) {
    SASSY("Sassy & Roasting", "Spicy meme commentary & zero filter"),
    PARENT("Overprotective Parent", "Did you bring a jacket?! Don't catch a chill!"),
    FASHIONISTE("Dramatic Fashioniste", "Haute couture or fashion disaster?"),
    BRO("Minimalist Bro", "Shorts, hoodie, done.")
}

data class FunnyStat(
    val label: String,
    val value: String,
    val iconName: String
)

data class ClothingRecommendation(
    val verdictTitle: String,
    val roastMessage: String,
    val dripScore: Int,
    val dripScoreTag: String,
    val topItem: String,
    val bottomItem: String,
    val outerwearItem: String,
    val footwearItem: String,
    val accessoryItems: List<String>,
    val mascotResId: Int,
    val weatherConditionName: String,
    val funnyStats: List<FunnyStat>
)

object ClothingEngine {

    fun generateRecommendation(
        weather: CurrentWeather,
        persona: AdvicePersona = AdvicePersona.SASSY,
        language: AppLanguage = AppLanguage.EN,
        isCelsius: Boolean = true
    ): ClothingRecommendation {
        val tempC = weather.temperature
        val apparentC = weather.apparentTemperature
        val code = weather.weatherCode
        val wind = weather.windSpeed
        val rain = weather.rain + weather.showers + weather.precipitation
        val snow = weather.snowfall
        val humidity = weather.relativeHumidity

        val isExtremeHeat = tempC >= 28.0 || apparentC >= 30.0
        val isWarm = tempC in 18.0..27.9
        val isMild = tempC in 12.0..17.9
        val isChilly = tempC in 5.0..11.9
        val isFreezing = tempC < 5.0

        val isRainy = rain > 0.1 || code in listOf(51, 53, 55, 61, 63, 65, 80, 81, 82, 95, 96, 99)
        val isSnowy = snow > 0.0 || code in listOf(71, 73, 75, 77, 85, 86)
        val isWindy = wind > 25.0

        // Select Mascot Graphic
        val mascotRes = when {
            isSnowy || isFreezing || (isChilly && wind > 20.0) -> R.drawable.img_mascot_cold_1785847445143
            isRainy -> R.drawable.img_mascot_rainy_1785847434395
            else -> R.drawable.img_mascot_sunny_1785847424547
        }

        // Weather Condition Name (Localized)
        val conditionName = decodeWeatherCode(code, rain, snow, language)

        // Funny stats (Localized)
        val funnyStats = generateFunnyStats(isExtremeHeat, isWarm, isRainy, isWindy, isFreezing, isChilly, isMild, language)

        return when (language) {
            AppLanguage.DE -> generateGermanAdvice(persona, tempC, apparentC, isRainy, isSnowy, isWindy, isExtremeHeat, conditionName, mascotRes, funnyStats)
            AppLanguage.FR -> generateFrenchAdvice(persona, tempC, apparentC, isRainy, isSnowy, isWindy, isExtremeHeat, conditionName, mascotRes, funnyStats)
            AppLanguage.NL -> generateDutchAdvice(persona, tempC, apparentC, isRainy, isSnowy, isWindy, isExtremeHeat, conditionName, mascotRes, funnyStats)
            AppLanguage.ES -> generateSpanishAdvice(persona, tempC, apparentC, isRainy, isSnowy, isWindy, isExtremeHeat, conditionName, mascotRes, funnyStats)
            AppLanguage.EN -> generateEnglishAdvice(persona, tempC, apparentC, isRainy, isSnowy, isWindy, isExtremeHeat, conditionName, mascotRes, funnyStats)
        }
    }

    private fun generateFunnyStats(
        isExtremeHeat: Boolean,
        isWarm: Boolean,
        isRainy: Boolean,
        isWindy: Boolean,
        isFreezing: Boolean,
        isChilly: Boolean,
        isMild: Boolean,
        lang: AppLanguage
    ): List<FunnyStat> {
        val list = mutableListOf<FunnyStat>()
        when (lang) {
            AppLanguage.DE -> {
                val sweat = if (isExtremeHeat) "GEFÄHRLICH (Sofort-Sauna)" else if (isWarm) "Mäßig (Achselschweiß-Alarm)" else "Gering (Furztrocken)"
                val umbrella = if (isWindy && isRainy) "3 Sekunden (Ruhe in Frieden)" else if (isRainy) "Überlebensfähig" else "Nutzlose Requisite"
                val jacket = if (isFreezing) "KRITISCH (Mind. 5 Schichten)" else if (isChilly) "Hoodie Pflicht" else if (isMild) "Optionaler Flex" else "Selbstsabotage"
                list.add(FunnyStat("Schweiß-Gefahr", sweat, "LocalFireDepartment"))
                list.add(FunnyStat("Schirm-Überleben", umbrella, "Umbrella"))
                list.add(FunnyStat("Jacken-Level", jacket, "Checkroom"))
            }
            AppLanguage.FR -> {
                val sweat = if (isExtremeHeat) "DANGEREUX (Sauna Immédiat)" else if (isWarm) "Modéré (Alerte Transpi)" else "Faible (Sec comme un os)"
                val umbrella = if (isWindy && isRainy) "3 Secondes (RIP Parapluie)" else if (isRainy) "Fonctionnel" else "Accessoire Inutile"
                val jacket = if (isFreezing) "CRITIQUE (5 Couches Min)" else if (isChilly) "Capuche Obligatoire" else if (isMild) "Style Optionnel" else "Auto-sabotage"
                list.add(FunnyStat("Risque Transpi", sweat, "LocalFireDepartment"))
                list.add(FunnyStat("Survie Parapluie", umbrella, "Umbrella"))
                list.add(FunnyStat("Niveau Veste", jacket, "Checkroom"))
            }
            AppLanguage.NL -> {
                val sweat = if (isExtremeHeat) "GEVAARLIJK (Directe Sauna)" else if (isWarm) "Matig (Okselwaarschuwing)" else "Laag (Kurkdroog)"
                val umbrella = if (isWindy && isRainy) "3 Seconden (RIP Paraplu)" else if (isRainy) "Volledig Functioneel" else "Overbodig Requisiet"
                val jacket = if (isFreezing) "KRITIEK (Minimaal 5 Lagen)" else if (isChilly) "Verplichte Hoodie" else if (isMild) "Optionele Flex" else "Zelfkwelling"
                list.add(FunnyStat("Zweet Gevaar", sweat, "LocalFireDepartment"))
                list.add(FunnyStat("Paraplu Overleving", umbrella, "Umbrella"))
                list.add(FunnyStat("Jas Noodzaak", jacket, "Checkroom"))
            }
            AppLanguage.ES -> {
                val sweat = if (isExtremeHeat) "PELIGROSO (Sauna al Instante)" else if (isWarm) "Moderado (Alerta de Sudor)" else "Bajo (Seco total)"
                val umbrella = if (isWindy && isRainy) "3 Segundos (DEP Paraguas)" else if (isRainy) "Totalmente Útil" else "Accesorio Innecesario"
                val jacket = if (isFreezing) "CRÍTICO (Mínimo 5 Capas)" else if (isChilly) "Sudadera Obligatoria" else if (isMild) "Opcional con Estilo" else "Autosabotaje"
                list.add(FunnyStat("Riesgo de Sudor", sweat, "LocalFireDepartment"))
                list.add(FunnyStat("Vida de Paraguas", umbrella, "Umbrella"))
                list.add(FunnyStat("Nivel de Abrigo", jacket, "Checkroom"))
            }
            AppLanguage.EN -> {
                val sweat = if (isExtremeHeat) "DANGEROUS (Instant Sauna)" else if (isWarm) "Moderate (Armpit Warning)" else "Low (Dry as a Bone)"
                val umbrella = if (isWindy && isRainy) "3 Seconds (RIP Umbrella)" else if (isRainy) "Fully Functional" else "Unnecessary Prop"
                val jacket = if (isFreezing) "CRITICAL (5 Layers Min)" else if (isChilly) "Mandatory Hoodie" else if (isMild) "Optional Flex" else "Self-Sabotage"
                list.add(FunnyStat("Sweat Danger", sweat, "LocalFireDepartment"))
                list.add(FunnyStat("Umbrella Survival", umbrella, "Umbrella"))
                list.add(FunnyStat("Jacket Level", jacket, "Checkroom"))
            }
        }
        return list
    }

    private fun generateEnglishAdvice(
        persona: AdvicePersona,
        tempC: Double,
        apparentC: Double,
        isRainy: Boolean,
        isSnowy: Boolean,
        isWindy: Boolean,
        isExtremeHeat: Boolean,
        conditionName: String,
        mascotRes: Int,
        funnyStats: List<FunnyStat>
    ): ClothingRecommendation {
        return when (persona) {
            AdvicePersona.SASSY -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Sauna Simulator 3000 🔥",
                    roastMessage = "It's absurdly hot outside. Wear as little as legally allowed. Wearing heavy denim today is a form of emotional self-harm.",
                    dripScore = 88,
                    dripScoreTag = "Sweat flex energy",
                    topItem = "Ultra-breathable linen tee or tank",
                    bottomItem = "Weightless shorts (no skinny jeans!)",
                    outerwearItem = "ABSOLUTELY NONE. Leave it at home.",
                    footwearItem = "Airy slides or ventilated sneakers",
                    accessoryItems = listOf("Dark Sunglasses", "Handheld Mini Fan", "Gallon of Water"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Trash Bag Chic 🌧️",
                    roastMessage = "Sky is leaking aggressively. Put on waterproof armor or prepare to look like a wet golden retriever by noon.",
                    dripScore = 65,
                    dripScoreTag = "Damp survivalist",
                    topItem = "Quick-dry shirt",
                    bottomItem = "Water-resistant pants",
                    outerwearItem = "Heavy-duty raincoat or waterproof windbreaker",
                    footwearItem = "Rubber boots or beat-up sneakers you don't care about",
                    accessoryItems = listOf("Sturdy Umbrella", "Waterproof Bag", "Extra dry socks"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isSnowy || tempC < 5.0 -> ClothingRecommendation(
                    verdictTitle = "Human Marshmallow Mode ❄️",
                    roastMessage = "Biting frost. Channel your inner Michelin Man. If you can move your arms freely, you haven't worn enough layers.",
                    dripScore = 94,
                    dripScoreTag = "Maximum cozy",
                    topItem = "Thermal base layer + fleece sweater",
                    bottomItem = "Thermal leggings under insulated pants",
                    outerwearItem = "Massive oversized puffer coat",
                    footwearItem = "Insulated snow boots with thick wool socks",
                    accessoryItems = listOf("Beanie", "Fuzzy Scarf", "Oven-mitt tier Gloves"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isWindy -> ClothingRecommendation(
                    verdictTitle = "Parachute Protocol 🌬️",
                    roastMessage = "Wind is trying to blow you into another dimension. Avoid wide capes unless you plan on taking flight.",
                    dripScore = 78,
                    dripScoreTag = "Aerodynamic chic",
                    topItem = "Fitted hoodie or crewneck",
                    bottomItem = "Tapered joggers (no loose skirts)",
                    outerwearItem = "Fitted windbreaker jacket",
                    footwearItem = "Heavy sturdy boots or grip sneakers",
                    accessoryItems = listOf("Hair ties / Cap", "Lip balm", "Gravity"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC in 15.0..24.0 -> ClothingRecommendation(
                    verdictTitle = "Main Character Flex 🕶️",
                    roastMessage = "Peak perfection weather! You can literally wear anything and look like you're in a clothing commercial. Go shine.",
                    dripScore = 99,
                    dripScoreTag = "Absolute perfection",
                    topItem = "Stylish oversized t-shirt or crisp button-down",
                    bottomItem = "Relaxed chinos or classic denim",
                    outerwearItem = "Light denim jacket (optional shoulder drape)",
                    footwearItem = "Clean white sneakers",
                    accessoryItems = listOf("Designer Sunglasses", "Watch", "Unmatched confidence"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Classic Hoodie Weather ☕",
                    roastMessage = "A bit nippy! Grab a hoodie and cozy up. The classic 'I tried without trying too hard' aesthetic.",
                    dripScore = 85,
                    dripScoreTag = "Cozy minimalist",
                    topItem = "Soft cotton t-shirt",
                    bottomItem = "Comfy jeans or cargo trousers",
                    outerwearItem = "Your favorite heavy hoodie or fleece cardigan",
                    footwearItem = "Daily driver sneakers",
                    accessoryItems = listOf("Hot Coffee", "Beanie (optional)"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.PARENT -> when {
                isRainy -> ClothingRecommendation(
                    verdictTitle = "DID YOU BRING AN UMBRELLA?! ☔",
                    roastMessage = "Don't you dare step outside without a proper raincoat and boots! Do you want to catch pneumonia?! Put a sweater underneath!",
                    dripScore = 100,
                    dripScoreTag = "Parent approved",
                    topItem = "Long sleeve shirt tucked in",
                    bottomItem = "Warm pants",
                    outerwearItem = "Thick hooded raincoat",
                    footwearItem = "Waterproof rain boots with thick wool socks",
                    accessoryItems = listOf("Big umbrella", "Tissues", "Hand sanitizer"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC < 12.0 -> ClothingRecommendation(
                    verdictTitle = "ZIP THAT JACKET ALL THE WAY UP! 🧥",
                    roastMessage = "Your neck is exposed! Where is your scarf?! Eat a hot meal before you leave and take a thermos of tea with you!",
                    dripScore = 95,
                    dripScoreTag = "100% Warmth guaranteed",
                    topItem = "Thermal undershirt + thick sweater",
                    bottomItem = "Long underwear + thick trousers",
                    outerwearItem = "Heavy winter coat zipped to the chin",
                    footwearItem = "Warm winter boots",
                    accessoryItems = listOf("Wool scarf", "Gloves", "Thermos of soup"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "DRINK WATER & WEAR A HAT! ☀️",
                    roastMessage = "It is blazing! Don't forget sunscreen on your ears! Stay in the shade and don't run around in the sun like crazy!",
                    dripScore = 90,
                    dripScoreTag = "Hydrated & Protected",
                    topItem = "Light colored cotton t-shirt",
                    bottomItem = "Comfortable shorts",
                    outerwearItem = "No jacket needed, but keep a hat on!",
                    footwearItem = "Sensible walking shoes",
                    accessoryItems = listOf("Wide brim sun hat", "SPF 70 Sunscreen", "Ice water bottle"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Take a light jacket just in case! 🧥",
                    roastMessage = "It feels fine now, but what if the wind picks up later? Always carry a light jacket. Better safe than sorry dear!",
                    dripScore = 88,
                    dripScoreTag = "Prepared for anything",
                    topItem = "Comfortable shirt",
                    bottomItem = "Jeans",
                    outerwearItem = "Light cardigan or windbreaker in your bag",
                    footwearItem = "Comfortable sneakers",
                    accessoryItems = listOf("Light jacket", "Snack", "Water"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.FASHIONISTE -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Resort Runway Realness 🌴",
                    roastMessage = "High fashion summer statement: Oversized linen silhouette with high-end sun visors. Sweat is just haute couture glow.",
                    dripScore = 98,
                    dripScoreTag = "Vogue Summer Issue",
                    topItem = "Silk-blend camp collar shirt",
                    bottomItem = "Pleated wide-leg linen trousers",
                    outerwearItem = "Draped silk kimono (pure aesthetic)",
                    footwearItem = "Italian leather slides",
                    accessoryItems = listOf("Cat-eye Sunglasses", "Canvas Tote", "Glow Mist"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Dramatic Noir Trenchcoat 🎬",
                    roastMessage = "Turn the drizzle into a music video shoot. A dramatic floor-length trench coat and chunky leather boots will command respect.",
                    dripScore = 96,
                    dripScoreTag = "Dramatic protagonist",
                    topItem = "Fitted turtleneck sweater",
                    bottomItem = "Tailored dark trousers",
                    outerwearItem = "Double-breasted vinyl trench coat",
                    footwearItem = "Chunky platform waterproof boots",
                    accessoryItems = listOf("Monochrome umbrella", "Leather gloves"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC < 10.0 -> ClothingRecommendation(
                    verdictTitle = "Avant-Garde Layering ❄️",
                    roastMessage = "Cold weather is an opportunity for dramatic texture layering. Mix cashmere, leather, and faux-fur like a Parisian icon.",
                    dripScore = 99,
                    dripScoreTag = "Fashion Week VIP",
                    topItem = "High-neck wool knit",
                    bottomItem = "Leather pants or heavy wool trousers",
                    outerwearItem = "Statement oversized teddy coat",
                    footwearItem = "Pointed-toe leather boots",
                    accessoryItems = listOf("Cashmere scarf", "Beret / Statement hat"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Streetwear Royalty 👟",
                    roastMessage = "Moderate climate means supreme drip flexibility. Pair structured outerwear with effortless streetwear accents.",
                    dripScore = 97,
                    dripScoreTag = "Streetstyle Icon",
                    topItem = "Graphic vintage tee",
                    bottomItem = "Raw denim jeans",
                    outerwearItem = "Structured bomber or leather jacket",
                    footwearItem = "Limited edition retro sneakers",
                    accessoryItems = listOf("Crossbody bag", "Tinted lenses"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.BRO -> when {
                tempC >= 15.0 -> ClothingRecommendation(
                    verdictTitle = "Shorts + Hoodie Combo 🤙",
                    roastMessage = "Is it shorts weather? Always. Toss on a hoodie, grab your flip flops or beaters, and you're good for the day bro.",
                    dripScore = 80,
                    dripScoreTag = "Chill Bro Standard",
                    topItem = "Plain black t-shirt",
                    bottomItem = "Athletic shorts (regardless of wind)",
                    outerwearItem = "Gray zip-up hoodie",
                    footwearItem = "Slide sandals or everyday sneakers",
                    accessoryItems = listOf("Baseball cap backwards", "Protein shaker"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Sweatpants & Jacket Bro 👊",
                    roastMessage = "It's cold bro. Put on sweatpants and a heavy zip hoodie. No need to overthink it.",
                    dripScore = 82,
                    dripScoreTag = "Low effort peak comfort",
                    topItem = "Heavyweight t-shirt",
                    bottomItem = "Cozy fleece sweatpants",
                    outerwearItem = "Puffer jacket or thick fleece",
                    footwearItem = "Comfy sneakers",
                    accessoryItems = listOf("Beanie", "Keyring on belt loop"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
        }
    }

    private fun generateGermanAdvice(
        persona: AdvicePersona,
        tempC: Double,
        apparentC: Double,
        isRainy: Boolean,
        isSnowy: Boolean,
        isWindy: Boolean,
        isExtremeHeat: Boolean,
        conditionName: String,
        mascotRes: Int,
        funnyStats: List<FunnyStat>
    ): ClothingRecommendation {
        return when (persona) {
            AdvicePersona.SASSY -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Sauna-Simulator 3000 🔥",
                    roastMessage = "Es ist absurd heiß draußen. Zieh so wenig an, wie rechtlich erlaubt ist. Schwere Jeans sind heute seelische Selbstverletzung.",
                    dripScore = 88,
                    dripScoreTag = "Schwitz-Flex-Energie",
                    topItem = "Hauchdünnes Leinen-Shirt oder Tanktop",
                    bottomItem = "Federleichte Shorts (keine Skinny Jeans!)",
                    outerwearItem = "ABSOLUT GAR NICHTS. Lass es daheim.",
                    footwearItem = "Luftige Schlappen oder atmungsaktive Sneaker",
                    accessoryItems = listOf("Dunkle Sonnenbrille", "Handventilator", "Wasserflasche"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Müllsack-Chic 🌧️",
                    roastMessage = "Der Himmel weint hemmungslos. Zieh wasserdichte Rüstung an oder sieh bis mittags aus wie ein begossener Pudel.",
                    dripScore = 65,
                    dripScoreTag = "Nasser Überlebenskämpfer",
                    topItem = "Schnelltrocknendes Shirt",
                    bottomItem = "Wasserabweisende Hose",
                    outerwearItem = "Robuste Regenjacke oder Hardshell-Windbreaker",
                    footwearItem = "Gummistiefel oder alte Treter",
                    accessoryItems = listOf("Sturmfester Schirm", "Wasserdichte Tasche", "Ersatzsocken"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isSnowy || tempC < 5.0 -> ClothingRecommendation(
                    verdictTitle = "Menschliches Marshmallow ❄️",
                    roastMessage = "Klirrende Kälte! Wenn du deine Arme noch frei bewegen kannst, hast du schlicht nicht genug Schichten an.",
                    dripScore = 94,
                    dripScoreTag = "Maximale Gemütlichkeit",
                    topItem = "Thermo-Funktionsunterwäsche + Fleece-Pulli",
                    bottomItem = "Thermo-Leggings unter gefütterter Hose",
                    outerwearItem = "Riesige Daunen-Puffer-Jacke",
                    footwearItem = "Gefütterte Winterstiefel & Wollsocken",
                    accessoryItems = listOf("Wollmütze", "Kuschelschal", "Dicke Fäustlinge"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isWindy -> ClothingRecommendation(
                    verdictTitle = "Fallschirm-Protokoll 🌬️",
                    roastMessage = "Der Wind will dich in eine andere Dimension pusten. Verzichte auf weite Umhänge, es sei denn, du willst abheben.",
                    dripScore = 78,
                    dripScoreTag = "Aerodynamischer Chic",
                    topItem = "Eng anliegender Hoodie",
                    bottomItem = "Engere Jogginghose (kein weiter Rock)",
                    outerwearItem = "Taillierte Windjacke",
                    footwearItem = "Feste Boots mit gutem Grip",
                    accessoryItems = listOf("Haargummi / Cap", "Lippenbalsam", "Schwerkraft"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC in 15.0..24.0 -> ClothingRecommendation(
                    verdictTitle = "Hauptcharakter-Energie 🕶️",
                    roastMessage = "Absolutes Traumwetter! Du kannst buchstäblich alles tragen und siehst aus wie im Modemagazin. Geh glänzen!",
                    dripScore = 99,
                    dripScoreTag = "Absolute Perfektion",
                    topItem = "Lässiges Oversized-T-Shirt oder Hemd",
                    bottomItem = "Chinos oder klassische Denim-Jeans",
                    outerwearItem = "Leichte Jeansjacke über der Schulter",
                    footwearItem = "Frische weiße Sneaker",
                    accessoryItems = listOf("Designer-Sonnenbrille", "Uhr", "Unendliches Selbstvertrauen"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Klassisches Hoodie-Wetter ☕",
                    roastMessage = "Etwas frisch! Schnapp dir deinen Lieblings-Kapuzenpullover. Gemütlich und stylisch ohne großen Aufwand.",
                    dripScore = 85,
                    dripScoreTag = "Gemütlicher Minimalismus",
                    topItem = "Weiches Baumwoll-T-Shirt",
                    bottomItem = "Bequeme Jeans oder Cargo-Hose",
                    outerwearItem = "Dein schwerer Lieblings-Hoodie",
                    footwearItem = "Alltags-Sneaker",
                    accessoryItems = listOf("Heißer Kaffee", "Beanie-Mütze"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.PARENT -> when {
                isRainy -> ClothingRecommendation(
                    verdictTitle = "HAST DU EINEN SCHIRM DABEI?! ☔",
                    roastMessage = "Wage es nicht ohne Regenjacke und feste Schuhe raus! Willst du dir eine Lungenentzündung holen?! Zieh einen Pullover drunter!",
                    dripScore = 100,
                    dripScoreTag = "Eltern-Geprüft",
                    topItem = "Langarmshirt ordentlich eingesteckt",
                    bottomItem = "Warme Hose",
                    outerwearItem = "Dicke Regenjacke mit Kapuze",
                    footwearItem = "Wasserdichte Stiefel mit Wollsocken",
                    accessoryItems = listOf("Großer Schirm", "Taschentücher", "Desinfektionsmittel"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC < 12.0 -> ClothingRecommendation(
                    verdictTitle = "JACKE BIS OBEN ZUMACHEN! 🧥",
                    roastMessage = "Dein Hals ist ganz frei! Wo ist dein Schal?! Iss was Warmes und nimm eine Thermoskanne Tee mit!",
                    dripScore = 95,
                    dripScoreTag = "100% Wärmegarantie",
                    topItem = "Thermo-Unterhemd + dicker Strickpullover",
                    bottomItem = "Lange Unterhose + warme Hose",
                    outerwearItem = "Schwerer Wintermantel bis zum Kinn zugezogen",
                    footwearItem = "Warme Winterstiefel",
                    accessoryItems = listOf("Wollschal", "Handschuhe", "Thermoskanne"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "TRINK WASSER & SETZ EINE KAPPE AUF! ☀️",
                    roastMessage = "Es brennt förmlich! Vergiss nicht die Sonnencreme auf den Ohren! Bleib im Schatten und renn nicht in der Hitze rum!",
                    dripScore = 90,
                    dripScoreTag = "Hydriert & Geschützt",
                    topItem = "Helles Baumwoll-T-Shirt",
                    bottomItem = "Bequeme Shorts",
                    outerwearItem = "Keine Jacke, aber Sonnenhut auf!",
                    footwearItem = "Bequeme Wanderschuhe",
                    accessoryItems = listOf("Sonnenhut", "Sonnencreme LSF 50+", "Eiswasser"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Nimm lieber eine Jacke mit! 🧥",
                    roastMessage = "Jetzt ist es mild, aber was wenn später Wind aufzieht? Sicher ist sicher, mein Schatz!",
                    dripScore = 88,
                    dripScoreTag = "Auf alles vorbereitet",
                    topItem = "Bequemes Shirt",
                    bottomItem = "Jeans",
                    outerwearItem = "Strickjacke oder Windjacke in der Tasche",
                    footwearItem = "Bequeme Sneaker",
                    accessoryItems = listOf("Leichte Jacke", "Apfel/Snack", "Wasser"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.FASHIONISTE -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Resort Runway Eleganz 🌴",
                    roastMessage = "High-Fashion Sommer-Statement: Weite Leinen-Silhouette mit Designer-Sonnenbrille. Schweiß ist nur Haute-Couture-Glow.",
                    dripScore = 98,
                    dripScoreTag = "Vogue Sommer-Ausgabe",
                    topItem = "Seidenmischung-Kurzarmhemd",
                    bottomItem = "Faltenhose aus leichtem Leinen",
                    outerwearItem = "Lockerer Seiden-Kimono (rein ästhetisch)",
                    footwearItem = "Italienische Leder-Pantoletten",
                    accessoryItems = listOf("Cat-Eye Sonnenbrille", "Canvas Tote Bag", "Feuchtigkeitsspray"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Dramatischer Noir Trenchcoat 🎬",
                    roastMessage = "Verwandle den Nieselregen in ein Musikvideo. Ein bodenlanger Trenchcoat und klobige Lederstiefel gebieten Respekt.",
                    dripScore = 96,
                    dripScoreTag = "Dramatische Hauptrolle",
                    topItem = "Enger Rollkragenpullover",
                    bottomItem = "Elegante schwarze Stoffhose",
                    outerwearItem = "Zweireihiger Lack-Trenchcoat",
                    footwearItem = "Chunky Plateau-Stiefel",
                    accessoryItems = listOf("Schwarzer Regenschirm", "Lederhandschuhe"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC < 10.0 -> ClothingRecommendation(
                    verdictTitle = "Avantgarde Layering ❄️",
                    roastMessage = "Kälte ist eine Chance für dramatisches Textur-Schichten. Kaschmir, Leder und Fake-Fur wie auf der Pariser Modewoche.",
                    dripScore = 99,
                    dripScoreTag = "Fashion Week VIP",
                    topItem = "Grobstrick-Pullover aus Wolle",
                    bottomItem = "Lederhose oder schwere Wollhose",
                    outerwearItem = "Extravaganter Oversize-Teddy-Mantel",
                    footwearItem = "Spitze Lederstiefeletten",
                    accessoryItems = listOf("Kaschmirschal", "Baskenmütze"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Streetwear Royalty 👟",
                    roastMessage = "Perfektes Übergangswetter für ultimativen Drip. Kombiniere strukturierte Jacken mit lässigen Sneakern.",
                    dripScore = 97,
                    dripScoreTag = "Streetstyle Ikone",
                    topItem = "Vintage Grafik-Tee",
                    bottomItem = "Raw Denim Jeans",
                    outerwearItem = "Bomberjacke oder Lederjacke",
                    footwearItem = "Limitierte Retro-Sneaker",
                    accessoryItems = listOf("Crossbody Bag", "Getönte Sonnenbrille"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.BRO -> when {
                tempC >= 15.0 -> ClothingRecommendation(
                    verdictTitle = "Shorts + Hoodie Kombi 🤙",
                    roastMessage = "Ist es Zeit für kurze Hosen? Immer! Hoodie drüber, Latschen oder Sneaker an und der Tag gehört dir, Bro.",
                    dripScore = 80,
                    dripScoreTag = "Chill Bro Standard",
                    topItem = "Schlichtes schwarzes T-Shirt",
                    bottomItem = "Sport-Shorts (egal wie der Wind weht)",
                    outerwearItem = "Grauer Reißverschluss-Hoodie",
                    footwearItem = "Adiletten oder Alltags-Sneaker",
                    accessoryItems = listOf("Baseballcap verkehrt herum", "Protein-Shaker"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Jogginghose & Jacke Bro 👊",
                    roastMessage = "Ist halt kalt, Bro. Jogginghose an, dicker Hoodie drüber, nicht lang nachdenken.",
                    dripScore = 82,
                    dripScoreTag = "Minimaler Aufwand, maximal bequem",
                    topItem = "Schweres Basic-Shirt",
                    bottomItem = "Gemütliche Fleece-Jogginghose",
                    outerwearItem = "Pufferjacke oder dicker Zipper",
                    footwearItem = "Bequeme Sneaker",
                    accessoryItems = listOf("Beanie Mütze", "Schlüsselbund am Hosenbund"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
        }
    }

    private fun generateFrenchAdvice(
        persona: AdvicePersona,
        tempC: Double,
        apparentC: Double,
        isRainy: Boolean,
        isSnowy: Boolean,
        isWindy: Boolean,
        isExtremeHeat: Boolean,
        conditionName: String,
        mascotRes: Int,
        funnyStats: List<FunnyStat>
    ): ClothingRecommendation {
        return when (persona) {
            AdvicePersona.SASSY -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Simulateur de Sauna 3000 🔥",
                    roastMessage = "Il fait une chaleur insensée. Portez le strict minimum légal. Mettre un jean épais aujourd'hui relève du masochisme.",
                    dripScore = 88,
                    dripScoreTag = "Énergie anti-sueur",
                    topItem = "T-shirt ultra-léger en lin ou débardeur",
                    bottomItem = "Short fluide et aéré (pas de slim!)",
                    outerwearItem = "ABSOLUMENT RIEN. Laissez tout chez vous.",
                    footwearItem = "Claquettes aérées ou baskets respirantes",
                    accessoryItems = listOf("Lunettes noires", "Mini ventilateur portable", "Bouteille d'eau"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Chic Sac Poubelle 🌧️",
                    roastMessage = "Le ciel pleure des trombes d'eau. Armure imperméable exigée, sinon vous ressemblerez à un caniche trempé à midi.",
                    dripScore = 65,
                    dripScoreTag = "Survivant de l'averse",
                    topItem = "T-shirt séchage rapide",
                    bottomItem = "Pantalon déperlant",
                    outerwearItem = "Imperméable costaud ou coupe-vent étanche",
                    footwearItem = "Bottes de pluie ou vieilles baskets tout-terrain",
                    accessoryItems = listOf("Parapluie résistant", "Sac étanche", "Chaussettes de rechange"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isSnowy || tempC < 5.0 -> ClothingRecommendation(
                    verdictTitle = "Mode Chamallow Humain ❄️",
                    roastMessage = "Froid mordant. Invoquez le bonhomme Michelin qui sommeille en vous. Si vous bougez encore les bras, il manque une couche.",
                    dripScore = 94,
                    dripScoreTag = "Confort polaire",
                    topItem = "Sous-vêtement thermique + pull polaire",
                    bottomItem = "Collant thermique sous pantalon épais",
                    outerwearItem = "Doudoune géante matelassée",
                    footwearItem = "Après-ski fourrés avec grosses chaussettes",
                    accessoryItems = listOf("Bonnet en laine", "Écharpe doudou", "Moufles de trappeur"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC in 15.0..24.0 -> ClothingRecommendation(
                    verdictTitle = "Énergie Personnage Principal 🕶️",
                    roastMessage = "Météo absolument parfaite! Vous pouvez porter n'importe quoi et ressembler à une pub de magazine. Brillez!",
                    dripScore = 99,
                    dripScoreTag = "Perfection absolue",
                    topItem = "T-shirt stylé oversize ou chemise fluide",
                    bottomItem = "Chino décontracté ou jean brut",
                    outerwearItem = "Veste en jean sur les épaules",
                    footwearItem = "Baskets blanches impeccables",
                    accessoryItems = listOf("Lunettes créateur", "Montre", "Charisme insolent"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Météo à Sweat à Capuche ☕",
                    roastMessage = "Un peu frisquet! Enfilez votre sweat préféré. Le look parfait du 'stylé sans effort'.",
                    dripScore = 85,
                    dripScoreTag = "Minimalisme douillet",
                    topItem = "T-shirt en coton doux",
                    bottomItem = "Jean confortable ou pantalon cargo",
                    outerwearItem = "Votre gros sweat à capuche favori",
                    footwearItem = "Baskets du quotidien",
                    accessoryItems = listOf("Café brûlant", "Petit bonnet"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.PARENT -> when {
                isRainy -> ClothingRecommendation(
                    verdictTitle = "AS-TU PRIS TON PARAPLUIE?! ☔",
                    roastMessage = "N'ose pas sortir sans ciré ni bottes! Tu veux attraper une pneumonie?! Mets un gilet en dessous!",
                    dripScore = 100,
                    dripScoreTag = "Approuvé par les parents",
                    topItem = "T-shirt manches longues rentré dans le pantalon",
                    bottomItem = "Pantalon chaud",
                    outerwearItem = "Ciré épais avec capuche",
                    footwearItem = "Bottes étanches avec grosses chaussettes",
                    accessoryItems = listOf("Grand parapluie", "Mouchoirs", "Gel antibactérien"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC < 12.0 -> ClothingRecommendation(
                    verdictTitle = "FERME TA VESTE JUSQU'EN HAUT! 🧥",
                    roastMessage = "Ton cou est tout découvert! Où est ton écharpe?! Mange chaud et emporte un thermos de thé!",
                    dripScore = 95,
                    dripScoreTag = "Chaleur 100% garantie",
                    topItem = "Maillot de corps thermique + gros tricot",
                    bottomItem = "Collant chaud + pantalon épais",
                    outerwearItem = "Gros manteau d'hiver boutonné jusqu'au menton",
                    footwearItem = "Bottes fourrées",
                    accessoryItems = listOf("Écharpe en laine", "Gants", "Thermos de soupe"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Prends une petite laine au cas où! 🧥",
                    roastMessage = "Il fait bon maintenant mais si le vent se lève? Mieux vaut prévenir que guérir mon chéri!",
                    dripScore = 88,
                    dripScoreTag = "Prêt à tout",
                    topItem = "Chemise confortable",
                    bottomItem = "Jean",
                    outerwearItem = "Gilet ou coupe-vent dans le sac",
                    footwearItem = "Chaussures confortables",
                    accessoryItems = listOf("Petite veste", "Goûter", "Bouteille d'eau"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.FASHIONISTE -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Allure Croisière & Défilé 🌴",
                    roastMessage = "Déclaration estivale haute couture : silhouette fluide en lin et lunettes surdimensionnées. La transpiration n'est qu'un éclat couture.",
                    dripScore = 98,
                    dripScoreTag = "Édition Été Vogue",
                    topItem = "Chemise col cubain en soie mélangée",
                    bottomItem = "Pantalon large plissé en lin",
                    outerwearItem = "Kimono en soie vaporeuse (pure esthétique)",
                    footwearItem = "Mules en cuir italien",
                    accessoryItems = listOf("Lunettes œil de chat", "Cabas en toile", "Brume hydratante"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Trench-coat Noir Dramatique 🎬",
                    roastMessage = "Transformez la pluie en scène de film. Un trench long et des bottes chunky imposent le respect sous l'orage.",
                    dripScore = 96,
                    dripScoreTag = "Protagoniste dramatique",
                    topItem = "Sous-pull à col roulé ajusté",
                    bottomItem = "Pantalon noir tailleur",
                    outerwearItem = "Trench en vinyle ceinturé",
                    footwearItem = "Bottes chunky à semelle épaisse",
                    accessoryItems = listOf("Parapluie noir mat", "Gants en cuir"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Noblesse du Streetwear 👟",
                    roastMessage = "Le climat tempéré offre une liberté de style infinie. Associez pièces structurées et baskets iconiques.",
                    dripScore = 97,
                    dripScoreTag = "Icône Streetstyle",
                    topItem = "T-shirt vintage graphique",
                    bottomItem = "Jean selvedge brut",
                    outerwearItem = "Bomber texturé ou veste en cuir",
                    footwearItem = "Sneakers rétro en édition limitée",
                    accessoryItems = listOf("Sac bandoulière", "Verres teintés"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.BRO -> when {
                tempC >= 15.0 -> ClothingRecommendation(
                    verdictTitle = "Combo Short + Hoodie 🤙",
                    roastMessage = "Est-ce la saison du short? Toujours. Enfile un hoodie, tes claquettes ou tes baskets et t'es refait pour la journée.",
                    dripScore = 80,
                    dripScoreTag = "Standard Chill",
                    topItem = "T-shirt noir basique",
                    bottomItem = "Short de sport (peu importe le vent)",
                    outerwearItem = "Sweat à capuche gris zippé",
                    footwearItem = "Claquettes ou baskets de tous les jours",
                    accessoryItems = listOf("Casquette à l'envers", "Shaker de protéines"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Survêt & Doudoune Bro 👊",
                    roastMessage = "Il pèle mon pote. Mets un bas de survêt et une bonne doudoune. Pas besoin de se prendre la tête.",
                    dripScore = 82,
                    dripScoreTag = "Effort zéro, confort max",
                    topItem = "T-shirt épais",
                    bottomItem = "Jogging en molleton tout doux",
                    outerwearItem = "Doudoune chaude ou polaire",
                    footwearItem = "Baskets confortables",
                    accessoryItems = listOf("Bonnet simple", "Clés au passant du pantalon"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
        }
    }

    private fun generateDutchAdvice(
        persona: AdvicePersona,
        tempC: Double,
        apparentC: Double,
        isRainy: Boolean,
        isSnowy: Boolean,
        isWindy: Boolean,
        isExtremeHeat: Boolean,
        conditionName: String,
        mascotRes: Int,
        funnyStats: List<FunnyStat>
    ): ClothingRecommendation {
        return when (persona) {
            AdvicePersona.SASSY -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Sauna Simulator 3000 🔥",
                    roastMessage = "Het is bloedheet buiten. Draag zo min mogelijk stof binnen de wet. Een zware spijkerbroek is vandaag pure zelfkastijding.",
                    dripScore = 88,
                    dripScoreTag = "Zweet-flex energie",
                    topItem = "Luchtig linnen shirt of hemdje",
                    bottomItem = "Vederlichte korte broek (geen skinny jeans!)",
                    outerwearItem = "HELEMAAL NIETS. Laat alles thuis.",
                    footwearItem = "Luchtige slippers of ademende sneakers",
                    accessoryItems = listOf("Zonnebril", "Handventilator", "Fles koud water"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Vuilniszakken Chic 🌧️",
                    roastMessage = "De hemel zeikt onophoudelijk. Trek een waterdichte uitrusting aan of zie er rond het middaguur uit als een verzopen kat.",
                    dripScore = 65,
                    dripScoreTag = "Doorweekte strijder",
                    topItem = "Sneldrogend shirt",
                    bottomItem = "Waterafstotende broek",
                    outerwearItem = "Degelijke regenjas of waterdichte windbreaker",
                    footwearItem = "Regenlaarzen of oude afgetrapte stappers",
                    accessoryItems = listOf("Stormparaplu", "Waterdichte tas", "Extra droge sokken"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isSnowy || tempC < 5.0 -> ClothingRecommendation(
                    verdictTitle = "Levende Marshmallow Modus ❄️",
                    roastMessage = "Snijdende kou. Tover je innerlijke Michelinmannetje tevoorschijn. Als je je armen nog kunt buigen, heb je te weinig lagen aan.",
                    dripScore = 94,
                    dripScoreTag = "Maximaal knus",
                    topItem = "Thermo-onderkleding + fleece trui",
                    bottomItem = "Thermo-legging onder een warme broek",
                    outerwearItem = "Gigantische puffer jas",
                    footwearItem = "Gevoerde winterlaarzen met dikke wollen sokken",
                    accessoryItems = listOf("Wollen muts", "Zachte sjaal", "Dikke wanten"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC in 15.0..24.0 -> ClothingRecommendation(
                    verdictTitle = "Hoofdrolspeler Energie 🕶️",
                    roastMessage = "Absoluut droomweer! Je kunt letterlijk alles dragen en eruitzien als in een modecommercial. Ga stralen!",
                    dripScore = 99,
                    dripScoreTag = "Absolute perfectie",
                    topItem = "Stijlvol oversized T-shirt of fris overhemd",
                    bottomItem = "Nette chino of klassieke jeans",
                    outerwearItem = "Spijkerjasje over de schouders",
                    footwearItem = "Schone witte sneakers",
                    accessoryItems = listOf("Design zonnebril", "Horloge", "Onstuitbaar zelfvertrouwen"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Klassiek Hoodie Weer ☕",
                    roastMessage = "Lekker frisjes! Pak je favoriete hoodie. De ideale 'stijlvol zonder moeite' uitstraling.",
                    dripScore = 85,
                    dripScoreTag = "Knus minimalisme",
                    topItem = "Zacht katoenen T-shirt",
                    bottomItem = "Fijne jeans of cargobroek",
                    outerwearItem = "Je favoriete dikke capuchontrui",
                    footwearItem = "Dagelijkse sneakers",
                    accessoryItems = listOf("Warme koffie", "Muts (optioneel)"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.PARENT -> when {
                isRainy -> ClothingRecommendation(
                    verdictTitle = "HEB JE EEN PARAPLU MEE?! ☔",
                    roastMessage = "Waag het niet naar buiten te gaan zonder fatsoenlijke regenjas en laarzen! Wil je longontsteking krijgen?! Doe een trui eronder!",
                    dripScore = 100,
                    dripScoreTag = "Ouder-Goedgekeurd",
                    topItem = "Shirt met lange mouwen in je broek gestopt",
                    bottomItem = "Warme broek",
                    outerwearItem = "Dikke regenjas met capuchon",
                    footwearItem = "Waterdichte laarzen met wollen sokken",
                    accessoryItems = listOf("Grote paraplu", "Zakdoekjes", "Handgel"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC < 12.0 -> ClothingRecommendation(
                    verdictTitle = "RITS DIE JAS HELEMAAL DICHT! 🧥",
                    roastMessage = "Je nek ligt helemaal bloot! Waar is je sjaal?! Eet een warme hap voor je gaat en neem een thermoskan thee mee!",
                    dripScore = 95,
                    dripScoreTag = "100% Warmtegarantie",
                    topItem = "Thermohemd + dikke gebreide trui",
                    bottomItem = "Lange onderbroek + stevige broek",
                    outerwearItem = "Dikke winterjas tot de kin dichtgeritst",
                    footwearItem = "Warme winterlaarzen",
                    accessoryItems = listOf("Wollen sjaal", "Handschoenen", "Thermosfles soep"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Neem voor de zekerheid een vestje mee! 🧥",
                    roastMessage = "Het voelt nu lekker, maar wat als de wind straks aantrekt? Beter voorkomen dan genezen schat!",
                    dripScore = 88,
                    dripScoreTag = "Overal op voorbereid",
                    topItem = "Comfortabel shirt",
                    bottomItem = "Spijkerbroek",
                    outerwearItem = "Vest of windjack in je tas",
                    footwearItem = "Fijne wandelschoenen",
                    accessoryItems = listOf("Extra jasje", "Appeltje", "Flesje water"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.FASHIONISTE -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Resort Runway Realness 🌴",
                    roastMessage = "High-fashion zomerstatement: zwierig linnen silhouet met luxe zonnebril. Zweet is slechts haute-couture glans.",
                    dripScore = 98,
                    dripScoreTag = "Vogue Zomereditie",
                    topItem = "Zijden camp-collar overhemd",
                    bottomItem = "Geplooide wijde linnen pantalon",
                    outerwearItem = "Luchtige kimono van zijde (puur esthetisch)",
                    footwearItem = "Italiaanse leren muiltjes",
                    accessoryItems = listOf("Cat-eye zonnebril", "Canvas shopper", "Hydraterende spray"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Dramatische Noir Trenchcoat 🎬",
                    roastMessage = "Maak van de miezerregen een filmset. Een dramatische lange trenchcoat en grove laarzen dwingen respect af.",
                    dripScore = 96,
                    dripScoreTag = "Dramatische hoofdrol",
                    topItem = "Nauwsluitende coltrui",
                    bottomItem = "Zwarte tailored pantalon",
                    outerwearItem = "Dubbelrijige vinyl trenchcoat",
                    footwearItem = "Chunky platform laarzen",
                    accessoryItems = listOf("Effen zwarte paraplu", "Leren handschoenen"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Streetwear Royalty 👟",
                    roastMessage = "Gematigd weer geeft ultieme outfitvrijheid. Combineer gestructureerde jassen met exclusieve sneakers.",
                    dripScore = 97,
                    dripScoreTag = "Streetstyle Icoon",
                    topItem = "Vintage grafisch T-shirt",
                    bottomItem = "Raw selvedge denim",
                    outerwearItem = "Gestructureerd bomberjack of leren jack",
                    footwearItem = "Limited edition retro sneakers",
                    accessoryItems = listOf("Crossbody tas", "Gekleurde brilglazen"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.BRO -> when {
                tempC >= 15.0 -> ClothingRecommendation(
                    verdictTitle = "Korte Broek + Hoodie Combo 🤙",
                    roastMessage = "Is het korte broeken weer? Altijd. Hoodie aan, slippers of sneakers eronder en je bent helemaal klaar gast.",
                    dripScore = 80,
                    dripScoreTag = "Chille Gast Standaard",
                    topItem = "Gewoon zwart T-shirt",
                    bottomItem = "Sportieve korte broek (wind maakt niet uit)",
                    outerwearItem = "Grijze hoodie met rits",
                    footwearItem = "Badslippers of alledaagse sneakers",
                    accessoryItems = listOf("Petje achterstevoren", "Eiwitshake"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Trainingsbroek & Dikke Jas Bro 👊",
                    roastMessage = "Het is koud man. Doe gewoon een joggingbroek en een dikke jas aan. Geen gedoe.",
                    dripScore = 82,
                    dripScoreTag = "Nul moeite, ultiem comfort",
                    topItem = "Stevig basic T-shirt",
                    bottomItem = "Zachte fleece joggingbroek",
                    outerwearItem = "Dikke gewatteerde jas of fleece",
                    footwearItem = "Chille sneakers",
                    accessoryItems = listOf("Simpele muts", "Sleutelbos aan broekriem"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
        }
    }

    private fun generateSpanishAdvice(
        persona: AdvicePersona,
        tempC: Double,
        apparentC: Double,
        isRainy: Boolean,
        isSnowy: Boolean,
        isWindy: Boolean,
        isExtremeHeat: Boolean,
        conditionName: String,
        mascotRes: Int,
        funnyStats: List<FunnyStat>
    ): ClothingRecommendation {
        return when (persona) {
            AdvicePersona.SASSY -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Simulador de Sauna 3000 🔥",
                    roastMessage = "Hace un calor infernal. Vístete con lo mínimo legalmente permitido. Usar mezclilla gruesa hoy es puro masoquismo emocional.",
                    dripScore = 88,
                    dripScoreTag = "Energía anti-sudor",
                    topItem = "Camiseta o tirantes de lino ultra fresca",
                    bottomItem = "Pantalones cortos ligeros (¡nada de jeans ajustados!)",
                    outerwearItem = "ABSOLUTAMENTE NADA. Déjalo en casa.",
                    footwearItem = "Sandalias frescas o tenis ventilados",
                    accessoryItems = listOf("Gafas oscuras", "Mini ventilador", "Litro de agua"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Bolsa de Basura Chic 🌧️",
                    roastMessage = "El cielo está goteando con furia. Ponte armadura impermeable o parecerás un perro mojado antes de mediodía.",
                    dripScore = 65,
                    dripScoreTag = "Superviviente húmedo",
                    topItem = "Camiseta de secado rápido",
                    bottomItem = "Pantalones resistentes al agua",
                    outerwearItem = "Impermeable resistente o cortavientos impermeable",
                    footwearItem = "Botas de agua o tenis viejos que no te importen",
                    accessoryItems = listOf("Paraguas resistente", "Mochila impermeable", "Calcetines secos"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isSnowy || tempC < 5.0 -> ClothingRecommendation(
                    verdictTitle = "Modo Malvavisco Humano ❄️",
                    roastMessage = "Frío que muerde. Canaliza tu muñeco Michelin interior. Si puedes mover los brazos con soltura, te faltan capas.",
                    dripScore = 94,
                    dripScoreTag = "Máxima calidez",
                    topItem = "Camiseta térmica + sudadera de felpa",
                    bottomItem = "Mallas térmicas bajo pantalón grueso",
                    outerwearItem = "Abrigo acolchado gigante estilo puffer",
                    footwearItem = "Botas de nieve con calcetines de lana gruesos",
                    accessoryItems = listOf("Gorro de lana", "Bufanda suave", "Guantes térmicos"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC in 15.0..24.0 -> ClothingRecommendation(
                    verdictTitle = "Energía de Protagonista 🕶️",
                    roastMessage = "¡Clima de ensueño absoluto! Puedes ponerte lo que sea y parecer de anuncio de moda. Sal a deslumbrar.",
                    dripScore = 99,
                    dripScoreTag = "Perfección total",
                    topItem = "Camiseta oversize estilosa o camisa abierta",
                    bottomItem = "Chinos relajados o jeans clásicos",
                    outerwearItem = "Chaqueta vaquera ligera sobre los hombros",
                    footwearItem = "Tenis blancos impecables",
                    accessoryItems = listOf("Gafas de sol de diseño", "Reloj", "Confianza arrolladora"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Clima Clásico de Sudadera ☕",
                    roastMessage = "¡Un poco fresco! Ponte tu sudadera favorita. El clásico look 'con estilo sin esforzarse'.",
                    dripScore = 85,
                    dripScoreTag = "Minimalismo acogedor",
                    topItem = "Camiseta de algodón suave",
                    bottomItem = "Jeans cómodos o pantalones cargo",
                    outerwearItem = "Tu sudadera gruesa favorita con capucha",
                    footwearItem = "Tenis del día a día",
                    accessoryItems = listOf("Café caliente", "Gorro beanie"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.PARENT -> when {
                isRainy -> ClothingRecommendation(
                    verdictTitle = "¡¿LLEVAS PARAGUAS?! ☔",
                    roastMessage = "¡Ni se te ocurra salir sin chubasquero y buenas botas! ¿Quieres pillar una neumonía?! ¡Ponte un suéter debajo!",
                    dripScore = 100,
                    dripScoreTag = "Aprobado por tus padres",
                    topItem = "Camiseta de manga larga bien metida por dentro",
                    bottomItem = "Pantalones abrigados",
                    outerwearItem = "Chubasquero grueso con capucha",
                    footwearItem = "Botas impermeables con calcetines de lana",
                    accessoryItems = listOf("Paraguas grande", "Pañuelos", "Gel hidroalcohólico"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                tempC < 12.0 -> ClothingRecommendation(
                    verdictTitle = "¡SÚBETE LA CREMALLERA HASTA ARRIBA! 🧥",
                    roastMessage = "¡Llevas el cuello al aire! ¿Dónde está la bufanda?! Come algo caliente antes de salir y lleva un termo con té.",
                    dripScore = 95,
                    dripScoreTag = "100% Calidez asegurada",
                    topItem = "Camiseta interior térmica + suéter grueso",
                    bottomItem = "Ropa interior larga + pantalón abrigado",
                    outerwearItem = "Abrigo de invierno abrochado hasta la barbilla",
                    footwearItem = "Botas cálidas de invierno",
                    accessoryItems = listOf("Bufanda de lana", "Guantes", "Termo de caldo"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "¡Llévate una chaquetita por si acaso! 🧥",
                    roastMessage = "Ahora está agradable, pero ¿y si refresca luego? ¡Más vale prevenir que curar cariño!",
                    dripScore = 88,
                    dripScoreTag = "Preparado para todo",
                    topItem = "Camisa cómoda",
                    bottomItem = "Pantalones vaqueros",
                    outerwearItem = "Rebeca o cortavientos en la mochila",
                    footwearItem = "Zapatillas cómodas para caminar",
                    accessoryItems = listOf("Chaqueta ligera", "Fruta / Snack", "Botella de agua"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.FASHIONISTE -> when {
                isExtremeHeat -> ClothingRecommendation(
                    verdictTitle = "Elegancia de Pasarela Resort 🌴",
                    roastMessage = "Declaración estival de alta moda: silueta holgada de lino con viseras exclusivas. El sudor es solo brillo de pasarela.",
                    dripScore = 98,
                    dripScoreTag = "Edición Verano Vogue",
                    topItem = "Camisa de seda fluida con cuello camp",
                    bottomItem = "Pantalón palazzo de lino con pliegues",
                    outerwearItem = "Kimono de seda drapeado (pura estética)",
                    footwearItem = "Mules de piel italiana",
                    accessoryItems = listOf("Gafas ojo de gato", "Bolso tote de lona", "Bruma hidratante"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                isRainy -> ClothingRecommendation(
                    verdictTitle = "Trench Noir Dramático 🎬",
                    roastMessage = "Convierte la llovizna en el rodaje de tu videoclip. Una gabardina larga y botas contundentes imponen respeto.",
                    dripScore = 96,
                    dripScoreTag = "Protagonista de cine",
                    topItem = "Suéter de cuello alto ajustado",
                    bottomItem = "Pantalones oscuros de vestir",
                    outerwearItem = "Gabardina cruzada de vinilo",
                    footwearItem = "Botas de plataforma impermeables",
                    accessoryItems = listOf("Paraguas negro liso", "Guantes de cuero"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Realeza del Streetwear 👟",
                    roastMessage = "El clima templado permite máxima versatilidad. Mezcla prendas estructuradas con zapatillas icónicas.",
                    dripScore = 97,
                    dripScoreTag = "Icono de Streetstyle",
                    topItem = "Camiseta vintage con gráfico",
                    bottomItem = "Jeans de corte recto",
                    outerwearItem = "Bomber estructurada o cazadora de cuero",
                    footwearItem = "Zapatillas retro de edición limitada",
                    accessoryItems = listOf("Bandolera cruzada", "Lentes tintadas"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
            AdvicePersona.BRO -> when {
                tempC >= 15.0 -> ClothingRecommendation(
                    verdictTitle = "Combo Shorts + Sudadera 🤙",
                    roastMessage = "¿Es tiempo de bermudas? Siempre. Ponte una sudadera con capucha, chanclas o tenis y vas sobrado para el día tío.",
                    dripScore = 80,
                    dripScoreTag = "Estilo Colega Relajado",
                    topItem = "Camiseta básica negra",
                    bottomItem = "Pantalón corto deportivo (haga el viento que haga)",
                    outerwearItem = "Sudadera gris con cremallera",
                    footwearItem = "Chanclas o tenis de diario",
                    accessoryItems = listOf("Gorra hacia atrás", "Shaker de proteína"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
                else -> ClothingRecommendation(
                    verdictTitle = "Pantalón de Chándal & Plumífero 👊",
                    roastMessage = "Hace frío hermano. Pantalón de chándal, sudadera abrigada y no te compliques la vida.",
                    dripScore = 82,
                    dripScoreTag = "Cero esfuerzo, comodidad total",
                    topItem = "Camiseta básica gruesa",
                    bottomItem = "Pantalón de chándal de felpa",
                    outerwearItem = "Chaqueta acolchada o forro polar",
                    footwearItem = "Tenis cómodos",
                    accessoryItems = listOf("Gorro de lana", "Llavero en la trabilla"),
                    mascotResId = mascotRes,
                    weatherConditionName = conditionName,
                    funnyStats = funnyStats
                )
            }
        }
    }

    private fun decodeWeatherCode(code: Int, rain: Double, snow: Double, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.DE -> when (code) {
                0 -> "Klarer Himmel & Sonne ☀️"
                1, 2 -> "Leicht bewölkt ⛅"
                3 -> "Bedeckter Himmel ☁️"
                45, 48 -> "Geheimnisvoller Nebel 🌫️"
                51, 53, 55 -> "Leichter Nieselregen 🌧️"
                61, 63, 65 -> "Heftiger Regensturm ⛈️"
                71, 73, 75, 77 -> "Winterliches Schneegestöber ❄️"
                80, 81, 82 -> "Vorübergehende Regenschauer 🌧️"
                85, 86 -> "Schneeschauer 🌨️"
                95, 96, 99 -> "Gewitter-Apokalypse ⚡"
                else -> if (rain > 0) "Regnerisch 🌧️" else if (snow > 0) "Schneefall ❄️" else "Mildes Wetter 🌤️"
            }
            AppLanguage.FR -> when (code) {
                0 -> "Ciel Clair & Soleil ☀️"
                1, 2 -> "Partiellement Nuageux ⛅"
                3 -> "Ciel Couvert ☁️"
                45, 48 -> "Brouillard Mystique 🌫️"
                51, 53, 55 -> "Bruine Légère 🌧️"
                61, 63, 65 -> "Forte Pluie Battante ⛈️"
                71, 73, 75, 77 -> "Paysage Enneigé ❄️"
                80, 81, 82 -> "Averses Passagères 🌧️"
                85, 86 -> "Averses de Neige 🌨️"
                95, 96, 99 -> "Orage Apocalyptique ⚡"
                else -> if (rain > 0) "Pluvieux 🌧️" else if (snow > 0) "Neigeux ❄️" else "Temps Doux 🌤️"
            }
            AppLanguage.NL -> when (code) {
                0 -> "Stralende Zon ☀️"
                1, 2 -> "Licht Bewolkt ⛅"
                3 -> "Geheel Bewolkt ☁️"
                45, 48 -> "Mystieke Mist 🌫️"
                51, 53, 55 -> "Lichte Motregen 🌧️"
                61, 63, 65 -> "Stevige Plensbui ⛈️"
                71, 73, 75, 77 -> "Winterse Sneeuwpret ❄️"
                80, 81, 82 -> "Passeerbuien 🌧️"
                85, 86 -> "Sneeuwbuien 🌨️"
                95, 96, 99 -> "Onweer Apocalyps ⚡"
                else -> if (rain > 0) "Regenachtig 🌧️" else if (snow > 0) "Sneeuwachtig ❄️" else "Aangenaam Weer 🌤️"
            }
            AppLanguage.ES -> when (code) {
                0 -> "Cielo Despejado y Sol ☀️"
                1, 2 -> "Parcialmente Nublado ⛅"
                3 -> "Cielo Nublado ☁️"
                45, 48 -> "Niebla Misteriosa 🌫️"
                51, 53, 55 -> "Llovizna Ligera 🌧️"
                61, 63, 65 -> "Tormenta de Lluvia ⛈️"
                71, 73, 75, 77 -> "Nevada Invernal ❄️"
                80, 81, 82 -> "Chubascos Pasajeros 🌧️"
                85, 86 -> "Chubascos de Nieve 🌨️"
                95, 96, 99 -> "Tormenta Apocalíptica ⚡"
                else -> if (rain > 0) "Lluvioso 🌧️" else if (snow > 0) "Nieve ❄️" else "Clima Suave 🌤️"
            }
            AppLanguage.EN -> when (code) {
                0 -> "Clear Sky Sunshine ☀️"
                1, 2 -> "Partly Cloudy ⛅"
                3 -> "Overcast Sky ☁️"
                45, 48 -> "Foggy Mystery 🌫️"
                51, 53, 55 -> "Light Drizzle 🌧️"
                61, 63, 65 -> "Heavy Rainstorm ⛈️"
                71, 73, 75, 77 -> "Snowfall Wonderland ❄️"
                80, 81, 82 -> "Passing Rain Showers 🌧️"
                85, 86 -> "Snow Showers 🌨️"
                95, 96, 99 -> "Thunderstorm Apocalypse ⚡"
                else -> if (rain > 0) "Rainy Drizzle 🌧️" else if (snow > 0) "Snowy Frost ❄️" else "Mild Weather 🌤️"
            }
        }
    }

    fun getHourlyMicroTip(hourIndex: Int, temp: Double, code: Int, lang: AppLanguage = AppLanguage.EN): String {
        return when (lang) {
            AppLanguage.DE -> when {
                temp >= 28.0 -> "🔥 Schwitz-Gefahr! Nur T-Shirt"
                temp in 20.0..27.9 -> "☀️ T-Shirt & Sonnenbrille"
                temp in 14.0..19.9 -> "🧥 Leichte Jacke / Hoodie"
                temp in 5.0..13.9 -> "🧥 Jacke erforderlich!"
                else -> "❄️ Frostwarnung! Dicke Daunenjacke"
            }
            AppLanguage.FR -> when {
                temp >= 28.0 -> "🔥 Alerte chaleur! T-shirt seul"
                temp in 20.0..27.9 -> "☀️ T-shirt & lunettes de soleil"
                temp in 14.0..19.9 -> "🧥 Veste légère / sweat"
                temp in 5.0..13.9 -> "🧥 Manteau obligatoire!"
                else -> "❄️ Alerte gel! Sortez la doudoune"
            }
            AppLanguage.NL -> when {
                temp >= 28.0 -> "🔥 Zweetalarm! Alleen T-shirt"
                temp in 20.0..27.9 -> "☀️ T-shirt & zonnebril"
                temp in 14.0..19.9 -> "🧥 Licht jasje / hoodie"
                temp in 5.0..13.9 -> "🧥 Warme jas vereist!"
                else -> "❄️ Vorstwaarschuwing! Dikke puffer"
            }
            AppLanguage.ES -> when {
                temp >= 28.0 -> "🔥 ¡Alerta de calor! Solo camiseta"
                temp in 20.0..27.9 -> "☀️ Camiseta y gafas de sol"
                temp in 14.0..19.9 -> "🧥 Chaqueta ligera / sudadera"
                temp in 5.0..13.9 -> "🧥 ¡Abrigo necesario!"
                else -> "❄️ ¡Alerta de helada! Modo plumífero"
            }
            AppLanguage.EN -> when {
                temp >= 28.0 -> "🔥 Sweat alert! T-shirt only"
                temp in 20.0..27.9 -> "☀️ T-shirt & sunglasses"
                temp in 14.0..19.9 -> "🧥 Light jacket / hoodie"
                temp in 5.0..13.9 -> "🧥 Coat required!"
                else -> "❄️ Freeze warning! Puffer time"
            }
        }
    }

    fun getDailyForecastSummary(tempMax: Double, tempMin: Double, code: Int, lang: AppLanguage = AppLanguage.EN): String {
        val avg = (tempMax + tempMin) / 2.0
        return when (lang) {
            AppLanguage.DE -> when {
                code in listOf(61, 63, 65, 80, 81, 82, 95) -> "Regenjacke Pflicht! 🌧️"
                code in listOf(71, 73, 75, 85, 86) -> "Wintermantel & Schneestiefel ❄️"
                avg >= 26.0 -> "So leicht wie möglich anziehen ☀️"
                avg >= 18.0 -> "T-Shirt & Shorts Vibe 😎"
                avg >= 10.0 -> "Hoodie & Jeans Wetter 🧥"
                else -> "Dicker Mantel & Schal Schichten 🧣"
            }
            AppLanguage.FR -> when {
                code in listOf(61, 63, 65, 80, 81, 82, 95) -> "Imperméable obligatoire! 🌧️"
                code in listOf(71, 73, 75, 85, 86) -> "Gros manteau & après-ski ❄️"
                avg >= 26.0 -> "Tenue la plus légère possible ☀️"
                avg >= 18.0 -> "T-shirt & short détente 😎"
                avg >= 10.0 -> "Pull & jean confortable 🧥"
                else -> "Manteau chaud & écharpe 🧣"
            }
            AppLanguage.NL -> when {
                code in listOf(61, 63, 65, 80, 81, 82, 95) -> "Regenjas verplicht! 🌧️"
                code in listOf(71, 73, 75, 85, 86) -> "Dikke winterjas & sneeuwschoenen ❄️"
                avg >= 26.0 -> "Zo luchtig mogelijk kleden ☀️"
                avg >= 18.0 -> "T-shirt & korte broek vibe 😎"
                avg >= 10.0 -> "Hoodie & spijkerbroek weer 🧥"
                else -> "Dikke jas & sjaal lagen 🧣"
            }
            AppLanguage.ES -> when {
                code in listOf(61, 63, 65, 80, 81, 82, 95) -> "¡Chubasquero obligatorio! 🌧️"
                code in listOf(71, 73, 75, 85, 86) -> "Abrigo grueso y botas de nieve ❄️"
                avg >= 26.0 -> "Ropa lo más ligera posible ☀️"
                avg >= 18.0 -> "Vibra de camiseta y bermudas 😎"
                avg >= 10.0 -> "Tiempo de sudadera y vaqueros 🧥"
                else -> "Abrigo grueso y bufanda 🧣"
            }
            AppLanguage.EN -> when {
                code in listOf(61, 63, 65, 80, 81, 82, 95) -> "Raincoat mandatory! 🌧️"
                code in listOf(71, 73, 75, 85, 86) -> "Heavy winter coat & snow boots ❄️"
                avg >= 26.0 -> "Lightest clothing possible ☀️"
                avg >= 18.0 -> "T-shirt & shorts vibe 😎"
                avg >= 10.0 -> "Hoodie & jeans weather 🧥"
                else -> "Thick coat & beanie layering 🧣"
            }
        }
    }
}
