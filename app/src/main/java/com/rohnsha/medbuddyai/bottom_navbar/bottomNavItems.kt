package com.rohnsha.medbuddyai.bottom_navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AdminPanelSettings
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
    object Insights: bottomNavItems(
        title = "Community",
        route = "community",
        unselectedIcon = Icons.Outlined.Forum,
        selectedIcon = Icons.Filled.Forum
    )
    object Settings: bottomNavItems(
        title = "More",
        route = "more",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    )

    object ScanCatogoricals: bottomNavItems(
        title = "scan_category",
        route = "scan_category",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    )

    object Scan: bottomNavItems(
        title = "scan",
        route = "scan",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    )
    object ScanInter: bottomNavItems(
        title = "scanInter",
        route = "scanInter/{grp_index}",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    ){
        fun passGrp(grp: Int): String{
            return "scanInter/$grp"
        }
    }
    object ScanResult: bottomNavItems(
        title = "ScanResults",
        route = "scan_result/{group}/{serial_no}",
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        selectedIcon = Icons.Filled.AdminPanelSettings
    ){
        fun passGrpAndSerialNumber(
            grp: Int,
            serial_number: Int
        ) : String{
            return "scan_result/$grp/$serial_number"
        }
    }
}