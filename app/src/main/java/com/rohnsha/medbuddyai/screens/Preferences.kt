package com.rohnsha.medbuddyai.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.R
import com.rohnsha.medbuddyai.database.userdata.currentUser.currentUserDataVM
import com.rohnsha.medbuddyai.domain.dataclass.moreActions
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.navigation.bottombar.bottomNavItems
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.customYellow
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    padding: PaddingValues,
    navController: NavHostController,
    currentUserDataVM: currentUserDataVM,
    snackBarToggleVM: snackBarToggleVM
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = bottomNavItems.Preferences.title,
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
        val scope= rememberCoroutineScope()
        val name= remember {
            mutableStateOf("")
        }

        LaunchedEffect(key1 = true) {
            val userData= currentUserDataVM.getAllUsers().filter { it.isDefaultUser }[0]
            name.value= "${userData.fname} ${userData.lname}"
        }

        val profileAction= listOf(
            moreActions(title = "Personal Informations", onClick = {
                Log.d("action", "MoreScreen: Personal Informations")
                navController.navigate(bottomNavItems.ProfileScreen.route)
            }) ,
            moreActions("API Secrets") { navController.navigate(bottomNavItems.ApiScreen.route) }
        )
        val settingActions= listOf(
            moreActions("Account Deactivate & Deletion") {
                snackBarToggleVM.SendToast(
                    message = "Feature not ready yet!",
                    indicator_color = customYellow,
                    padding = PaddingValues(2.dp),
                    icon = Icons.Outlined.Engineering
                )
            },
            moreActions("Data Usage") { navController.navigate(bottomNavItems.documentations.returnDoc(0)) }
        )
        LazyColumn(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(top = 30.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .height(75.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(start = 18.dp)
                    ) {
                        Text(
                            text = "Hello,",
                            fontSize = 14.sp,
                            fontFamily = fontFamily
                        )
                        Text(
                            text = name.value,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(600),
                            fontFamily = fontFamily,
                            modifier = Modifier
                                .offset(y = (-2).dp)
                        )
                    }
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp)
                    ) {
                        Image(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile Avatar",
                            modifier = Modifier
                                .size(70.dp)
                                .background(ViewDash, CircleShape)
                                .padding(10.dp)
                        )
                    }
                }
            }
            item {
                TextSubHead(
                    text = "Profile Informations",
                    modifier = Modifier
                        .padding(start = 24.dp, top = 18.dp, bottom = 8.dp)
                )
            }
            items(profileAction){
                MoreOptions(data = it)
            }
            item {
                TextSubHead(
                    text = "Settings",
                    modifier = Modifier
                        .padding(start = 24.dp, top = 18.dp, bottom = 8.dp)
                )
            }
            items(settingActions){
                MoreOptions(data = it)
            }
            item {
                TextSubHead(
                    text = "About Us",
                    modifier = Modifier
                        .padding(start = 24.dp, top = 18.dp, bottom = 8.dp)
                )
            }
            item { AboutApp() }
        }
    }
}

@Composable
fun AboutApp() {
    Column {
        Row(
            modifier = Modifier
                .height(125.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                .background(color = ViewDash, shape = RoundedCornerShape(16.dp)),
        ){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 13.dp, top = 13.dp),
                horizontalAlignment = Alignment.Start
            ) {
                AboutUsTitleData(title = "Version", data = "0.6.2 Plant Sown")
                AboutUsTitleData(title = "Build Number", data = "2024.08.07.35")
                val context= LocalContext.current
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        imageVector = Icons.Filled.Code,
                        contentDescription = "github code",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White, CircleShape)
                            .clickable {
                                val githubUrl = "https://github.com/rohnsha0/SwasthAI-androidApp"

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
                                context.startActivity(intent)
                            }
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    /*
                    Image(
                        imageVector = Icons.Filled.StarHalf,
                        contentDescription = "rate code",
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.White, CircleShape)
                            .padding(4.dp)
                    )*/
                }
                AboutUsTitleData(title = "Maintainer", data = "Rohan Shaw", isLightAccent = true)
                Spacer(modifier = Modifier.height(13.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.logo_welcme),
                contentDescription = "logo",
                modifier = Modifier
                    .size(125.dp)
                    .padding(20.dp),
                colorFilter = ColorFilter.tint(color = customBlue)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AboutUsTitleData(
    title: String,
    data: String,
    isLightAccent: Boolean = false
) {
    Row {
        Text(
            text = "${title}: ",
            fontSize = 14.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight(600),
            modifier = Modifier,
            color = if (isLightAccent) lightTextAccent else Color.Black
        )
        Text(
            text = data,
            fontSize = 14.sp,
            fontFamily = fontFamily,
            color = if (isLightAccent) lightTextAccent else Color.Black
        )
    }
}

@Composable
fun MoreOptions(
    data: moreActions
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .height(54.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
                .clickable { data.onClick() },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier
                    .padding(horizontal = 18.dp),
                text = data.title,
                fontSize = 18.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ){
                Image(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "action icon",
                    colorFilter = ColorFilter.tint(color = formAccent),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun TextSubHead(
    text: String,
    modifier: Modifier = Modifier
        .padding(top = 18.dp, start = 24.dp)
) {
    Text(
        text = text,
        fontFamily = fontFamily,
        fontWeight = FontWeight(600),
        fontSize = 14.sp,
        modifier = modifier
    )
}