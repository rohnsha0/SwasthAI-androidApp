package com.rohnsha.medbuddyai.bottom_navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
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
    object Community: bottomNavItems(
        title = "Community",
        route = "community",
        unselectedIcon = Icons.Outlined.Forum,
        selectedIcon = Icons.Filled.Forum
    )
    object Preferences: bottomNavItems(
        title = "Preferences",
        route = "Preferences",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    )

    object Scan: bottomNavItems(
        title = "scan",
        route = "scan",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    )

    object ScanResult: bottomNavItems(
        title = "ScanResults",
        route = "scan_result/",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    )

    object Explore: bottomNavItems(
        title = "Explore",
        route = "explore",
        unselectedIcon = Icons.Outlined.Explore,
        selectedIcon = Icons.Filled.Explore
    )

    object BMI: bottomNavItems(
        title = "Bmi",
        route = "bmi",
        unselectedIcon = Icons.Outlined.Explore,
        selectedIcon = Icons.Filled.Explore
    )
}