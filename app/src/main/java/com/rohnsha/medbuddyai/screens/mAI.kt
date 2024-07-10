package com.rohnsha.medbuddyai.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.database.userdata.chatbot.chatDB_VM
import com.rohnsha.medbuddyai.database.userdata.chatbot.chats.chatEntity
import com.rohnsha.medbuddyai.domain.dataclass.moreActionsWithSubheader
import com.rohnsha.medbuddyai.navigation.bottombar.bottomNavItems
import com.rohnsha.medbuddyai.screens.scan.DataListFull
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mAIScreen(
    padding: PaddingValues,
    navController: NavHostController,
    chatdbVm: chatDB_VM
) {

    val chatCount= remember {
        mutableStateOf(Int.MAX_VALUE)
    }
    val chatHistory= remember {
        mutableStateOf(emptyList<chatEntity>())
    }

    LaunchedEffect(Unit) {
        chatCount.value= chatdbVm.getChatCounts()
        chatHistory.value= chatdbVm.readChatHistory()
        Log.d("dbCountInner", "chatcount: $chatCount, \n" +
                "chatHistory: ${chatHistory.value}")
    }

    Log.d("dbCount", "chatcount: $chatCount, \nchatHistory: ${chatHistory.value}")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = bottomNavItems.mAI.title,
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
                        navController.navigate(bottomNavItems.Chatbot.returnChatID(chatMode = 0, chatID = chatCount.value+1))
                    }
                )
                /*DataListFull(
                    title = "QnA - Specialized Diseases",
                    subtitle = "ai-based specialized advice",
                    imageVector = Icons.Outlined.QuestionMark,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        Log.d("logStatus", "clicked")
                        navController.navigate(bottomNavItems.Chatbot.returnChatID(chatMode = 0, chatID =  chatCount.value+1))
                    }
                )*/
                DataListFull(
                    title = "AI Symptom Checker",
                    subtitle = "check what's wrong",
                    imageVector = Icons.Outlined.SmartToy,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        Log.d("logStatus", "clicked")
                        navController.navigate(bottomNavItems.Chatbot.returnChatID(chatMode = 1, chatID =  chatCount.value+1))
                    }
                )
            }

            val taskSpecificBots= listOf(
                moreActionsWithSubheader(
                    title = "QnA - Pathology",
                    subheader = "Task Specific Chat",
                    onClick = {}
                ),
                moreActionsWithSubheader(
                    title = "QnA - Medicinology",
                    subheader = "Task Specific Chat",
                    onClick = {}
                ),
                moreActionsWithSubheader(
                    title = "QnA - Allergy",
                    subheader = "Task Specific Chat",
                    onClick = {}
                ),
            )
            item {
                Text(
                    text = "Task Specific Chat",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 26.dp, start = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(taskSpecificBots){
                DataListFull(
                    title = it.title,
                    subtitle = it.subheader,
                    imageVector = Icons.Outlined.Download,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        it.onClick()
                        Log.d("logStatus", "clicked")
                    }
                )
            }

            item {
                Text(
                    text = "Know Your Product",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 26.dp, start = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                DataListFull(
                    title = "Ask SwasthAI Admin",
                    subtitle = "get more technical info",
                    imageVector = Icons.Outlined.Info,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        Log.d("logStatus", "clicked")
                    }
                )
                DataListFull(
                    title = "Download Fine-tuned LLM",
                    subtitle = "powered by LLAMA3",
                    imageVector = Icons.Outlined.Download,
                    colorLogo = Color.White,
                    additionalDataColor = lightTextAccent,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        Log.d("logStatus", "clicked")
                    }
                )
                if (chatCount.value>0){
                    Text(
                        text = "Chat History",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(top = 18.dp, start = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            items(chatHistory.value.take(n = 3)){
                DataListFull(
                    title = when(it.mode){
                        0 -> "QnA"
                        1 -> "Symptom Checker"
                        else -> "Unknown"
                                         },
                    colorLogo = customBlue,
                    subtitle = "Chat ID: ${it.id}",
                    imageVector = Icons.Outlined.History,
                    onClickListener = {
                        navController.navigate(bottomNavItems.Chatbot.returnChatID(chatMode = it.mode, chatID = it.id))
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
                    if (chatHistory.value.size>3){
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