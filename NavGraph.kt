package com.example.kaagada.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kaagada.ui.screens.*
import com.example.kaagada.ui.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object AlphabetLearning : Screen("alphabet_learning")
    object AlphabetIdentification : Screen("alphabet_identification")
    object Phrases : Screen("phrases")
    object Profile : Screen("profile")
    object Flashcards : Screen("flashcards")
    object Proverbs : Screen("proverbs")
}

// NEW: Defines the bottom nav bar items so the Scaffold in HomeScreen
//       can render them without the nav graph knowing their display details.
sealed class BottomNavItem(
    val screen: Screen,
    val label: String,
    val iconName: String          // used in HomeScreen to resolve Material icons
) {
    object Home    : BottomNavItem(Screen.Home, "Home", "Home")
    object Learn   : BottomNavItem(Screen.AlphabetLearning, "Learn", "School")
    object Phrases : BottomNavItem(Screen.Phrases, "Phrases", "RecordVoiceOver")
    object Proverbs: BottomNavItem(Screen.Proverbs, "Proverbs", "List")
    object Profile : BottomNavItem(Screen.Profile, "Profile", "Person")
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Learn,
    BottomNavItem.Phrases,
    BottomNavItem.Proverbs,
    BottomNavItem.Profile
)

@Composable
fun KaagadaNavGraph(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()

    val startDestination = when {
        isLoggedIn -> Screen.Home.route
        hasCompletedOnboarding -> Screen.Login.route
        else -> Screen.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Screen.Signup.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(Screen.Signup.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                }
            )
        }

        // UPDATED: HomeScreen now receives navController so it can drive
        //           the bottom nav bar internally.
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onLearnAlphabets = { navController.navigate(Screen.AlphabetLearning.route) },
                onIdentifyAlphabets = { navController.navigate(Screen.AlphabetIdentification.route) },
                onPhrases = { navController.navigate(Screen.Phrases.route) },
                onFlashcards = { navController.navigate(Screen.Flashcards.route) },
                onProverbs = { navController.navigate(Screen.Proverbs.route) },
                onProfile = { navController.navigate(Screen.Profile.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AlphabetLearning.route) {
            AlphabetLearningScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.AlphabetIdentification.route) {
            AlphabetIdentificationScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Phrases.route) {
            PhrasesScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Flashcards.route) {
            FlashcardScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Proverbs.route) {
            ProverbsScreen(onBack = { navController.popBackStack() })
        }
    }
}
