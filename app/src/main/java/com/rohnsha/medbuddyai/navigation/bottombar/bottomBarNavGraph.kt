package com.rohnsha.medbuddyai.navigation.bottombar

import android.content.Context
import android.content.pm.PackageInfo
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rohnsha.medbuddyai.database.appData.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.database.appData.disease_questions.questionVM
import com.rohnsha.medbuddyai.database.userdata.chatbot.chatDB_VM
import com.rohnsha.medbuddyai.database.userdata.currentUser.currentUserDataVM
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.chatVM
import com.rohnsha.medbuddyai.domain.viewmodels.classificationVM
import com.rohnsha.medbuddyai.domain.viewmodels.communityVM
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.sideStateVM
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.domain.viewmodels.userAuthVM
import com.rohnsha.medbuddyai.screens.BMIScreen
import com.rohnsha.medbuddyai.screens.ChatBotScreen
import com.rohnsha.medbuddyai.screens.CommunityReply
import com.rohnsha.medbuddyai.screens.CommunityScreen
import com.rohnsha.medbuddyai.screens.DiseasesCatelogue
import com.rohnsha.medbuddyai.screens.ExploreScreen
import com.rohnsha.medbuddyai.screens.HomeScreen
import com.rohnsha.medbuddyai.screens.MoreScreen
import com.rohnsha.medbuddyai.screens.UserStatScreen
import com.rohnsha.medbuddyai.screens.auth.UserAuthScreen
import com.rohnsha.medbuddyai.screens.auth.WelcomeLogoScreen
import com.rohnsha.medbuddyai.screens.mAIScreen
import com.rohnsha.medbuddyai.screens.scan.DoctorScreen
import com.rohnsha.medbuddyai.screens.scan.ScanCategoryScreen
import com.rohnsha.medbuddyai.screens.scan.ScanQuestions
import com.rohnsha.medbuddyai.screens.scan.ScanResultScreen
import com.rohnsha.medbuddyai.screens.scan.ScanScreen
import kotlinx.coroutines.delay

