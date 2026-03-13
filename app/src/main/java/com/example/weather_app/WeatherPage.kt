package com.example.weather_app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.SharedPreferences
import coil.compose.AsyncImage
import com.example.weather_app.api.weatherModel
import com.example.weather_app.ui.theme.NetworkResponse
import kotlinx.coroutines.delay
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf<List<String>>(emptyList()) }
    var filteredSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isDark = isSystemInDarkTheme()
    val prefs = context.getSharedPreferences("weather_app", android.content.Context.MODE_PRIVATE)
    val gson = Gson()

    // Load search history on startup
    LaunchedEffect(Unit) {
        searchHistory = loadSearchHistory(prefs, gson)
    }

    // Color scheme
    val bgGradient = if (isDark) {
        listOf(
            Color(0xFF1a1a2e),
            Color(0xFF16213e),
            Color(0xFF0f3460)
        )
    } else {
        listOf(
            Color(0xFF4facfe),
            Color(0xFF00b4db),
            Color(0xFF667eea)
        )
    }

    val searchBarBg = if (isDark) Color(0xFF2d2d44) else Color.White
    val searchBarText = if (isDark) Color(0xFFe0e0e0) else Color(0xFF2c3e50)
    val accentColor = if (isDark) Color(0xFF8ab4f8) else Color(0xFF4facfe)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(bgGradient))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Section with Title and Search
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 16.dp)
            ) {
                // App Title
                AnimatedTitle(isDark)

                Spacer(modifier = Modifier.height(24.dp))

                // Search Bar with improved design
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp)),
                                value = city,
                                onValueChange = { newValue ->
                                    city = newValue
                                    showSuggestions = newValue.isNotBlank()
                                    filteredSuggestions = if (newValue.isNotBlank()) {
                                        searchHistory.filter {
                                            it.contains(newValue, ignoreCase = true)
                                        }
                                    } else {
                                        emptyList()
                                    }
                                },
                                placeholder = {
                                    Text(
                                        "Search city (e.g., London, Tokyo)",
                                        color = searchBarText.copy(alpha = 0.5f),
                                        fontSize = 15.sp
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedContainerColor = searchBarBg,
                                    unfocusedContainerColor = searchBarBg,
                                    focusedTextColor = searchBarText,
                                    unfocusedTextColor = searchBarText
                                ),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        if (city.isNotBlank()) {
                                            addToSearchHistory(prefs, gson, city)
                                            searchHistory = loadSearchHistory(prefs, gson)
                                            viewModel.getData(city)
                                            keyboardController?.hide()
                                            showSuggestions = false
                                        }
                                    }
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )

                            // Redesigned Search Button
                            EnhancedSearchButton(
                                onClick = {
                                    if (city.isNotBlank()) {
                                        addToSearchHistory(prefs, gson, city)
                                        searchHistory = loadSearchHistory(prefs, gson)
                                        viewModel.getData(city)
                                        keyboardController?.hide()
                                        showSuggestions = false
                                    }
                                },
                                accentColor = accentColor
                            )
                        }

                        // Suggestions Dropdown
                        if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            SuggestionsDropdown(
                                suggestions = filteredSuggestions,
                                onSuggestionClick = { suggestion ->
                                    city = suggestion
                                    addToSearchHistory(prefs, gson, suggestion)
                                    searchHistory = loadSearchHistory(prefs, gson)
                                    viewModel.getData(suggestion)
                                    keyboardController?.hide()
                                    showSuggestions = false
                                },
                                isDark = isDark,
                                searchBarBg = searchBarBg,
                                searchBarText = searchBarText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Content with animations
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                when (val result = weatherResult.value) {
                    is NetworkResponse.Error -> {
                        ErrorView(result.message.toString(), isDark)
                    }
                    NetworkResponse.Loading -> {
                        LoadingView()
                    }
                    is NetworkResponse.Success<*> -> {
                        WeatherDetails(result.data as weatherModel, isDark)
                    }
                    null -> {
                        WelcomeView(isDark)
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedSearchButton(onClick: () -> Unit, accentColor: Color) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed) 4f else 8f,
        animationSpec = tween(200),
        label = "button_shadow"
    )

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .height(56.dp)
            .width(56.dp)
            .scale(scale)
            .shadow(shadowElevation.dp, shape = RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = accentColor
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200)
            isPressed = false
        }
    }
}

