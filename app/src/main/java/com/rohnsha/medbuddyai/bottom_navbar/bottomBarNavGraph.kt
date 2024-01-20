package com.rohnsha.medbuddyai.bottom_navbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rohnsha.medbuddyai.BMIScreen
import com.rohnsha.medbuddyai.CommunityScreen
import com.rohnsha.medbuddyai.ExploreScreen
import com.rohnsha.medbuddyai.HomeScreen
import com.rohnsha.medbuddyai.MoreScreen
import com.rohnsha.medbuddyai.ScanResultScreen
import com.rohnsha.medbuddyai.ScanScreen
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.classificationVM
import com.rohnsha.medbuddyai.domain.viewmodels.communityVM
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.mAIScreen

@Composable
fun bottomNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    snackBarVM: snackBarToggleVM
) {
    val savePhotoViewModel= viewModel<photoCaptureViewModel>()
    val classifierVM= viewModel<classificationVM>()
    val communityVM= viewModel<communityVM>()
    val scanHistoryviewModel= viewModel<scanHistoryViewModel>()

    LaunchedEffect(key1 = true){
        scanHistoryviewModel.readScanHistory()
    }

    NavHost(
        navController = navController,
        startDestination = bottomNavItems.Home.route
    ){
        composable(route = bottomNavItems.Home.route){
            HomeScreen(
                padding = padding,
                navController = navController,
                communityViewModel = communityVM,
                scanHistoryviewModel
            )
        }
        composable(route = bottomNavItems.Community.route){
            CommunityScreen(
                padding = padding,
                navController = navController,
                snackBarViewModel = snackBarVM,
                communityViewModel = communityVM
            )
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
                photoCaptureVM = savePhotoViewModel,
                classifierVM = classifierVM
            )
        }

        composable(
            route = bottomNavItems.Explore.route
        ){
            ExploreScreen(
                padding = padding,
                navController = navController,
                scanHistoryViewModel = scanHistoryviewModel,
            )
        }

        composable(
            route = bottomNavItems.BMI.route
        ){
            BMIScreen(
                padding = padding,
                navController = navController
            )
        }

        composable(route = bottomNavItems.mAI.route){
            mAIScreen(
                padding = padding
            )
        }

        composable(
            route = bottomNavItems.ScanResult.route,
        ){
            ScanResultScreen(
                padding = padding,
                navController = navController,
                viewModel = savePhotoViewModel,
                scanHistoryViewModel = scanHistoryviewModel
            )
        }
    }
}