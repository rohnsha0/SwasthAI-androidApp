package com.rohnsha.medbuddyai.bottom_navbar

import android.content.Context
import android.content.pm.PackageInfo
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rohnsha.medbuddyai.BMIScreen
import com.rohnsha.medbuddyai.CommunityScreen
import com.rohnsha.medbuddyai.ExploreScreen
import com.rohnsha.medbuddyai.HomeScreen
import com.rohnsha.medbuddyai.MoreScreen
import com.rohnsha.medbuddyai.ScanCategoryScreen
import com.rohnsha.medbuddyai.ScanResultScreen
import com.rohnsha.medbuddyai.ScanScreen
import com.rohnsha.medbuddyai.database.userdata.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.classificationVM
import com.rohnsha.medbuddyai.domain.viewmodels.communityVM
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.mAIScreen
import kotlinx.coroutines.delay

@Composable
fun bottomNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    snackBarVM: snackBarToggleVM,
    packageInfo: PackageInfo,
) {
    val savePhotoViewModel= viewModel<photoCaptureViewModel>()
    val classifierVM= viewModel<classificationVM>()
    val communityVM= viewModel<communityVM>()
    val scanHistoryviewModel= viewModel<scanHistoryViewModel>()
    val diseaseDBviewModel= viewModel<diseaseDBviewModel>()
    val context: Context= LocalContext.current

    LaunchedEffect(key1 = true){
        scanHistoryviewModel.readScanHistory()
    }

    Log.d("dbStatus", "state: ${diseaseDBviewModel.updatingDiseaseDB.collectAsState().value}")
    LaunchedEffect(key1 = true){
        Log.d("dbStatus", "Starting VM")
        delay(750L)
        diseaseDBviewModel.fetchUpdatedDB(versionName = packageInfo.versionName, context = context)
        Log.d("dbStatus", "Ending VM")
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
        composable(
            route = bottomNavItems.Scan.route,
            arguments = listOf(
                navArgument(scanIndexKey){
                    type= NavType.IntType
                }
            )
        ){
            ScanScreen(
                padding = padding,
                navController = navController,
                photoCaptureVM = savePhotoViewModel,
                classifierVM = classifierVM,
                index = it.arguments!!.getInt(scanIndexKey)
            )
        }

        composable(route = bottomNavItems.ScanCategory.route){
            ScanCategoryScreen(
                padding = padding,
                navController
            )
        }

        composable(
            route = bottomNavItems.Explore.route
        ){
            ExploreScreen(
                padding = padding,
                navController = navController,
                scanHistoryViewModel = scanHistoryviewModel,
                diseaseDBviewModel
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
            arguments = listOf(
                navArgument(scanResultKey){
                    type= NavType.IntType
                }
            )
        ){
            ScanResultScreen(
                padding = padding,
                navController = navController,
                viewModel = savePhotoViewModel,
                scanHistoryViewModel = scanHistoryviewModel,
                diseaseDBviewModel = diseaseDBviewModel,
                resultsLevel = it.arguments!!.getInt(scanResultKey)
            )
        }
    }
}