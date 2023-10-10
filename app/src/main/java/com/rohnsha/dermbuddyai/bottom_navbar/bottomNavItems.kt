package com.rohnsha.dermbuddyai.bottom_navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.ui.graphics.vector.ImageVector

sealed class bottomNavItems(
    val title: String,
    val route: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
) {
    object Home: bottomNavItems(
        title = "Home",
        route = "home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )
    object Insights: bottomNavItems(
        title = "Insights",
        route = "insights",
        unselectedIcon = Icons.Outlined.SpaceDashboard,
        selectedIcon = Icons.Filled.SpaceDashboard
    )
    object Settings: bottomNavItems(
        title = "More",
        route = "more",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    )
}