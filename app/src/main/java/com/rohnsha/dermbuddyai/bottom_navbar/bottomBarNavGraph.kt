package com.rohnsha.dermbuddyai.bottom_navbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rohnsha.dermbuddyai.CommunityScreen
import com.rohnsha.dermbuddyai.HomeScreen
import com.rohnsha.dermbuddyai.MoreScreen
import com.rohnsha.dermbuddyai.ScanScreen

@Composable
fun bottomNavGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = bottomNavItems.Home.route
    ){
        composable(route = bottomNavItems.Home.route){
            HomeScreen(padding = padding, navController = navController)
        }
        composable(route = bottomNavItems.Insights.route){
            CommunityScreen(padding = padding)
        }
        composable(route = bottomNavItems.Settings.route){
            MoreScreen(padding = padding)
        }
        composable(route = bottomNavItems.Settings.route){
            MoreScreen(padding = padding)
        }
        composable(route = bottomNavItems.Scan.route){
            ScanScreen(padding = padding)
        }
    }
}