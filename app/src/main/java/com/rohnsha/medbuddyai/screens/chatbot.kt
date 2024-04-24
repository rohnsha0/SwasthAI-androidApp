package com.rohnsha.medbuddyai.screens

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rohnsha.medbuddyai.domain.dataclass.messageDC
import com.rohnsha.medbuddyai.domain.viewmodels.chatVM
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(
    paddingValues: PaddingValues,
) {
    val scope= rememberCoroutineScope()
    val chatbotViewModel= viewModel<chatVM>()

    val messageField= remember {
        mutableStateOf("")
    }

    val messaageList= remember {
        mutableListOf<messageDC>()
    }
    
    LaunchedEffect(chatbotViewModel.messageCount.collectAsState().value) {
        chatbotViewModel.messagesList.collect{ message ->
            messaageList.add(message)
        }
    }
    LaunchedEffect(key1 = chatbotViewModel.messageCount.collectAsState().value) {
        val count= chatbotViewModel.messageCount
        Log.d("sizes", "vm: ${count}, size: ${messaageList.size}")
    }
    val scrollState = rememberLazyListState()

    LaunchedEffect(chatbotViewModel.messageCount.collectAsState().value) {
        if (messaageList.isNotEmpty()) {
            scrollState.animateScrollToItem(messaageList.size- 1)
        }
    }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Chat - QnA",
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
        Column(
            modifier = Modifier
                .padding(values)
                .padding(paddingValues)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(top = 30.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp, end = 24.dp),
                state = scrollState
            ) {
                items(messaageList){
                    Messages(messageInfo = it)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier= Modifier
                    .padding(bottom = 10.dp, start = 13.dp, end=10.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value =messageField.value,
                    onValueChange = { messageField.value= it },
                    modifier = Modifier
                        .weight(1f)
                        .background(color = ViewDash, shape = RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        containerColor = ViewDash
                    ),
                    placeholder = { Text(text = "Enter your query", color= lightTextAccent) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector =Icons.Outlined.Send, contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable {
                            scope.launch {
                                chatbotViewModel.chat(
                                    messageField.value,
                                    resetMessageFeild = {
                                        messageField.value = ""
                                    },
                                    onCompletion = { })
                            }
                        }
                        .background(customBlue)
                        .padding(10.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun Messages(
    messageInfo: messageDC,
) {
    if (messageInfo.message!=""){
        Row {
            if (!messageInfo.isBotMessage){
                Spacer(modifier = Modifier.weight(1f))
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .then(
                        if (messageInfo.isBotMessage) {
                            Modifier.padding(end = 20.dp)
                        } else {
                            Modifier.padding(start = 20.dp)
                        }
                    )
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                    .background(color = ViewDash, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.CenterStart
            ){
                Column {
                    Text(
                        text = messageInfo.message,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        modifier = Modifier
                            .padding(start = 13.dp, end = 24.dp, bottom = 8.dp, top = 13.dp)
                    )
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.End)
                            .padding(start = 13.dp, end = 24.dp, bottom = 5.dp, top = 3.dp),
                    ){
                        val formatter = SimpleDateFormat("MMMM dd, yyyy - HH:mm", Locale.getDefault())
                        Text(
                            text = "${if (messageInfo.isBotMessage) "SwasthAI Chat - QnA" else "You"} | ${formatter.format(
                                Date(messageInfo.timestamp)
                            )}",
                            fontSize = 10.sp,
                            fontFamily = fontFamily,
                            modifier = Modifier,
                            color = customBlue,
                            fontWeight = FontWeight(600)
                        )
                    }
                }
            }
            if (messageInfo.isBotMessage){
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}