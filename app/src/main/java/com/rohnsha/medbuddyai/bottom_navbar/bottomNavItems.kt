package com.rohnsha.medbuddyai.bottom_navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Assistant
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

const val scanIndexKey= "indexScan"
const val scanResultKey= "scanResultKey"
const val scanResultIndex= "scanResultIndex"
const val chatID= "chatID"

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
        route = "scan/{$scanIndexKey}",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    ){
        fun returnScanIndex(index: Int): String{
            return this.route.replace(oldValue = "{$scanIndexKey}", newValue = index.toString())
        }
    }

    object ScanCategory: bottomNavItems(
        title = "scanCategory",
        route = "scanCategory",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    )

    object ScanResult: bottomNavItems(
        title = "ScanResults",
        route = "scan_result/{$scanResultKey}/{$scanResultIndex}",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    ){
        fun returnScanResIndex(level: Int, index: Int): String {
            return "scan_result/$level/$index"
        }
    }

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

    object Chatbot: bottomNavItems(
        title = "chat",
        route = "chat/{$chatID}",
        unselectedIcon = Icons.Outlined.Explore,
        selectedIcon = Icons.Filled.Explore
    ){
        fun returnChatID(chatID: Int): String{
            return "chat/$chatID"
        }
    }

    object mAI: bottomNavItems(
        title = "sAI",
        route = "m_ai",
        unselectedIcon = Icons.Outlined.Assistant,
        selectedIcon = Icons.Filled.Assistant
    )
}