@Composable
fun SuggestionsDropdown(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    isDark: Boolean,
    searchBarBg: Color,
    searchBarText: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = searchBarBg
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(suggestions) { suggestion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSuggestionClick(suggestion) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = searchBarText.copy(alpha = 0.6f)
                    )
                    Text(
                        text = suggestion,
                        fontSize = 16.sp,
                        color = searchBarText,
                        fontWeight = FontWeight.Normal
                    )
                }
                if (suggestion != suggestions.last()) {
                    Divider(
                        color = searchBarText.copy(alpha = 0.1f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedTitle(isDark: Boolean) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val offsetY by animateIntAsState(
        targetValue = if (visible) 0 else -50,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "title_offset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "title_alpha"
    )

    Column(
        modifier = Modifier
            .offset(y = offsetY.dp)
            .alpha(alpha)
    ) {
        Text(
            text = "Weather",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.5.sp
        )
        Text(
            text = "Stay updated with real-time forecasts",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun WelcomeView(isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "welcome")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .scale(scale)
                .alpha(alpha),
            tint = if (isDark) Color(0xFF8ab4f8) else Color.White.copy(alpha = 0.9f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Search for a city",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Get accurate weather information\nfor any location worldwide",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f),
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun ErrorView(message: String, isDark: Boolean) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + scaleIn(tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "⚠️",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Oops!",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun LoadingView() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 4.dp,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Fetching weather data...",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun WeatherDetails(data: weatherModel, isDark: Boolean) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(data) {
        visible = false
        delay(100)
        visible = true
    }

    val cardBg = if (isDark) Color(0xFF2d2d44) else Color.White
    val textPrimary = if (isDark) Color(0xFFe0e0e0) else Color(0xFF2c3e50)
    val textSecondary = if (isDark) Color(0xFFa0a0b0) else Color(0xFF7f8c8d)
    val accentColor = if (isDark) Color(0xFF8ab4f8) else Color(0xFF4facfe)

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(700)) + slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            initialOffsetY = { it / 2 }
        )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AnimatedCard(delay = 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg.copy(alpha = 0.95f)
                        ),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp),
                                tint = accentColor
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${data.location.name}, ${data.location.country}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary
                            )
                        }
                    }
                }
            }

            item {
                AnimatedCard(delay = 100) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg.copy(alpha = 0.95f)
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(36.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var iconVisible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                delay(300)
                                iconVisible = true
                            }

                            val iconScale by animateFloatAsState(
                                targetValue = if (iconVisible) 1f else 0.5f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                ),
                                label = "icon_scale"
                            )

                            AsyncImage(
                                model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(130.dp)
                                    .scale(iconScale)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = data.current.condition.text,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                color = textPrimary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(28.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                var tempVisible by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    delay(500)
                                    tempVisible = true
                                }

                                AnimatedVisibility(
                                    visible = tempVisible,
                                    enter = fadeIn(tween(600)) + scaleIn(tween(600))
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${data.current.temp_c}",
                                            fontSize = 80.sp,
                                            color = accentColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "°C",
                                            fontSize = 40.sp,
                                            color = accentColor,
                                            fontWeight = FontWeight.Light
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Feels like ${data.current.feelslike_c}°C",
                                fontSize = 17.sp,
                                color = textSecondary,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }

            item {
                AnimatedCard(delay = 200) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg.copy(alpha = 0.95f)
                        ),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            verticalArrangement = Arrangement.spacedBy(28.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                QuickInfoItem(
                                    "💧",
                                    "Humidity",
                                    "${data.current.humidity}%",
                                    textPrimary,
                                    textSecondary,
                                    0
                                )
                                QuickInfoItem(
                                    "💨",
                                    "Wind",
                                    "${data.current.wind_kph} km/h",
                                    textPrimary,
                                    textSecondary,
                                    100
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                QuickInfoItem(
                                    "☁️",
                                    "Cloudiness",
                                    "${data.current.cloud}%",
                                    textPrimary,
                                    textSecondary,
                                    200
                                )
                                QuickInfoItem(
                                    "🌡️",
                                    "Pressure",
                                    "${data.current.pressure_mb} mb",
                                    textPrimary,
                                    textSecondary,
                                    300
                                )
                            }
                        }
                    }
                }
            }

            item {
                AnimatedCard(delay = 300) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg.copy(alpha = 0.95f)
                        ),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp)
                        ) {
                            Text(
                                text = "Additional Details",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            DetailRow("Wind Degree", "${data.current.wind_degree}°", textPrimary, textSecondary, 0)
                            DetailRow("Dew Point", "${data.current.dewpoint_c}°C", textPrimary, textSecondary, 50)
                            DetailRow("Heat Index", "${data.current.heatindex_c}°C", textPrimary, textSecondary, 100)
                            DetailRow(
                                "Local Time",
                                "${data.location.localtime.split(" ")[1]} • ${data.location.localtime.split(" ")[0]}",
                                textPrimary,
                                textSecondary,
                                150
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AnimatedCard(delay: Long, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label = "card_alpha"
    )

    val offsetY by animateIntAsState(
        targetValue = if (visible) 0 else 30,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_offset"
    )

    Box(
        modifier = Modifier
            .offset(y = offsetY.dp)
            .alpha(alpha)
    ) {
        content()
    }
}

@Composable
fun QuickInfoItem(
    icon: String,
    label: String,
    value: String,
    textPrimary: Color,
    textSecondary: Color,
    delay: Long
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        visible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "item_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(140.dp)
            .scale(scale)
    ) {
        Text(
            text = icon,
            fontSize = 36.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            color = textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    textPrimary: Color,
    textSecondary: Color,
    delay: Long
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(400),
        label = "row_alpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .alpha(alpha),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = textSecondary,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 15.sp,
            color = textPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Storage Functions
fun addToSearchHistory(prefs: SharedPreferences, gson: Gson, city: String) {
    val currentHistory = loadSearchHistory(prefs, gson).toMutableList()

    // Remove if already exists to avoid duplicates
    currentHistory.remove(city)

    // Add to the beginning
    currentHistory.add(0, city)

    // Keep only last 10 searches
    if (currentHistory.size > 10) {
        currentHistory.removeAt(currentHistory.lastIndex)
    }

    val json = gson.toJson(currentHistory)
    prefs.edit().putString("search_history", json).apply()
}

fun loadSearchHistory(prefs: SharedPreferences, gson: Gson): List<String> {
    val json = prefs.getString("search_history", null)
    return if (json != null) {
        try {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    } else {
        emptyList()
    }
}