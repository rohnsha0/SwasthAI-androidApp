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
const val postID= "postID"
const val chatMode= "chatMode"
const val domainSelection= "domianSelection"
const val authMode= "authMode"

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
    object DiseaseCatelogue: bottomNavItems(
        title = "Diseases Catalogue",
        route = "disease_catalogue/{$domainSelection}",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ){
        fun returnDiseaseCatelogue(domainSelection: Int): String{
            return "disease_catalogue/$domainSelection"
        }
    }
    object Community: bottomNavItems(
        title = "Community",
        route = "community",
        unselectedIcon = Icons.Outlined.Forum,
        selectedIcon = Icons.Filled.Forum
    )

    object CommunityReply: bottomNavItems(
        title = "CommunityReply",
        route = "communityReply/{$postID}",
        unselectedIcon = Icons.Outlined.Forum,
        selectedIcon = Icons.Filled.Forum
    ){
        fun returnPostID(postID: String): String{
            return "communityReply/$postID"
        }
    }

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
        route = "chat/{$chatMode}/{$chatID}",
        unselectedIcon = Icons.Outlined.Explore,
        selectedIcon = Icons.Filled.Explore
    ){
        fun returnChatID(chatID: Int, chatMode: Int): String{
            return "chat/$chatMode/$chatID"
        }
    }

    object mAI: bottomNavItems(
        title = "sAI",
        route = "m_ai",
        unselectedIcon = Icons.Outlined.Assistant,
        selectedIcon = Icons.Filled.Assistant
    )

    object LogoWelcome: bottomNavItems(
        title = "logoWelcome",
        route = "logoWelcome",
        unselectedIcon = Icons.Outlined.Assistant,
        selectedIcon = Icons.Filled.Assistant
    )

    object userAuth: bottomNavItems(
        title = "userAuth",
        route = "userAuth/{$authMode}",
        unselectedIcon = Icons.Outlined.Assistant,
        selectedIcon = Icons.Filled.Assistant
    ){
        fun returnAuthMode(authMode: Int): String{
            return "userAuth/$authMode"
        }
    }
}