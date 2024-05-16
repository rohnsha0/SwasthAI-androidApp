package com.rohnsha.medbuddyai.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Biotech
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.EmojiPeople
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.ReadMore
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.database.userdata.chatbot.chatDB_VM
import com.rohnsha.medbuddyai.database.userdata.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.communityVM
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.customGreen
import com.rohnsha.medbuddyai.ui.theme.customRed
import com.rohnsha.medbuddyai.ui.theme.customYellow
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    padding: PaddingValues,
    navController: NavHostController,
    communityViewModel: communityVM,
    scanHistoryVM: scanHistoryViewModel,
    diseaseDBviewModel: diseaseDBviewModel,
    chatdbVm: chatDB_VM,
    snackBarToggleVM: snackBarToggleVM
) {

    val chatCount= remember {
        mutableStateOf(Int.MAX_VALUE)
    }

    LaunchedEffect(key1 = true) {
        chatCount.value= chatdbVm.getChatCounts()
    }
    val lastScans= scanHistoryVM.scanHistoryEntries.collectAsState().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row {
                        Text(
                            text = "Swasth",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight(600),
                            fontSize = 26.sp
                        )
                        Text(
                            text = "AI",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight(600),
                            fontSize = 26.sp,
                            color = customBlue
                        )
                    }
                        },
                actions = {
                    Image(
                        imageVector = Icons.Outlined.AdminPanelSettings,
                        contentDescription = "Show accuracy button",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp)
                            .clickable {
                                navController.navigate(bottomNavItems.Preferences.route)
                            }
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = BGMain
                )
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ) { values ->
        val scrollState= rememberScrollState()
        LazyColumn(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .scrollable(state = scrollState, orientation = Orientation.Vertical)
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
        ) {
            item {
                Text(
                    text = "Health Dashboard",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 30.dp, start = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                DataListFull(
                    title = "Air Quality Index",
                    subtitle = "in Kolkata",
                    data = "254 PM2.5",
                    additionData = "Severe",
                    imageVector = Icons.Outlined.Air,
                    colorLogo = customRed
                )
                DataListFull(
                    title = "BMI",
                    subtitle = "Body Mass Index",
                    data = "18.34",
                    additionData = "Normal",
                    imageVector = Icons.Outlined.Calculate,
                    colorLogo = customGreen
                )
                DataListFull(
                    title = "mPoints",
                    subtitle = "Fit Score",
                    data = "54",
                    additionData = "Imbalanced",
                    imageVector = Icons.Outlined.Speed,
                    colorLogo = customRed
                )
                AddMoreDashWidget()
            }
            item {
                Text(
                    text = "Explore",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 26.dp, start = 24.dp)
                )
                explore_home(navController = navController, snackBarToggleVM)
                Spacer(modifier = Modifier.height(6.dp))
                DataListFull(
                    title = "AI Symptom Checker",
                    subtitle = "check what's wrong",
                    imageVector = Icons.Outlined.SmartToy,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        navController.navigate(bottomNavItems.Chatbot.returnChatID(chatMode = 1, chatID =  chatCount.value+1))
                    }
                )
                val scope = rememberCoroutineScope()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "View More",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .clickable {
                                scope.launch { navController.navigate(bottomNavItems.Explore.route) }
                            },
                        color = customBlue
                    )
                }
            }
            item {
                Text(
                    text = "Read about Diseases",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 26.dp, start = 24.dp)
                )
                explore_diseases(navController = navController)
            }
            if (lastScans.size>0){
                item {
                    Text(
                        text = "Scan History",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(top = 18.dp, start = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            items(lastScans.take(n = 3)){data ->
                DataListFull(
                    title = data.title,
                    subtitle = data.domain,
                    data = data.domain,
                    additionData = data.timestamp.let {
                        val instant = Instant.ofEpochMilli(it)
                        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
                        formatter.format(dateTime)
                    },
                    imageVector = Icons.Filled.History,
                    colorLogo = customBlue,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        diseaseDBviewModel.inputNameToSearch(data) {
                            navController.navigate(
                                bottomNavItems.ScanResult.returnScanResIndex(
                                    1,
                                    9999
                                )
                            )
                        }
                        Log.d("logStatus", "clicked")
                    }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 21.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (lastScans.size>3){
                        Text(
                            text = "View More",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight(600),
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(top = 14.dp)
                                .clickable {
                                },
                            color = customBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddMoreDashWidget() {
    Box(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, top = 8.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {

            }
            .drawBehind {
                drawRoundRect(
                    color = formAccent, style = Stroke(
                        width = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                    ), cornerRadius = CornerRadius(x = 16.dp.toPx(), y = 16.dp.toPx())
                )
            },
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "+ Add More",
            fontWeight = FontWeight(600),
            fontFamily = fontFamily,
            fontSize = 19.sp,
            color = formAccent
        )
    }
}

@Composable
fun explore_diseases(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            explore_tabs(title = "Neural", icon = Icons.Outlined.Psychology, weight = .49f, onClickListener = {
                navController.navigate(bottomNavItems.DiseaseCatelogue.returnDiseaseCatelogue(3))
            })
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(title = "Derma", icon = Icons.Outlined.EmojiPeople, weight = 1f, onClickListener = {
                navController.navigate(bottomNavItems.DiseaseCatelogue.returnDiseaseCatelogue(8))
            })
        }
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        ) {
            explore_tabs(title = "Respiratory", icon = Icons.Outlined.SelfImprovement, weight = .49f, onClickListener = {
                navController.navigate(bottomNavItems.DiseaseCatelogue.returnDiseaseCatelogue(6))
            })
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(title = "Digestive", icon = Icons.Outlined.ReadMore, weight = 1f, onClickListener = {
                navController.navigate(bottomNavItems.DiseaseCatelogue.returnDiseaseCatelogue(1))
            })
        }
        Text(
            text = "View More",
            fontFamily = fontFamily,
            fontWeight = FontWeight(600),
            fontSize = 15.sp,
            color = customBlue,
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navController.navigate(bottomNavItems.DiseaseCatelogue.returnDiseaseCatelogue(Int.MAX_VALUE))
                }
        )
    }
}

@Composable
fun explore_home(
    navController: NavHostController,
    snackBarToggleVM: snackBarToggleVM
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            explore_tabs(
                title = "Scan",
                icon = Icons.Outlined.CameraAlt,
                weight = .4f,
                navController = navController,
                route = bottomNavItems.ScanCategory.route
            )
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(
                title = "Decode Reports",
                icon = Icons.Outlined.Biotech,
                weight = 1f,
                onClickListener = {
                    snackBarToggleVM.SendToast(
                        message = "Feature not ready yet!",
                        indicator_color = customYellow,
                        padding = PaddingValues(2.dp),
                        icon = Icons.Outlined.Engineering
                    )
                }
            )
        }
    }
}

