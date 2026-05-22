package com.example.kaagada.ui.screens
import java.net.URL
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.common.model.DownloadConditions

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import java.net.URLEncoder
import com.example.kaagada.BuildConfig
import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection

import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

// --- CRITICAL PROJECT IMPORTS ---
import com.example.kaagada.R
import com.example.kaagada.data.model.AlphabetProvider
import com.example.kaagada.data.model.KannadaAlphabet
import com.example.kaagada.data.model.Proverb            // Added
import com.example.kaagada.data.remote.RetrofitClient     // Added
import com.example.kaagada.ui.adapter.ProverbAdapter      // Added
import com.example.kaagada.ui.navigation.BottomNavItem
import com.example.kaagada.ui.navigation.Screen
import com.example.kaagada.ui.navigation.bottomNavItems
import com.example.kaagada.ui.theme.*
import com.example.kaagada.ui.viewmodel.AuthViewModel

// --- LEGACY UI IMPORTS ---
import androidx.compose.ui.viewinterop.AndroidView
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    Scaffold(
        containerColor = KaagadaRed
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "welcome to",
                color = KaagadaYellow,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "kaagada",
                color = KaagadaYellow,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(KaagadaYellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "School",
                    modifier = Modifier.size(120.dp),
                    tint = KaagadaRed
                )
            }

            Spacer(modifier = Modifier.weight(1.2f))

            Button(
                onClick = {
                    viewModel.completeOnboarding()
                    onGetStarted()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = KaagadaYellow,
                    contentColor = KaagadaRed
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "get started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onLoginClick) {
                Text(
                    text = "already a user? login",
                    color = KaagadaYellow
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") } // Note: Firebase treats this as email
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = KaagadaRed
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(KaagadaYellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp),
                    tint = KaagadaRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "login",
                color = KaagadaYellow,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("email", color = Black.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black,
                    cursorColor = Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("password", color = Black.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null, tint = Black)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black,
                    cursorColor = Black
                )
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = KaagadaYellow,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // NEW FIREBASE LOGIC
                    viewModel.login(username, password,
                        onSuccess = { onLoginSuccess() },
                        onError = { error = it }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = KaagadaYellow,
                    contentColor = KaagadaRed
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onSignupClick) {
                Text(
                    text = "don't have an account? signup",
                    color = KaagadaYellow
                )
            }
        }
    }
}

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = KaagadaRed
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(KaagadaYellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp),
                    tint = KaagadaRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "create account!",
                color = KaagadaYellow,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("email", color = Black.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black,
                    cursorColor = Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("name", color = Black.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black,
                    cursorColor = Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = { Text("phone no", color = Black.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black,
                    cursorColor = Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("password", color = Black.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black,
                    cursorColor = Black
                )
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = KaagadaYellow,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // NEW FIREBASE LOGIC
                    viewModel.signup(username, email, phone, password,
                        onSuccess = { onSignupSuccess() },
                        onError = { error = it }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = KaagadaYellow,
                    contentColor = KaagadaRed
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "create account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onLoginClick) {
                Text(
                    text = "already a user? login",
                    color = KaagadaYellow
                )
            }
        }
    }
}

// ── FEATURE 3: Bottom Navigation Bar ──────────────────────────────────────
// HomeScreen now receives a NavController so the BottomNavigationBar can
// drive top-level navigation without prop-drilling individual callbacks.
// The Scaffold bottomBar slot renders a Material3 NavigationBar that
// highlights the currently active route.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLearnAlphabets: () -> Unit,
    onIdentifyAlphabets: () -> Unit,
    onPhrases: () -> Unit,
    onFlashcards: () -> Unit,
    onProverbs: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    val scrollState = rememberScrollState()

    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    var isTranslating by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val currentRoute =
        navController.currentBackStackEntry?.destination?.route

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "kaagada",
                        fontWeight = FontWeight.Bold
                    )
                },

                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KaagadaRed,
                    titleContentColor = KaagadaYellow,
                    actionIconContentColor = KaagadaYellow
                )
            )
        },

        bottomBar = {

            NavigationBar(
                containerColor = KaagadaRed,
                contentColor = KaagadaYellow
            ) {

                bottomNavItems.forEach { item ->

                    val selected =
                        currentRoute == item.screen.route

                    NavigationBarItem(

                        selected = selected,

                        onClick = {

                            when (item) {

                                is BottomNavItem.Home -> {}

                                is BottomNavItem.Learn ->
                                    onLearnAlphabets()

                                is BottomNavItem.Phrases ->
                                    onPhrases()

                                is BottomNavItem.Proverbs ->
                                    onProverbs()

                                is BottomNavItem.Profile ->
                                    onProfile()
                            }
                        },

                        icon = {

                            val icon = when (item.iconName) {

                                "Home" -> Icons.Default.Home

                                "School" -> Icons.Default.School

                                "RecordVoiceOver" ->
                                    Icons.Default.RecordVoiceOver

                                "List" -> Icons.Default.List

                                else -> Icons.Default.Person
                            }

                            Icon(
                                icon,
                                contentDescription = item.label
                            )
                        },

                        label = {
                            Text(
                                item.label,
                                fontSize = 10.sp
                            )
                        },

                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KaagadaRed,
                            selectedTextColor = KaagadaYellow,
                            indicatorColor = KaagadaYellow,
                            unselectedIconColor =
                                KaagadaYellow.copy(alpha = 0.6f),
                            unselectedTextColor =
                                KaagadaYellow.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }

    ) { innerPadding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(KaagadaRed)
                .verticalScroll(scrollState)
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            // TOP CARD

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        KaagadaYellow.copy(alpha = 0.1f)
                    )
                    .padding(16.dp),

                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.spacedBy(16.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "School",
                        modifier = Modifier.size(80.dp),
                        tint = KaagadaYellow
                    )

                    Column {

                        Text(
                            text = "Learn Kannada!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = KaagadaYellow
                        )

                        Text(
                            text =
                                "Explore alphabets, characters, and phrases at your own pace.",

                            fontSize = 14.sp,

                            color =
                                KaagadaYellow.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI TRANSLATOR

            Text(
                text = "AI Translator",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = KaagadaYellow,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(

                value = inputText,

                onValueChange = {
                    inputText = it
                },

                label = {
                    Text("Enter Kannada or English")
                },

                modifier = Modifier.fillMaxWidth(),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KaagadaYellow,
                    unfocusedBorderColor = KaagadaYellow,
                    focusedTextColor = KaagadaYellow,
                    unfocusedTextColor = KaagadaYellow,
                    focusedLabelColor = KaagadaYellow,
                    unfocusedLabelColor = KaagadaYellow,
                    cursorColor = KaagadaYellow
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(

                onClick = {

                    scope.launch {

                        isTranslating = true

                        translatedText =
                            translateWord(inputText)

                        isTranslating = false
                    }
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = KaagadaYellow,
                    contentColor = KaagadaRed
                )

            ) {

                Text("Translate")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isTranslating) {

                CircularProgressIndicator(
                    color = KaagadaYellow
                )
            }

            if (translatedText.isNotEmpty()) {

                Card(

                    colors = CardDefaults.cardColors(
                        containerColor = KaagadaYellow
                    ),

                    modifier = Modifier.fillMaxWidth()

                ) {

                    Text(
                        text = translatedText,

                        modifier = Modifier.padding(16.dp),

                        color = KaagadaRed,

                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LEARNING MODULES

            Text(
                text = "Learning Modules",

                fontSize = 20.sp,

                fontWeight = FontWeight.Bold,

                modifier = Modifier.fillMaxWidth(),

                textAlign = TextAlign.Start,

                color = KaagadaYellow
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeCard(
                title = "Learn Alphabets",
                subtitle = "Master the vowels and consonants",
                icon = Icons.Default.School,
                onClick = onLearnAlphabets
            )

            Spacer(modifier = Modifier.height(12.dp))

            HomeCard(
                title = "Identify Characters",
                subtitle = "Practice and Quiz",
                icon = Icons.Default.Quiz,
                onClick = onIdentifyAlphabets
            )

            Spacer(modifier = Modifier.height(12.dp))

            HomeCard(
                title = "Basic Phrases",
                subtitle = "Common daily conversations",
                icon = Icons.Default.RecordVoiceOver,
                onClick = onPhrases
            )

            Spacer(modifier = Modifier.height(12.dp))

            HomeCard(
                title = "Flashcards",
                subtitle = "Quick memory test",
                icon = Icons.Default.Style,
                onClick = onFlashcards
            )

            Spacer(modifier = Modifier.height(12.dp))

            HomeCard(
                title = "Kannada Proverbs",
                subtitle = "API & RecyclerView Demo",
                icon = Icons.Default.List,
                onClick = onProverbs
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
@Composable
fun HomeCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = KaagadaYellow
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(KaagadaRed),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = KaagadaYellow,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = KaagadaRed
                )

                Text(
                    text = subtitle,
                    fontSize = 15.sp,
                    color = KaagadaRed.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = KaagadaRed
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphabetLearningScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    var selectedAlphabet by remember { mutableStateOf<KannadaAlphabet?>(null) }

    DisposableEffect(Unit) {
        tts.value = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.value?.language = Locale("kn", "IN")
            }
        }
        onDispose {
            tts.value?.stop()
            tts.value?.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Learn Alphabets", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KaagadaRed,
                    titleContentColor = KaagadaYellow,
                    navigationIconContentColor = KaagadaYellow
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(KaagadaRed)
        ) {
            val swaras = AlphabetProvider.swaras
            val vyanjanas = AlphabetProvider.vyanjanas
            val allAlphabets = swaras + vyanjanas

            Box(modifier = Modifier.weight(1f)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(3) }) {
                        Text(
                            text = "Swaras (Vowels)",
                            style = MaterialTheme.typography.titleLarge,
                            color = KaagadaYellow,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(swaras) { alphabet ->
                        AlphabetCard(alphabet, isSelected = selectedAlphabet == alphabet) {
                            selectedAlphabet = alphabet
                            tts.value?.speak(alphabet.char, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    }
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(3) }) {
                        Text(
                            text = "Vyanjanas (Consonants)",
                            style = MaterialTheme.typography.titleLarge,
                            color = KaagadaYellow,
                            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                        )
                    }
                    items(vyanjanas) { alphabet ->
                        AlphabetCard(alphabet, isSelected = selectedAlphabet == alphabet) {
                            selectedAlphabet = alphabet
                            tts.value?.speak(alphabet.char, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    }
                }
            }

            if (selectedAlphabet != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = KaagadaYellow,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                val currentIndex = allAlphabets.indexOf(selectedAlphabet)
                                if (currentIndex > 0) {
                                    selectedAlphabet = allAlphabets[currentIndex - 1]
                                    tts.value?.speak(selectedAlphabet?.char ?: "", TextToSpeech.QUEUE_FLUSH, null, null)
                                }
                            },
                            enabled = allAlphabets.indexOf(selectedAlphabet) > 0,
                            colors = ButtonDefaults.buttonColors(containerColor = KaagadaRed, contentColor = KaagadaYellow)
                        ) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous")
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(selectedAlphabet?.char ?: "", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = KaagadaRed)
                            Text(selectedAlphabet?.pronunciation ?: "", fontSize = 16.sp, color = KaagadaRed.copy(alpha = 0.7f))
                        }

                        Button(
                            onClick = {
                                val currentIndex = allAlphabets.indexOf(selectedAlphabet)
                                if (currentIndex < allAlphabets.size - 1) {
                                    selectedAlphabet = allAlphabets[currentIndex + 1]
                                    tts.value?.speak(selectedAlphabet?.char ?: "", TextToSpeech.QUEUE_FLUSH, null, null)
                                }
                            },
                            enabled = allAlphabets.indexOf(selectedAlphabet) < allAlphabets.size - 1,
                            colors = ButtonDefaults.buttonColors(containerColor = KaagadaRed, contentColor = KaagadaYellow)
                        ) {
                            Icon(Icons.Default.ChevronRight, contentDescription = "Next")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlphabetCard(alphabet: KannadaAlphabet, isSelected: Boolean = false, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) White else KaagadaYellow
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, KaagadaRed) else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = alphabet.char,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = KaagadaRed
            )
            Text(
                text = alphabet.pronunciation,
                fontSize = 14.sp,
                color = KaagadaRed.copy(alpha = 0.7f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphabetIdentificationScreen(onBack: () -> Unit, viewModel: AuthViewModel = viewModel()) {
    // 1. Initialize state. We use a key for remember if we want to force reset,
    // but for now, simple remember is fine.
    var currentAlphabet by remember { mutableStateOf(AlphabetProvider.allAlphabets.random()) }
    var options by remember { mutableStateOf(generateOptions(currentAlphabet)) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    val scrollState = rememberScrollState()

    // 2. Clearer Next Question Logic
    fun nextQuestion() {
        val nextChar = AlphabetProvider.allAlphabets.random()
        currentAlphabet = nextChar
        options = generateOptions(nextChar)
        selectedOption = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Identify the Character", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KaagadaRed,
                    titleContentColor = KaagadaYellow,
                    navigationIconContentColor = KaagadaYellow
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(KaagadaRed)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score Display
            Card(
                colors = CardDefaults.cardColors(containerColor = KaagadaYellow),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Force the Text to update by using the state variable directly
                    Text(text = "Score: $score", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = KaagadaRed)
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = KaagadaRed)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Character Box
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(KaagadaYellow),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentAlphabet.char,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = KaagadaRed
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(text = "Choose the correct pronunciation", color = KaagadaYellow)

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Options Logic
            options.forEach { option ->
                // Check correctness relative to the CURRENT character
                val isCorrectAnswer = option == currentAlphabet.pronunciation

                val buttonColor = when {
                    selectedOption == null -> KaagadaYellow
                    isCorrectAnswer -> Color(0xFF4CAF50) // Correct lights up Green
                    selectedOption == option -> Color(0xFFF44336) // Wrong selection lights up Red
                    else -> KaagadaYellow.copy(alpha = 0.5f) // Others dim
                }

                Button(
                    onClick = {
                        if (selectedOption == null) {
                            selectedOption = option
                            if (isCorrectAnswer) {
                                score += 1
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = if (selectedOption == null) KaagadaRed else Color.White
                    )
                ) {
                    Text(text = option, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (selectedOption != null) {
                Button(
                    onClick = { nextQuestion() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KaagadaYellow, contentColor = KaagadaRed)
                ) {
                    Text("Next Character")
                }
            }
        }
    }
}

fun generateOptions(correct: KannadaAlphabet): List<String> {
    val wrong = AlphabetProvider.allAlphabets
        .filter { it.pronunciation != correct.pronunciation }
        .shuffled()
        .take(3)
        .map { it.pronunciation }
    return (wrong + correct.pronunciation).shuffled()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhrasesScreen(onBack: () -> Unit, viewModel: AuthViewModel = viewModel()) {
    val allPhrases = listOf(
        Phrase("Hello", "Namaskara", "ನಮಸ್ಕಾರ", "Greetings"),
        Phrase("Good morning", "Shubhodaya", "ಶುಭೋದಯ", "Greetings"),
        Phrase("Good night", "Shubharatri", "ಶುಭರಾತ್ರಿ", "Greetings"),
        Phrase("Thank you", "Dhanyavadagalu", "ಧನ್ಯವಾದಗಳು", "Essentials"),
        Phrase("Please", "Dayavittu", "ದಯವಿಟ್ಟು", "Essentials"),
        Phrase("Sorry", "Kshamisiri", "ಕ್ಷಮಿಸಿರಿ", "Essentials"),
        Phrase("How are you?", "Hegiddira?", "ಹೇಗಿದ್ದೀರಾ?", "Common"),
        Phrase("I am fine", "Nanu chennagiddene", "ನಾನು ಚೆನ್ನಾಗಿದ್ದೇನೆ", "Common"),
        Phrase("What is your name?", "Nimma hesaru enu?", "ನಿಮ್ಮ ಹೆಸರು ಏನು?", "Common"),
        Phrase("My name is...", "Nanna hesaru...", "ನನ್ನ ಹೆಸರು...", "Common"),
        Phrase("Where is...?", "Ellide...?", "ಎಲ್ಲಿದೆ...?", "Directions"),
        Phrase("Go straight", "Nera hogi", "ನೇರ ಹೋಗಿ", "Directions"),
        Phrase("Turn left", "Yadakke thirugi", "ಎಡಕ್ಕೆ ತಿರುಗಿ", "Directions"),
        Phrase("Turn right", "Balakke thirugi", "ಬಲಕ್ಕೆ ತಿರುಗಿ", "Directions"),
        Phrase("Water", "Neeru", "ನೀರು", "Food"),
        Phrase("I am hungry", "Hasivaguttide", "ಹಸಿವಾಗುತ್ತಿದೆ", "Food"),
        Phrase("Food", "Oota", "ಊಟ", "Food"),
        Phrase("How much?", "Eshtu?", "ಎಷ್ಟು?", "Shopping"),
        Phrase("Too expensive", "Tumba thura", "ತುಂಬಾ ದುಬಾರಿ", "Shopping"),
        Phrase("Yes", "Houdu", "ಹೌದು", "Basic"),
        Phrase("No", "Illa", "ಇಲ್ಲ", "Basic"),
        Phrase("Maybe", "Iruhu", "ಇರಬಹುದು", "Basic"),
        Phrase("Goodbye", "Hogi baruttene", "ಹೋಗಿ ಬರುತ್ತೇನೆ", "Greetings")
    )

    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        tts.value = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.value?.language = Locale("kn", "IN")
            }
        }
        onDispose {
            tts.value?.stop()
            tts.value?.shutdown()
        }
    }

    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All") + allPhrases.map { it.category }.distinct()

    val filteredPhrases = if (selectedCategory == "All") allPhrases else allPhrases.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Basic Phrases", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KaagadaRed,
                    titleContentColor = KaagadaYellow,
                    navigationIconContentColor = KaagadaYellow
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).background(KaagadaRed).fillMaxSize()) {
            androidx.compose.foundation.lazy.LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = KaagadaYellow,
                            selectedLabelColor = KaagadaRed,
                            labelColor = KaagadaYellow,
                            containerColor = KaagadaRed
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = KaagadaYellow,
                            selectedBorderColor = KaagadaYellow,
                            enabled = true,
                            selected = selectedCategory == category
                        )
                    )
                }
            }

            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPhrases.size) { index ->
                    val phrase = filteredPhrases[index]

                    Card(
                        onClick = {
                            tts.value?.speak(phrase.kannada, TextToSpeech.QUEUE_FLUSH, null, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = KaagadaYellow
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = phrase.kannada,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KaagadaRed,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = "Listen",
                                    tint = KaagadaRed
                                )
                            }
                            Text(
                                text = phrase.transliteration,
                                fontSize = 16.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = KaagadaRed.copy(alpha = 0.7f)
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = KaagadaRed.copy(alpha = 0.2f))
                            Text(
                                text = phrase.english,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = KaagadaRed
                            )
                        }
                    }
                }
            }
        }
    }
}


data class Phrase(val english: String, val transliteration: String, val kannada: String, val category: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit, onLogout: () -> Unit, viewModel: AuthViewModel = viewModel()) {
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var editUsername by remember { mutableStateOf(username) }
    var editEmail by remember { mutableStateOf(email) }
    var editPhone by remember { mutableStateOf(phone) }

    val context = LocalContext.current

    // Password Dialog State
    var showPasswordDialog by remember { mutableStateOf(false) }
    var currentPasswordInput by remember { mutableStateOf("") }
    var newPasswordInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                showPasswordDialog = false
                errorMessage = null
            },
            containerColor = White,
            title = { Text("Change Password", color = Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (errorMessage != null) {
                        Text(errorMessage!!, color = Color.Red, fontSize = 12.sp)
                    }

                    TextField(
                        value = currentPasswordInput,
                        onValueChange = { currentPasswordInput = it },
                        placeholder = { Text("Current Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF5F5F5))
                    )

                    TextField(
                        value = newPasswordInput,
                        onValueChange = { newPasswordInput = it },
                        placeholder = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF5F5F5))
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val isSuccessful = viewModel.verifyAndChangePassword(currentPasswordInput, newPasswordInput)
                    if (isSuccessful) {
                        showPasswordDialog = false
                        currentPasswordInput = ""
                        newPasswordInput = ""
                        errorMessage = null
                    } else {
                        errorMessage = "Current password is incorrect"
                    }
                }) {
                    Text("Update", color = KaagadaRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text("Cancel", color = Black)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isEditing) {
                            viewModel.updateProfile(editUsername, editEmail, editPhone)
                            isEditing = false
                        } else {
                            isEditing = true
                        }
                    }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Save" else "Edit"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KaagadaRed,
                    titleContentColor = KaagadaYellow,
                    navigationIconContentColor = KaagadaYellow,
                    actionIconContentColor = KaagadaYellow
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(KaagadaRed)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(KaagadaYellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, Modifier.size(80.dp), KaagadaRed)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isEditing) {
                        TextField(
                            value = editUsername,
                            onValueChange = { editUsername = it },
                            placeholder = { Text("Name") },
                            modifier = Modifier.padding(horizontal = 24.dp),
                            colors = TextFieldDefaults.colors(focusedContainerColor = White),
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Text(username, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = KaagadaYellow)
                    }
                    Text("Kannada Learner", fontSize = 16.sp, color = KaagadaYellow.copy(alpha = 0.8f))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileInfoItem(label = "Email", value = email, isEditing = isEditing, editValue = editEmail, onValueChange = { editEmail = it })
                ProfileInfoItem(label = "Phone", value = phone, isEditing = isEditing, editValue = editPhone, onValueChange = { editPhone = it })

                // Change Password Trigger
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPasswordDialog = true }
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Security", color = KaagadaYellow.copy(alpha = 0.7f), fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Change Password", color = KaagadaYellow, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Icon(Icons.Default.Lock, null, tint = KaagadaYellow, modifier = Modifier.size(20.dp))
                    }
                    HorizontalDivider(color = KaagadaYellow.copy(alpha = 0.3f), modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- SHARE INTENT BUTTON ---
                OutlinedButton(
                    onClick = {
                        val sendIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, "Learn Kannada with me on Kaagada!")
                            type = "text/plain"
                        }
                        val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, KaagadaYellow),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = KaagadaYellow)
                ) {
                    Icon(Icons.Default.Share, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share App", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // LOGOUT BUTTON
                Button(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KaagadaYellow, contentColor = KaagadaRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = KaagadaRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text("v1.0.0", fontSize = 12.sp, color = KaagadaYellow.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String, isEditing: Boolean, editValue: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = KaagadaYellow.copy(alpha = 0.7f), fontSize = 14.sp)
        if (isEditing) {
            TextField(
                value = editValue,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black
                ),
                shape = RoundedCornerShape(8.dp)
            )
        } else {
            Text(text = if (value.isEmpty()) "Not set" else value, color = KaagadaYellow, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            HorizontalDivider(color = KaagadaYellow.copy(alpha = 0.3f), modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(onBack: () -> Unit) {
    var currentAlphabet by remember { mutableStateOf(AlphabetProvider.allAlphabets.random()) }
    var isFlipped by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flashcards", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KaagadaRed,
                    titleContentColor = KaagadaYellow,
                    navigationIconContentColor = KaagadaYellow
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(KaagadaRed)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Tap the card to flip!",
                color = KaagadaYellow.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Flashcard
            Card(
                onClick = { isFlipped = !isFlipped },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = KaagadaYellow)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isFlipped) {
                        // Front Side
                        Text(text = currentAlphabet.char, fontSize = 100.sp, fontWeight = FontWeight.Bold, color = KaagadaRed)
                    } else {
                        // Back Side
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Pronunciation:", fontSize = 18.sp, color = KaagadaRed.copy(alpha = 0.6f))
                            Text(text = currentAlphabet.pronunciation, fontSize = 48.sp, fontWeight = FontWeight.Bold, color = KaagadaRed)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    currentAlphabet = AlphabetProvider.allAlphabets.random()
                    isFlipped = false
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KaagadaYellow, contentColor = KaagadaRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Next Card", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


// ── FEATURE 1 & 2: Fixed Proverbs API + AI Translation ───────────────────
//
// FEATURE 1 — API FIX:
//   • Replaced silent catch-all with granular error state so users know
//     whether the failure was a network issue or a server error.
//   • Added LoadingIndicator while the coroutine is in-flight.
//   • Replaced legacy AndroidView/RecyclerView with Compose LazyColumn —
//     eliminates the adapter re-bind bug where the list showed stale items.
//   • Fallback proverbs now include 'meaning' (English gloss) so the
//     offline experience is still useful.
//
// FEATURE 2 — AI TRANSLATION:
//   • Each proverb card has a "Translate" button that calls the Anthropic
//     API via a lightweight suspend function.
//   • Translation result is stored per-item in a SnapshotStateMap so
//     toggling one card doesn't recompose the entire list.
//   • Loading spinner per card prevents duplicate requests.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProverbsScreen(onBack: () -> Unit) {
    // ── State ──────────────────────────────────────────────────────────────
    var proverbs by remember { mutableStateOf<List<Proverb>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Per-card translation state:  proverbIndex -> translated string or null
    val translations = remember { mutableStateMapOf<Int, String?>() }
    val translatingIdx = remember { mutableStateMapOf<Int, Boolean>() }

    val scope = rememberCoroutineScope()

    // ── API call ───────────────────────────────────────────────────────────
    LaunchedEffect(Unit) {

        isLoading = true

        try {

            // RetrofitClient.api.getSample()

            proverbs = listOf(
                Proverb(
                    text = "ಕೈ ಕೆಸರಾದರೆ ಬಾಯಿ ಮೊಸರು",
                    meaning = "If the hands get dirty (with work), the mouth gets curd (reward)."
                ),
                Proverb(
                    text = "ದೇಶ ಸುತ್ತು ಕೋಶ ಓದು",
                    meaning = "Travel the country, read the dictionary — seek knowledge everywhere."
                ),
                Proverb(
                    text = "ತಾಳಿದವನು ಬಾಳಿಯಾನು",
                    meaning = "One who is patient will thrive."
                )
            )

        } catch (e: Exception) {

            errorMsg = e.message

        }

        isLoading = false
    }
    // ── UI ─────────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kannada Proverbs", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KaagadaRed,
                    titleContentColor = KaagadaYellow,
                    navigationIconContentColor = KaagadaYellow
                )
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = KaagadaRed)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(KaagadaRed)
                ) {
                    if (errorMsg != null) {
                        Surface(
                            color = KaagadaYellow.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠ $errorMsg",
                                color = KaagadaYellow,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(proverbs.size) { idx ->
                            val proverb = proverbs[idx]
                            val translation = translations[idx]
                            val isTranslating = translatingIdx[idx] == true

                            ProverbCard(
                                proverb = proverb,
                                translation = translation,
                                isTranslating = isTranslating,
                                onTranslate = {
                                    if (translation == null && !isTranslating) {
                                        scope.launch {
                                            translatingIdx[idx] = true
                                            translations[idx] = translateProverb(proverb.text)
                                            translatingIdx[idx] = false
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Proverb card ──────────────────────────────────────────────────────────
@Composable
private fun ProverbCard(
    proverb: Proverb,
    translation: String?,
    isTranslating: Boolean,
    onTranslate: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = KaagadaYellow),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Kannada text
            Text(
                text = proverb.text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = KaagadaRed
            )

            // English meaning (from API or fallback)
            if (proverb.meaning.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = proverb.meaning,
                    fontSize = 13.sp,
                    color = KaagadaRed.copy(alpha = 0.7f)
                )
            }

            Spacer(Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(8.dp))



            // AI translation section
            when {
                isTranslating -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = KaagadaRed
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Translating…", fontSize = 13.sp, color = KaagadaRed.copy(alpha = 0.6f))
                    }
                }
                translation != null -> {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = KaagadaRed.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "AI Translation",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = KaagadaRed.copy(alpha = 0.6f)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = translation,
                                fontSize = 14.sp,
                                color = KaagadaRed
                            )
                        }
                    }
                }

            }
        }
    }
}

// ── AI translation helper ─────────────────────────────────────────────────
// Calls the Anthropic API (claude-sonnet-4-20250514) to translate a Kannada
// proverb into English. The function is IO-dispatched and safe to call from
// a coroutine scope in a Composable.
private suspend fun translateProverb(kannadaText: String): String =
    withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY

            val url = URL(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"
            )

            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val prompt = """
                Translate the following Kannada proverb into English.
                Provide:
                1. A literal word-for-word translation.
                2. The idiomatic English equivalent meaning in one sentence.
                Keep the response under 60 words.

                Proverb: $kannadaText
            """.trimIndent()

            val jsonInputString = """
            {
              "contents": [
                {
                  "parts": [
                    {
                      "text": ${JSONObject.quote(prompt)}
                    }
                  ]
                }
              ]
            }
            """.trimIndent()

            conn.outputStream.use { os ->
                val input = jsonInputString.toByteArray(Charsets.UTF_8)
                os.write(input, 0, input.size)
            }

            val responseText = conn.inputStream.bufferedReader().use { it.readText() }

            val json = JSONObject(responseText)

            json.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)

                .getString("text")
                .trim()

        } catch (e: Exception) {
            "Translation unavailable: ${e.message}"
        }
    }
private suspend fun translateWord(text: String): String {

    return suspendCancellableCoroutine { continuation ->

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.KANNADA)
            .build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {

                translator.translate(text)
                    .addOnSuccessListener { translatedText ->

                        translator.close()

                        continuation.resume(translatedText)

                    }
                    .addOnFailureListener { e ->

                        translator.close()

                        continuation.resume(
                            "Translation failed: ${e.message}"
                        )
                    }

            }
            .addOnFailureListener { e ->

                translator.close()

                continuation.resume(
                    "Model download failed: ${e.message}"
                )
            }
    }
}