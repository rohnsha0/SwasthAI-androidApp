package com.rohnsha.medbuddyai.bottom_navbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rohnsha.medbuddyai.CommunityScreen
import com.rohnsha.medbuddyai.ExploreScreen
import com.rohnsha.medbuddyai.HomeScreen
import com.rohnsha.medbuddyai.MoreScreen
import com.rohnsha.medbuddyai.ScanResultScreen
import com.rohnsha.medbuddyai.ScanScreen
import com.rohnsha.medbuddyai.domain.photoCaptureViewModel

@Composable
fun bottomNavGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    val savePhotoViewModel= viewModel<photoCaptureViewModel>()
    NavHost(
        navController = navController,
        startDestination = bottomNavItems.Home.route
    ){
        composable(route = bottomNavItems.Home.route){
            HomeScreen(padding = padding, navController = navController)
        }
        composable(route = bottomNavItems.Community.route){
            CommunityScreen(padding = padding, navController = navController)
        }
        composable(route = bottomNavItems.Preferences.route){
            MoreScreen(padding = padding)
        }
        composable(route = bottomNavItems.Preferences.route){
            MoreScreen(padding = padding)
        }
        composable(route = bottomNavItems.Scan.route){
            ScanScreen(
                padding = padding,
                navController = navController,
                photoCaptureVM = savePhotoViewModel
            )
        }

        composable(
            route = bottomNavItems.Explore.route
        ){
            ExploreScreen(
                padding = padding,
                navController = navController
            )
        }

        composable(
            route = bottomNavItems.ScanResult.route,
        ){
            ScanResultScreen(
                padding = padding,
                viewModel = savePhotoViewModel
            )
        }
    }
}