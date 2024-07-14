package com.rohnsha.medbuddyai.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.outlined.Attachment
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material.icons.outlined.Merge
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.ShortText
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohnsha.medbuddyai.database.appData.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.database.appData.symptoms.symptomDC
import com.rohnsha.medbuddyai.database.userdata.chatbot.chatDB_VM
import com.rohnsha.medbuddyai.database.userdata.chatbot.messages.messageEntity
import com.rohnsha.medbuddyai.database.userdata.currentUser.currentUserDataVM
import com.rohnsha.medbuddyai.database.userdata.currentUser.fieldValueDC
import com.rohnsha.medbuddyai.domain.dataclass.moreActions
import com.rohnsha.medbuddyai.domain.viewmodels.chatVM
import com.rohnsha.medbuddyai.domain.viewmodels.sideStateVM
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.navigation.sidebar.screens.sideBarModifier
import com.rohnsha.medbuddyai.screens.scan.DataListFull
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.customYellow
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private lateinit var currentUserDataVModel: currentUserDataVM
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(
    paddingValues: PaddingValues,
    snackBarToggleVM: snackBarToggleVM,
    diseaseDBviewModel: diseaseDBviewModel,
    chatdbVm: chatDB_VM,
    chatID: Int,
    sideStateVM: sideStateVM,
    currentUserDataVM: currentUserDataVM,
    chatVM: chatVM,
    mode: Int //0 -> qna, 1 -> ai_symptoms_checker, 2 -> chat with attachments
) {
    Log.d("chatDB", chatID.toString())
    val scope= rememberCoroutineScope()
    val chatbotViewModel= chatVM
    currentUserDataVModel= currentUserDataVM

    val messageFieldState= remember {
        mutableStateOf(mode==0 || mode==2)
    }

    val bomState= remember {
        mutableStateOf(false)
    }
    val bomStateDUser= remember {
        mutableStateOf(false)
    }

    val messageField= remember {
        mutableStateOf("")
    }

    val messaageList= remember {
        mutableListOf<messageEntity>()
    }
    val collectingSymptoms= remember {
        mutableStateOf(true)
    }
    val responseSymptom= remember {
        mutableStateOf("")
    }
    val detectedSymptom= remember {
        mutableListOf<symptomDC>()
    }
    val optionEnabled= remember {
        mutableStateOf(false)
    }

    val symptoms= remember {
        mutableListOf<symptomDC>()
    }

    val currentUser= currentUserDataVModel.defaultUserIndex.collectAsState().value
    val chatM= chatVM.messageWAttachment.collectAsState().value

    if (mode==2){
        LaunchedEffect(key1 = true) {
            if (chatM.message != ""){
                chatVM.chat(
                    message = chatM.message,
                    resetMessageFeild = {
                        messageField.value = ""
                        chatVM.resetChatWAttachment()
                    },
                    vmChat = chatdbVm,
                    chatID = chatID,
                    mode = mode,
                    currentUserIndex = currentUser
                )
            }
        }
        Log.d("optionTxtFieldStateChat", chatM.toString())
    }

    LaunchedEffect(key1 = mode == 1) {
        diseaseDBviewModel.readSymptoms().forEach { symptoms.add(it) }
    }

    LaunchedEffect(key1 = true) {
        chatdbVm.readChatWithMessages(chatID).forEach {
            it.messages.forEach {
                messaageList.add(it)
            }
        }
    }

    LaunchedEffect(chatbotViewModel.messageCount.collectAsState().value) {
        chatbotViewModel.messagesList.collect{ message ->
            Log.d("chatList", message.toString())
            messaageList.add(message)
        }
    }

    Log.d("chatList", messaageList.toString())
    LaunchedEffect(key1 = chatbotViewModel.messageCount.collectAsState().value) {
        val count= chatbotViewModel.messageCount
        Log.d("sizes", "vm: ${count.value}, size: ${messaageList.size}")
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
                        text = when(mode){
                            0, 2 -> "QnA"
                            1 -> "Symptom Checker"
                            else -> { "Undetected categorization of chat mode" }
                        },
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Menu Icon"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { bomStateDUser.value = true }) {
                        Icon(
                            imageVector = Icons.Default.SettingsSuggest,
                            contentDescription = "Menu Icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BGMain
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .then(sideBarModifier(sideStateVM = sideStateVM)),
        containerColor = BGMain
    ){ values ->

        if (bomStateDUser.value){
            ModalBottomSheet(
                onDismissRequest = { bomStateDUser.value = false },
                containerColor = Color.White
            ) {
                BOMChangeDUser(currentUserDataVM = currentUserDataVM) {
                    bomStateDUser.value= false
                    currentUserDataVM.switchDefafultUser(it)
                }
            }
        }

        if (bomState.value){
            val selectedSymptom= remember {
                mutableStateOf<symptomDC>(symptomDC("", ""))
            }

            ModalBottomSheet(onDismissRequest = { bomState.value= false }, containerColor = Color.White) {
                ChatBOM(context = {
                    selectedSymptom.value= it
                    if (it !in detectedSymptom){
                        detectedSymptom.add(it)
                        messageField.value= "I am having ${detectedSymptom.joinToString(", ") { it.symptom }.lowercase()}"
                        scope.launch {
                            chatbotViewModel.symptomChat(
                                symptom = it.symptomAbbreviation,
                                selectedDisease = selectedSymptom.value,
                                vmChat = chatdbVm,
                                chatID = chatID,
                                outcome = { sym ->
                                    if (sym !in detectedSymptom){
                                        //detectedSymptom.add(sym)
                                    }
                                    optionEnabled.value= true
                                },
                                diseaseDBviewModel = diseaseDBviewModel, currentUserIndex = currentUser
                            )
                        }
                    } },
                    state = {
                        bomState.value= it
                            },
                    symptoms = symptoms,
                    diseaseDBviewModel = diseaseDBviewModel,
                    detectedSymp = detectedSymptom
                )
            }
        }
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
                state = scrollState,
            ) {
                if (messaageList.isEmpty()){
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Center
                        ){
                            Text(
                                text = when(mode){
                                    0 -> "You are chatting with SwasthAI QnA Bot trained to answer general" +
                                            " questions about medical"
                                    1 -> "You are chatting with SwasthAI Symptom checker bot trained to detect possible" +
                                            "symtoms associated with a disease"
                                    else -> { "Undetected categorization of chat mode" }
                                },
                                color = lightTextAccent,
                                fontFamily = fontFamily,
                                fontSize = 13.sp,
                                modifier = Modifier.align(Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                items(messaageList){
                    if (!it.isError){
                        Messages(messageInfo = it)
                    } else{
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Center){
                            Text(
                                text = it.message,
                                color = lightTextAccent,
                                fontFamily = fontFamily,
                                fontSize = 10.sp,
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
            val listGreet= listOf(
                moreActions("Yes, I have symptoms") {
                    var selectedSymptom = symptomDC("", "")
                    scope.launch {
                        chatbotViewModel.symptomChat(
                            symptom = detectedSymptom.joinToString(", ") { it.symptomAbbreviation },
                            vmChat = chatdbVm,
                            chatID = chatID,
                            outcome = {
                                selectedSymptom= it
                                if (it !in detectedSymptom){
                                    detectedSymptom.add(it)
                                    messageField.value= "I am having ${ detectedSymptom.joinToString(", ") { it.symptom }.lowercase() }"
                                }
                                optionEnabled.value = true
                            },
                            diseaseDBviewModel = diseaseDBviewModel, currentUserIndex = currentUser,
                            selectedDisease = selectedSymptom
                        )
                        Log.d("selectedSymptom", selectedSymptom.toString())
                    }
                },
                moreActions("No") {
                    optionEnabled.value = true
                    scope.launch {
                        chatbotViewModel.endSymptomTest(1, chatID)
                        optionEnabled.value = false
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedVisibility(visible = optionEnabled.value) {
                LazyRow(
                    Modifier.padding(start = 6.dp)
                ) {
                    item { Spacer(modifier = Modifier.width(6.dp)) }
                    items(listGreet){
                        Text(
                            text = it.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(600),
                            fontFamily = fontFamily,
                            color = customBlue,
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 10.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ViewDash)
                                .clickable { it.onClick() }
                                .padding(vertical = 3.dp, horizontal = 14.dp)
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier= Modifier
                    .padding(bottom = 10.dp, start = 13.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value =messageField.value,
                    onValueChange = { messageField.value= it },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = ViewDash)
                        .then(
                            if (!messageFieldState.value) {
                                Modifier.clickable {
                                    bomState.value = true
                                }
                            } else {
                                Modifier
                            }
                        ),
                    shape= RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ViewDash,
                        unfocusedContainerColor = ViewDash,
                        disabledContainerColor = ViewDash,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    placeholder = { Text(
                        text = if(messageFieldState.value){
                            "Enter your query"
                        } else{
                            "Select relevant option"
                        },
                        fontFamily = fontFamily,
                        color= lightTextAccent) },
                    enabled = messageFieldState.value
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector =Icons.Outlined.Send, contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable {
                            scope.launch {
                                if (messageField.value != "") {
                                    chatbotViewModel.chat(
                                        messageField.value,
                                        resetMessageFeild = {
                                            if (mode == 1) {
                                                detectedSymptom.clear()
                                            }
                                            messageField.value = ""
                                        },
                                        vmChat = chatdbVm,
                                        chatID = chatID,
                                        mode = mode,
                                        currentUserIndex = currentUser
                                    )
                                    optionEnabled.value = false
                                } else {
                                    snackBarToggleVM.SendToast(
                                        message = "Enter a message to be sent",
                                        indicator_color = customYellow,
                                        padding = PaddingValues(2.dp),
                                        icon = Icons.Outlined.Warning
                                    )
                                }
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
    messageInfo: messageEntity,
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
                    .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
                    .padding(top = 13.dp),
                contentAlignment = Alignment.CenterStart
            ){
                Column {
                    if (!messageInfo.isBotMessage){
                        Row(modifier = Modifier
                            .padding(start = 13.dp, end = 24.dp, bottom = 0.dp)
                            .clickable {

                            }
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(BGMain)
                            .padding(vertical = 2.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            Icon(
                                imageVector = Icons.Outlined.Attachment,
                                modifier = Modifier.size(12.dp),
                                contentDescription = "attachment icon",

                                )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Includes Scan Report",
                                fontSize = 10.sp,
                                fontFamily = fontFamily,
                                modifier = Modifier,
                                color = Color.Black,
                                //fontWeight = FontWeight(600)
                            )
                        }
                    }
                    Text(
                        text = messageInfo.message,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        modifier = Modifier
                            .padding(start = 13.dp, end = 24.dp, bottom = 8.dp)
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

@Composable
fun ChatBOM(
    context: (symptomDC) -> Unit,
    state: (Boolean)-> Unit,
    symptoms: List<symptomDC>,
    diseaseDBviewModel: diseaseDBviewModel,
    detectedSymp: MutableList<symptomDC>
) {
    val selectedData= remember {
        mutableStateOf(symptomDC(symptom = "", symptomAbbreviation = ""))
    }
    val content= remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = selectedData.value) {
        content.value= selectedData.value.symptom
    }

    val listFetched= remember {
        mutableStateOf(symptoms)
    }

    LaunchedEffect(key1 = content.value) {
        if (content.value.isNotEmpty()){
            val list = diseaseDBviewModel.searchSymptom(content.value)
            listFetched.value = list
        } else {
            listFetched.value= symptoms
        }
        Log.d("symptoms", listFetched.toString())
    }
    val selectedDataInList= remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = content.value) {
        if (selectedDataInList.value){
            selectedDataInList.value= false
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        item {
            Row {
                Text(
                    modifier = Modifier,
                    text = "Symptom Search",
                    fontSize = 19.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .clickable {
                            selectedDataInList.value= selectedData.value in detectedSymp
                            context(selectedData.value)
                            if (!selectedDataInList.value){
                                state(false)
                            }
                        },
                    text = "Save",
                    fontSize = 17.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            TextInputThemed(
                value = content.value,
                onValueChanged = { content.value= it },
                label = "Enter contents",
                icon = Icons.Outlined.ShortText,
                onClose = { content.value = "" },
                singleLine = false,
                errorBool = selectedDataInList.value,
                errorText = "Symptom already selected"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(listFetched.value){
            SymptomsList(title = it.symptom, onClickListener = {
                selectedData.value= it
            })
        }
        item {
            Spacer(modifier = Modifier.height(45.dp))
        }
    }
}

@Composable
fun SymptomsList(title: String, onClickListener: () -> Unit) {
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .clickable { onClickListener() }
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Merge,
            contentDescription = "symptoms",
            tint = lightTextAccent,
            modifier = Modifier
                .size(34.dp)
                .padding(6.dp)
        )
        Text(
            text = title,
            fontSize = 14.sp,
            fontFamily = fontFamily,
            modifier = Modifier.padding(start = 13.dp)
        )
    }
}

@Composable
fun BOMChangeDUser(
    currentUserDataVM: currentUserDataVM,
    onClickListener: (Int) -> Unit
) {
    val users= remember{
        mutableStateListOf<fieldValueDC>()
    }
    val index= currentUserDataVM.defaultUserIndex.collectAsState().value
    val defUserIndnex= remember {
        mutableStateOf(index)
    }
    LaunchedEffect(key1 = true) {
        val defUsers= currentUserDataVM.getAllUsers()
        defUsers.forEach { users.add(it) }
    }
    LazyColumn(
        modifier = Modifier
    ) {
        item {
            Row {
                Text(
                    modifier = Modifier
                        .padding(start = 30.dp),
                    text = "Type Error",
                    fontSize = 19.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .clickable {
                            onClickListener(defUserIndnex.value)
                        }
                        .padding(end = 30.dp),
                    text = "Save",
                    fontSize = 17.sp,
                    color = customBlue,
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        items(users){
            DataListFull(
                title = "${it.fname} ${it.lname}",
                subtitle = if (it.isDefaultUser) "default" else "patient:${it.index}",
                imageVector = if (defUserIndnex.value== it.index)Icons.Outlined.Done else Icons.Outlined.MedicalInformation,
                colorLogo = Color.White,
                additionalDataColor = lightTextAccent,
                colorLogoTint = Color.Black,
                onClickListener = {
                    defUserIndnex.value= it.index
                }
            )
        }
        item { Spacer(modifier = Modifier.height(28.dp)) }
    }
}