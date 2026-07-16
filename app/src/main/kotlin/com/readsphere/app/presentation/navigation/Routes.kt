package com.readsphere.app.presentation.navigation

sealed class Routes(val route: String) {
    data object Onboarding : Routes("onboarding")
    data object Home : Routes("home")
    data object Library : Routes("library")
    data object Search : Routes("search")
    data object Settings : Routes("settings")
    data object Reader : Routes("reader/{documentId}") {
        fun createRoute(documentId: String) = "reader/$documentId"
    }
}

enum class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String,
    val selectedIcon: String
) {
    Home(
        route = Routes.Home.route,
        label = "Home",
        icon = "home_outlined",
        selectedIcon = "home_filled"
    ),
    Library(
        route = Routes.Library.route,
        label = "Library",
        icon = "library_books_outlined",
        selectedIcon = "library_books_filled"
    ),
    Search(
        route = Routes.Search.route,
        label = "Search",
        icon = "search",
        selectedIcon = "search"
    ),
    Settings(
        route = Routes.Settings.route,
        label = "Settings",
        icon = "settings_outlined",
        selectedIcon = "settings_filled"
    )
}
