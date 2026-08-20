package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.api.GeocodingResult
import com.example.data.db.SavedLocationEntity
import com.example.data.domain.AppLanguage
import com.example.data.domain.AppStrings
import com.example.ui.WeatherUiState
import com.example.ui.WeatherViewModel
import com.example.ui.components.CitySearchSheet
import com.example.ui.components.ClothingHeroCard
import com.example.ui.components.FunnyStatsSection
import com.example.ui.components.HourlyOutfitTimeline
import com.example.ui.components.OutfitChecklistCard
import com.example.ui.components.PersonaSelector
import com.example.ui.components.PreferencesSheet
import com.example.ui.components.WeeklyOutfitForecast
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.OutfitCastTheme
import com.example.ui.theme.VibrantIndigo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            fetchUserLocation()
        } else {
            Toast.makeText(this, "Location permission denied. Using default city.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (hasLocationPermission()) {
            fetchUserLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        setContent {
            OutfitCastTheme {
                MainScreen(
                    viewModel = viewModel,
                    onFetchMyLocation = { fetchUserLocation() }
                )
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun fetchUserLocation() {
        if (!hasLocationPermission()) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        Toast.makeText(this, "Fetching current GPS location... 📍", Toast.LENGTH_SHORT).show()

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        processLocation(location.latitude, location.longitude)
                    } else {
                        val cancellationTokenSource = CancellationTokenSource()
                        fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                            cancellationTokenSource.token
                        ).addOnSuccessListener { currentLocation ->
                            if (currentLocation != null) {
                                processLocation(currentLocation.latitude, currentLocation.longitude)
                            } else {
                                Toast.makeText(this, "Unable to get current location coordinates", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "Location error: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error fetching location: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Location security exception: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processLocation(lat: Double, lon: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lon, 1) { addresses ->
                val address = addresses.firstOrNull()
                val city = address?.locality ?: address?.subAdminArea ?: address?.adminArea ?: "Current Location"
                val country = address?.countryName ?: ""
                runOnUiThread {
                    viewModel.loadWeather(lat, lon, city, country)
                }
            }
        } else {
            try {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                val address = addresses?.firstOrNull()
                val city = address?.locality ?: address?.subAdminArea ?: address?.adminArea ?: "Current Location"
                val country = address?.countryName ?: ""
                viewModel.loadWeather(lat, lon, city, country)
            } catch (e: Exception) {
                viewModel.loadWeather(lat, lon, "Current Location", "")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: WeatherViewModel,
    onFetchMyLocation: () -> Unit = {}
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedPersona by viewModel.selectedPersona.collectAsStateWithLifecycle()
    val isCelsius by viewModel.isCelsius.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedIconOption by viewModel.selectedIconOption.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val savedLocations by viewModel.savedLocations.collectAsStateWithLifecycle()

    var showSearchSheet by remember { mutableStateOf(false) }
    var showPreferencesSheet by remember { mutableStateOf(false) }
    val searchSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (uiState is WeatherUiState.Success) {
                val state = uiState as WeatherUiState.Success
                FloatingActionButton(
                    onClick = {
                        val shareText = "OutfitCast (${state.cityName}):\n" +
                                "${state.clothingRecommendation.verdictTitle}\n" +
                                "\"${state.clothingRecommendation.roastMessage}\"\n\n" +
                                "Top: ${state.clothingRecommendation.topItem}\n" +
                                "Bottom: ${state.clothingRecommendation.bottomItem}\n" +
                                "Outerwear: ${state.clothingRecommendation.outerwearItem}\n" +
                                "Drip Score: ${state.clothingRecommendation.dripScore}/100"

                        clipboardManager.setText(AnnotatedString(shareText))
                        Toast.makeText(context, "Copied outfit advice to clipboard! 📋", Toast.LENGTH_SHORT).show()
                    },
                    containerColor = CoralOrange,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("share_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share outfit advice"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = ElectricBlue,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = AppStrings.getLoadingMessage(selectedLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Analyzing drip & weather conditions...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is WeatherUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = "Error",
                            modifier = Modifier.size(64.dp),
                            tint = CoralOrange
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = AppStrings.getFashionEmergencyTitle(selectedLanguage),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                viewModel.loadWeather(52.3676, 4.9041, "Amsterdam", "Netherlands")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(AppStrings.getRetryLabel(selectedLanguage))
                        }
                    }
                }

                is WeatherUiState.Success -> {
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Header App Branding & Preferences Quick Action
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = ElectricBlue.copy(alpha = 0.15f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Checkroom,
                                            contentDescription = "Logo",
                                            tint = ElectricBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = AppStrings.getAppName(selectedLanguage),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = AppStrings.getAppTagline(selectedLanguage),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Language & Preferences Pill
                                Surface(
                                    onClick = { showPreferencesSheet = true },
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.testTag("open_preferences_button")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = selectedLanguage.flagEmoji,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Palette,
                                            contentDescription = "Preferences",
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                // Quick Refresh Button
                                IconButton(
                                    onClick = {
                                        viewModel.loadWeather(
                                            state.latitude,
                                            state.longitude,
                                            state.cityName,
                                            state.countryName
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh weather",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Hero Outfit Card
                        ClothingHeroCard(
                            cityName = state.cityName,
                            countryName = state.countryName,
                            weather = state.weather.current ?: com.example.data.api.CurrentWeather(),
                            recommendation = state.clothingRecommendation,
                            isCelsius = isCelsius,
                            isSaved = state.isSaved,
                            onToggleSave = { viewModel.toggleSaveCurrentLocation() },
                            onOpenSearch = { showSearchSheet = true },
                            onToggleUnit = { viewModel.toggleTemperatureUnit() },
                            onUseMyLocation = onFetchMyLocation
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Persona Selector Row
                        PersonaSelector(
                            selectedPersona = selectedPersona,
                            language = selectedLanguage,
                            onSelectPersona = { viewModel.setPersona(it) }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Outfit Checklist Card
                        OutfitChecklistCard(
                            recommendation = state.clothingRecommendation,
                            language = selectedLanguage
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Funny Stats Section
                        FunnyStatsSection(
                            stats = state.clothingRecommendation.funnyStats,
                            language = selectedLanguage
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Hourly Outfit Timeline
                        HourlyOutfitTimeline(
                            hourly = state.weather.hourly,
                            isCelsius = isCelsius,
                            language = selectedLanguage
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Weekly Outfit Forecast
                        WeeklyOutfitForecast(
                            daily = state.weather.daily,
                            isCelsius = isCelsius,
                            language = selectedLanguage
                        )

                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            // City Search Bottom Sheet
            if (showSearchSheet) {
                CitySearchSheet(
                    sheetState = searchSheetState,
                    searchQuery = searchQuery,
                    searchResults = searchResults,
                    isSearching = isSearching,
                    savedLocations = savedLocations,
                    language = selectedLanguage,
                    onQueryChanged = { viewModel.onSearchQueryChanged(it) },
                    onSelectCity = { city ->
                        viewModel.selectSearchResult(city)
                    },
                    onSelectSavedLocation = { saved ->
                        viewModel.loadWeather(saved.latitude, saved.longitude, saved.name, saved.country)
                    },
                    onRemoveSavedLocation = { id ->
                        viewModel.removeSavedLocation(id)
                    },
                    onUseMyLocation = onFetchMyLocation,
                    onDismiss = { showSearchSheet = false }
                )
            }

            // Preferences Bottom Sheet
            if (showPreferencesSheet) {
                PreferencesSheet(
                    currentLanguage = selectedLanguage,
                    isCelsius = isCelsius,
                    currentPersona = selectedPersona,
                    selectedIconOption = selectedIconOption,
                    onLanguageSelected = { lang ->
                        viewModel.setLanguage(lang)
                    },
                    onToggleCelsius = { isC ->
                        viewModel.setTemperatureUnit(isC)
                    },
                    onPersonaSelected = { persona ->
                        viewModel.setPersona(persona)
                    },
                    onIconOptionSelected = { optId ->
                        viewModel.setIconOption(optId)
                        Toast.makeText(context, "App icon preference set to Option $optId", Toast.LENGTH_SHORT).show()
                    },
                    onDismiss = { showPreferencesSheet = false }
                )
            }
        }
    }
}
