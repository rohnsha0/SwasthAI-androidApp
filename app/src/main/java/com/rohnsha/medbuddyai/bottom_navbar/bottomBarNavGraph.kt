package com.rohnsha.medbuddyai.bottom_navbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rohnsha.medbuddyai.CommunityScreen
import com.rohnsha.medbuddyai.HomeScreen
import com.rohnsha.medbuddyai.MoreScreen
import com.rohnsha.medbuddyai.ScanCatogoryScreen
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
        composable(route = bottomNavItems.Insights.route){
            CommunityScreen(padding = padding)
        }
        composable(route = bottomNavItems.Settings.route){
            MoreScreen(padding = padding)
        }
        composable(route = bottomNavItems.Settings.route){
            MoreScreen(padding = padding)
        }
        composable(route = bottomNavItems.ScanCatogoricals.route){
            ScanCatogoryScreen(
                padding = padding,
                navController = navController,
                photoCaptureVM = savePhotoViewModel)
        }
        composable(route = bottomNavItems.Scan.route){
            ScanScreen(
                padding = padding,
                navController = navController,
                photoCaptureVM = savePhotoViewModel
            )
        }
        composable(
            route = bottomNavItems.ScanResult.route,
            arguments = listOf(
                navArgument("group"){
                    type= NavType.IntType
                },
                navArgument("serial_no"){
                    type= NavType.IntType
                }
            )
        ){
            ScanResultScreen(
                padding = padding,
                group = it.arguments?.getInt("group", 404).toString(),
                serial_number = it.arguments?.getInt("serial_no", 404).toString()
            )
        }
    }
}