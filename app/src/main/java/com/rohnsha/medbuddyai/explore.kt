package com.rohnsha.medbuddyai

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.BrunchDining
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    padding: PaddingValues,
    navController: NavHostController
) {
    val scrollState= rememberScrollState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Explore",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BGMain
                )
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ) { values ->
        Column(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Explore",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 30.dp, start = 24.dp)
            )
            explore_tools(navController = navController)
            Text(
                text = "Wellbeing",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 18.dp, start = 24.dp)
            )
            ExploreWellbeing(navController = navController)
            Text(
                text = "Self awareness",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 18.dp, start = 24.dp)
            )
            ExploreSelfAware(navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ExploreSelfAware(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            explore_tabs(
                title = "Asthma",
                icon = Icons.Filled.MedicalInformation,
                weight = .49f,
                navController = navController,
                route = bottomNavItems.Scan.route
            )
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(
                title = "Diabetes",
                icon = Icons.Filled.MedicalInformation,
                weight = 1f,
                navController = navController,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            explore_tabs(
                title = "Asthma",
                icon = Icons.Filled.MedicalInformation,
                weight = .49f,
                navController = navController,
                route = bottomNavItems.Scan.route
            )
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(
                title = "Diabetes",
                icon = Icons.Filled.MedicalInformation,
                weight = 1f,
                navController = navController,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        val query = remember {
            mutableStateOf("")
        }
        TextField(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp)),
            value = query.value,
            onValueChange = { ji: String ->
                query.value= ji
            },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .padding(start = 13.dp)
                        .height(34.dp)
                        .width(34.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                            .background(Color.White, CircleShape)
                            .padding(2.dp),
                        imageVector = Icons.Filled.MonitorHeart,
                        contentDescription = "search icon"
                    )
                }
            },
            trailingIcon = {
                           if (query.value.isNotBlank()){
                               Icon(
                                   modifier = Modifier
                                       .height(24.dp)
                                       .width(24.dp)
                                       .padding(2.dp)
                                       .clickable {
                                                  query.value= ""
                                       },
                                   imageVector = Icons.Filled.Clear,
                                   contentDescription = "search icon"
                               )
                           }
            },
            placeholder = { Text(text = "Didn't find your disease? Search here!") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ViewDash,
                unfocusedContainerColor = ViewDash,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
            )
        )
    }
}

@Composable
fun ExploreWellbeing(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            explore_tabs(
                title = "BMI",
                icon = Icons.Filled.AccessibilityNew,
                weight = .49f,
                navController = navController,
                route = bottomNavItems.Scan.route
            )
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(
                title = "Water Alarm",
                icon = Icons.Filled.WaterDrop,
                weight = 1f,
                navController = navController,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            explore_tabs(
                title = "Calorie Tracker",
                icon = Icons.Filled.RestaurantMenu,
                weight = .49f,
                navController = navController,
                route = bottomNavItems.Scan.route
            )
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(
                title = "Habits",
                icon = Icons.Filled.Event,
                weight = 1f,
                navController = navController,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        DataListFull(
            title = "Diet Plan",
            subtitle = "that suits you best",
            data = "Personalised",
            additionData = "null",
            imageVector = Icons.Filled.BrunchDining,
            colorLogo = Color.White,
            additionalDataColor = lightTextAccent,
            colorLogoTint = Color.Black,
            onClickListener = {
                Log.d("logStatus", "clicked")
            }
        )
    }
}

@Composable
fun explore_tools(
    navController: NavHostController
) {
    val isDoctorExpanded = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        explore_home(navController = navController)
        Spacer(modifier = Modifier.height(12.dp))
        DataListFull(
            title = "Find Doctors",
            subtitle = "from Practo",
            data = "300+",
            additionData = "View Options",
            imageVector = Icons.Filled.MedicalServices,
            colorLogo = Color.White,
            additionalDataColor = lightTextAccent,
            colorLogoTint = Color.Black,
            onClickListener = {
                Log.d("logStatus", "clicked")
                isDoctorExpanded.value=!isDoctorExpanded.value
            }
        )
        Log.d("logStatus", isDoctorExpanded.value.toString())
        if (isDoctorExpanded.value){
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(
                visible = isDoctorExpanded.value,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ){
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                ) {
                    explore_tabs(
                        title = "By Location",
                        icon = Icons.Filled.CameraAlt,
                        weight = .49f,
                        navController = navController,
                        route = bottomNavItems.Scan.route
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    explore_tabs(
                        title = "By Domain",
                        icon = Icons.Filled.Biotech,
                        weight = 1f,
                        navController = navController,
                    )
                }
            }
        }
    }
}