@Composable
fun bottomNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    snackBarVM: snackBarToggleVM,
    packageInfo: PackageInfo,
    sideDrawerState: sideStateVM
) {
    val savePhotoViewModel= viewModel<photoCaptureViewModel>()
    val classifierVM= viewModel<classificationVM>()
    val communityVM= viewModel<communityVM>()
    val userAuth= viewModel<userAuthVM>()
    val currentUserVM= viewModel<currentUserDataVM>()
    val questionVM= viewModel<questionVM>()
    val _auth= FirebaseAuth.getInstance()
    val chatVM= viewModel<chatVM>()

    val username = remember {
        mutableStateOf("")
    }

    Log.d("username", "topNavGraph: ${_auth.currentUser!=null}")

    if (_auth.currentUser!=(null)){
        LaunchedEffect(key1 = true) {
            currentUserVM.getQueryData(isDefaultUser = true)
        }
        username.value= currentUserVM.userName.collectAsState().value
    }

    Log.d("username", "bottomNavGraph: ${username.value}")

    val dbRef= Firebase.database.reference
    userAuth.initialize(instance = _auth, dbReference = dbRef, currentUserVM)
    communityVM.initialize(instance = _auth, dbReference = dbRef, username = username.value)
    val scanHistoryviewModel= viewModel<scanHistoryViewModel>()
    val diseaseDBviewModel= viewModel<diseaseDBviewModel>()
    val chatdbVM= viewModel<chatDB_VM>()
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
        startDestination = if (userAuth.isUserUnAuthenticated()) bottomNavItems.LogoWelcome.route else bottomNavItems.Home.route
    ){
        composable(route = bottomNavItems.Home.route){
            HomeScreen(
                padding = padding,
                navController = navController,
                communityViewModel = communityVM,
                scanHistoryVM = scanHistoryviewModel, diseaseDBviewModel = diseaseDBviewModel, chatdbVm = chatdbVM, sideStateVM = sideDrawerState,
                snackBarToggleVM = snackBarVM, currentUserDataVM = currentUserVM
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
        composable(route = bottomNavItems.CommunityReply.route,
                arguments = listOf(
                    navArgument(postID){
                        type= NavType.StringType
                    }
                )
            ){
            CommunityReply(padding, postID = it.arguments?.getString(postID)!!, communityVM, snackBarVM)
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
                index = it.arguments!!.getInt(scanIndexKey),
                sideStateVM = sideDrawerState, currentUserDataVM = currentUserVM
            )
        }

        composable(
            route = bottomNavItems.ScanQA.route,
            arguments = listOf(
                navArgument(scanIndexKey){
                    type= NavType.IntType
                }
            )
        ){
            ScanQuestions(
                indexClassification = it.arguments!!.getInt(scanIndexKey),
                photoCaptureViewModel = savePhotoViewModel,
                sideStateVM = sideDrawerState,
                padding = padding,
                navHostController = navController
            )
        }

        composable(route = bottomNavItems.ScanCategory.route){
            ScanCategoryScreen(
                padding = padding,
                navController,
                photoCaptureViewModel = savePhotoViewModel,
                snackBarViewModel = snackBarVM,
                sideStateVM = sideDrawerState, currentUserDataVM = currentUserVM
            )
        }

        composable(
            route = bottomNavItems.DiseaseCatelogue.route,
            arguments = listOf(
                navArgument(domainSelection){
                    type= NavType.IntType
                }
            )
        ){
            DiseasesCatelogue(padding, diseaseDBviewModel, navController, it.arguments!!.getInt(
                domainSelection
            ))
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
                padding = padding,
                navController, chatdbVM
            )
        }

        composable(
            route = bottomNavItems.Chatbot.route,
            arguments = listOf(
                navArgument(chatID){
                    type= NavType.IntType
                },
                navArgument(chatMode){
                    type= NavType.IntType
                }
            )
        ){
            ChatBotScreen(
                paddingValues = padding,
                snackBarVM,
                scanHistoryViewModel = scanHistoryviewModel,
                chatdbVm = chatdbVM,
                chatID = it.arguments!!.getInt(chatID),
                mode = it.arguments!!.getInt(chatMode),
                diseaseDBviewModel = diseaseDBviewModel,
                sideStateVM = sideDrawerState,
                currentUserDataVM = currentUserVM,
                chatVM = chatVM
            )
        }

        composable(
            route = bottomNavItems.ScanResult.route,
            arguments = listOf(
                navArgument(scanResultKey){
                    type= NavType.IntType
                },
                navArgument(scanResultIndex){
                    type= NavType.IntType
                }
            )
        ){
            ScanResultScreen(
                padding = padding,
                chatVM = chatVM,
                navController = navController,
                viewModel = savePhotoViewModel,
                scanHistoryViewModel = scanHistoryviewModel,
                diseaseDBviewModel = diseaseDBviewModel,
                resultsLevel = it.arguments!!.getInt(scanResultKey),
                indexClassification = it.arguments!!.getInt(scanResultIndex),
                snackbarHostState = snackBarVM,
                communityVM = communityVM,
                currentUserDataVM = currentUserVM,
                chatdbVm = chatdbVM
            )
        }
        composable(
            route = bottomNavItems.DoctorScreen.route,
            arguments = listOf(
                navArgument(domainID){
                    type= NavType.IntType
                }
            )
        ){
            DoctorScreen(
                padding = padding,
                diseaseDBviewModel = diseaseDBviewModel,
                domain = it.arguments!!.getInt(domainID)
            )
        }
        composable(
            route = bottomNavItems.LogoWelcome.route
        ){
            WelcomeLogoScreen(
                navController = navController
            )
        }
        composable(
            route = bottomNavItems.userStatScreen.route,
            arguments = listOf(
                navArgument(userIndex){
                    type= NavType.IntType
                }
            )
        ){
            UserStatScreen(
                userIndexx = it.arguments!!.getInt(userIndex),
                navController = navController,padding = padding, scanHistoryViewModel = scanHistoryviewModel, diseaseDBviewModel = diseaseDBviewModel,
                chatdbVm = chatdbVM
            )
        }
        composable(
            route = bottomNavItems.userAuth.route,
            arguments = listOf(
                navArgument(authMode){
                    type= NavType.IntType
                }
            )
        ){
            UserAuthScreen(mode = it.arguments!!.getInt(authMode), navController = navController, userAuthVM = userAuth
                , snackBarToggleVM = snackBarVM)
        }
    }
}