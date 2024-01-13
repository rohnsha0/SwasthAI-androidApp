package com.rohnsha.medbuddyai

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReadMore
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.domain.viewmodels.communityVM
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customRed
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    padding: PaddingValues,
    navController: NavHostController,
    communityViewModel: communityVM
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "MedBuddy AI",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
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
        Column(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            communityViewModel.loginUser()
            Text(
                text = "Health Dashboard",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 30.dp, start = 24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DataListFull(
                title = "Air Quality Index",
                subtitle = "in Kolkata",
                data = "254 PM2.5",
                additionData = "Severe",
                imageVector = Icons.Outlined.Air,
                colorLogo = customRed
            )
            AddMoreDashWidget()
            Text(
                text = "Explore",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 18.dp, start = 24.dp)
            )
            explore_home(navController = navController)
            Text(
                text = "Read about Diseases",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 30.dp, start = 24.dp)
            )
            explore_diseases(navController = navController)
            Text(
                text = "Current Medical Affairs",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 30.dp, start = 24.dp)
            )
        }
    }
}

@Composable
fun AddMoreDashWidget() {
    Box(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, top = 14.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {

            }
            .drawBehind {
                drawRoundRect(color = formAccent, style = Stroke(width = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                ), cornerRadius = CornerRadius(x = 16.dp.toPx(), y = 16.dp.toPx()))
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
            explore_tabs(title = "Neural", icon = Icons.Filled.Psychology, weight = .49f, navController = navController,)
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(title = "Derma", icon = Icons.Filled.EmojiPeople, weight = 1f, navController = navController,)
        }
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        ) {
            explore_tabs(title = "Respiratory", icon = Icons.Filled.SelfImprovement, weight = .49f, navController = navController,)
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(title = "More", icon = Icons.Filled.ReadMore, weight = 1f, navController = navController)
        }
    }
}

@Composable
fun explore_home(
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
            explore_tabs(
                title = "Scan",
                icon = Icons.Filled.CameraAlt,
                weight = .4f,
                navController = navController,
                route = bottomNavItems.Scan.route
            )
            Spacer(modifier = Modifier.width(12.dp))
            explore_tabs(
                title = "Import Reports",
                icon = Icons.Filled.Biotech,
                weight = 1f,
                navController = navController,
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
            .animateContentSize(animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)),
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

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    HomeScreen(padding = PaddingValues(all = 0.dp), navController = rememberNavController(), communityVM())
}