@Composable
fun explore_tabs(
    title: String,
    icon: ImageVector,
    weight: Float,
    navController: NavHostController? = null,
    route: String= bottomNavItems.Community.route,
    onClickListener: (() -> Unit)? =null,
) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(weight)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
            .clickable {
                if (navController != null) {
                    navController.navigate(route)
                } else {
                    if (onClickListener != null) {
                        onClickListener()
                    }
                }
            }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            ),
        contentAlignment = Alignment.CenterStart
    ){
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                        .padding(2.dp),
                    imageVector = icon,
                    contentDescription = "cam_icon"
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 13.dp, end = 18.dp),
                text = title,
                fontWeight = FontWeight(600),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ScanCard() {
    Box(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, top = 30.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                .background(ViewDash, shape = RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 13.dp, start = 13.dp)
                        .height(34.dp)
                        .width(34.dp)
                        .background(Color.Black, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "scan icon",
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                            .padding(2.dp),
                        tint = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 13.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp,
                        fontFamily = fontFamily,
                        text = "Rohan Shaw",
                        letterSpacing = 0.1.sp
                    )
                    Text(
                        fontSize = 13.sp,
                        text = "20 | Male",
                        letterSpacing = 0.1.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp,
                        text = "PROFILE",
                        letterSpacing = 0.1.sp,
                        color = MaterialTheme.typography.bodyMedium.color.copy(alpha = .6f),
                        modifier = Modifier
                            .padding(top = 10.dp, end = 13.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 18.dp, start = 13.dp)
            ) {
                Text(
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily,
                    text = "Medical Condition: ",
                    letterSpacing = 0.1.sp,
                    fontSize = 15.sp
                )
                Text(
                    text = "Fit",
                    fontFamily = fontFamily,
                    letterSpacing = 0.1.sp,
                    fontSize = 15.sp
                    )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp, bottom = 12.dp, top = 3.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily,
                    text = "Last Scan: ",
                    letterSpacing = 0.1.sp,
                    fontSize = 15.sp
                )
                Text(
                    fontFamily = fontFamily,
                    text = "10 Aug, 2023 at 21:36",
                    letterSpacing = 0.1.sp,
                    fontSize = 15.sp
                )
            }
            /*
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                FilledTonalButton(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(start = 13.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = "camera_icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Capture")
                }
                FilledTonalButton(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(start = 13.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Collections,
                        contentDescription = "collections_icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Gallery")
                }
            }*/

        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    HomeScreen(padding = PaddingValues(all = 0.dp), navController = rememberNavController(), communityVM())
}*/