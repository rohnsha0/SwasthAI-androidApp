package com.rohnsha.medbuddyai.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.database.appData.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.database.userdata.chatbot.chatDB_VM
import com.rohnsha.medbuddyai.database.userdata.chatbot.chats.chatEntity
import com.rohnsha.medbuddyai.database.userdata.currentUser.currentUserDataVM
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistory
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryViewModel
import com.rohnsha.medbuddyai.navigation.bottombar.bottomNavItems
import com.rohnsha.medbuddyai.screens.scan.DataListFull
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStatScreen(
    userIndexx: Int,
    padding: PaddingValues,
    scanHistoryViewModel: scanHistoryViewModel,
    diseaseDBviewModel: diseaseDBviewModel,
    navController: NavHostController,
    chatdbVm: chatDB_VM,
    currentUserDataVM: currentUserDataVM,
) {
    val scope= rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Activity",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BGMain
                ),
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            currentUserDataVM.deleteUser(userIndexx)
                            chatdbVm.deleteChats(userIndexx)
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.PersonRemove,
                            contentDescription = "Delete Icon"
                        )
                    }
                },
                navigationIcon = {
                    Image(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "Show accuracy button",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(24.dp)
                            .padding(2.dp)
                            .clickable {

                            }
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ){ values ->

        val filteredScanHistory= remember {
            mutableStateListOf<scanHistory>()
        }
        val history= scanHistoryViewModel.scanHistoryEntries.collectAsState().value.filter { it.userIndex == userIndexx }
        filteredScanHistory.clear()
        filteredScanHistory.addAll(history)

        val filteredChatHistory= remember {
            mutableStateListOf<chatEntity>()
        }
        LaunchedEffect(key1 = Unit) {
            val chatHistory= chatdbVm.readChatHistory().filter { it.userIndex == userIndexx }
            filteredChatHistory.clear()
            filteredChatHistory.addAll(chatHistory)
        }

        LazyColumn(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(top = 30.dp)
        ){
            if (filteredScanHistory.isNotEmpty()){
                item {
                    Text(
                        text = "Scan Reports",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(bottom = 12.dp, start = 24.dp)
                    )
                }
                items(filteredScanHistory){ data ->
                    DataListFull(
                        title = data.title,
                        subtitle = data.timestamp.let {
                            val instant = Instant.ofEpochMilli(it)
                            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
                            formatter.format(dateTime)
                        },
                        imageVector = Icons.Filled.History,
                        colorLogo = customBlue,
                        additionalDataColor = lightTextAccent,
                        colorLogoTint = Color.White,
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
            }
            if (filteredChatHistory.isNotEmpty()){
                item {
                    Text(
                        text = "Chat History",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(top = 18.dp, start = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(filteredChatHistory){
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
            }
            if (filteredChatHistory.isEmpty() && filteredScanHistory.isEmpty()){
                item {
                    Text(
                        text = "No History",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp)
                }
            }
        }
    }

}