package com.readsphere.app.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.readsphere.app.domain.model.ReadingPreferences
import com.readsphere.app.presentation.home.HomeScreen
import com.readsphere.app.presentation.home.HomeViewModel
import com.readsphere.app.presentation.library.LibraryScreen
import com.readsphere.app.presentation.library.LibraryViewModel
import com.readsphere.app.presentation.onboarding.OnboardingScreen
import com.readsphere.app.presentation.reader.ReaderScreen
import com.readsphere.app.presentation.search.SearchScreen
import com.readsphere.app.presentation.search.SearchViewModel
import com.readsphere.app.presentation.settings.SettingsScreen
import com.readsphere.app.presentation.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    preferences: ReadingPreferences,
    onUpdatePreferences: (ReadingPreferences) -> Unit,
    onboardingCompleted: Boolean,
    onOnboardingCompleted: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Determine if bottom bar should be shown
    val showBottomBar = currentDestination?.route in listOf(
        Routes.Home.route,
        Routes.Library.route,
        Routes.Search.route,
        Routes.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = NavigationBarDefaults.Elevation
                ) {
                    BottomNavItem.entries.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = when (item) {
                                        BottomNavItem.Home -> androidx.compose.material.icons.Icons.Filled.Home
                                        BottomNavItem.Library -> androidx.compose.material.icons.Icons.Filled.LibraryBooks
                                        BottomNavItem.Search -> androidx.compose.material.icons.Icons.Filled.Search
                                        BottomNavItem.Settings -> androidx.compose.material.icons.Icons.Filled.Settings
                                    },
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (onboardingCompleted) Routes.Home.route else Routes.Onboarding.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { fadeIn(initialAlpha = 0.3f) + slideInHorizontally(initialOffsetX = { it / 4 }) },
            exitTransition = { fadeOut(targetAlpha = 0.3f) },
            popEnterTransition = { fadeIn(initialAlpha = 0.3f) },
            popExitTransition = { fadeOut(targetAlpha = 0.3f) + slideOutHorizontally(targetOffsetX = { it / 4 }) }
        ) {
            composable(route = Routes.Onboarding.route) {
                OnboardingScreen(
                    onGetStarted = {
                        onOnboardingCompleted()
                        navController.navigate(Routes.Home.route) {
                            popUpTo(Routes.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(route = Routes.Home.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = viewModel,
                    onDocumentClick = { document ->
                        navController.navigate(Routes.Reader.createRoute(document.id))
                    }
                )
            }

            composable(route = Routes.Library.route) {
                val viewModel: LibraryViewModel = hiltViewModel()
                LibraryScreen(
                    viewModel = viewModel,
                    onDocumentClick = { document ->
                        navController.navigate(Routes.Reader.createRoute(document.id))
                    }
                )
            }

            composable(route = Routes.Search.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(
                    viewModel = viewModel,
                    onDocumentClick = { document ->
                        navController.navigate(Routes.Reader.createRoute(document.id))
                    }
                )
            }

            composable(route = Routes.Settings.route) {
                val viewModel: SettingsViewModel = hiltViewModel()
                SettingsScreen(
                    viewModel = viewModel,
                    preferences = preferences,
                    onUpdatePreferences = onUpdatePreferences
                )
            }

            composable(
                route = Routes.Reader.route,
                arguments = listOf(
                    navArgument("documentId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val documentId = backStackEntry.arguments?.getString("documentId") ?: return@composable
                ReaderScreen(
                    documentId = documentId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
