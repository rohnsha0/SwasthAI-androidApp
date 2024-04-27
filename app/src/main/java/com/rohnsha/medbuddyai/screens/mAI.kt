package com.rohnsha.medbuddyai.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun mAIScreen(
    padding: PaddingValues,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "AI Toolkit",
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
    ){ values ->
        LazyColumn(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 30.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
        ){
            item {
                Text(
                    text = "Chat",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 26.dp, start = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                DataListFull(
                    title = "QnA - General",
                    subtitle = "ai-based advise",
                    imageVector = Icons.Outlined.QuestionMark,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        Log.d("logStatus", "clicked")
                        navController.navigate(bottomNavItems.Chatbot.route)
                    }
                )
                DataListFull(
                    title = "QnA - Specialized Diseases",
                    subtitle = "ai-based specialized advice",
                    imageVector = Icons.Outlined.QuestionMark,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        Log.d("logStatus", "clicked")
                        navController.navigate(bottomNavItems.Chatbot.route)
                    }
                )
                DataListFull(
                    title = "AI Symptom Checker",
                    subtitle = "check what's wrong",
                    imageVector = Icons.Outlined.SmartToy,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        Log.d("logStatus", "clicked")
                    }
                )
            }
        }
    }
}

@Composable
fun FilterItems(
    text: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 9.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ViewDash)
            .clickable {

            }
            .padding(start = 12.dp, top = 4.dp, bottom = 4.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = fontFamily,
            color = lightTextAccent
        )
        Spacer(modifier = Modifier.width(14.dp))
        Image(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = "Dropdown",
            modifier = Modifier.size(18.dp)
        )
    }
}