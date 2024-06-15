package com.rohnsha.medbuddyai.screens.scan

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.BlurOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.database.appData.disease_questions.questionVM
import com.rohnsha.medbuddyai.database.appData.disease_questions.questions
import com.rohnsha.medbuddyai.database.userdata.chatbot.messages.messageEntity
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import com.rohnsha.medbuddyai.domain.dataclass.disease_version
import com.rohnsha.medbuddyai.domain.dataclass.moreActions
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.sideStateVM
import com.rohnsha.medbuddyai.navigation.sidebar.screens.sideBarModifier
import com.rohnsha.medbuddyai.screens.Messages
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import kotlinx.coroutines.delay

private lateinit var otherDiseaseData: List<disease_version>
private lateinit var disease_results: MutableState<disease_data_dataClass>


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanQuestions(
    indexClassification: Int,
    photoCaptureViewModel: photoCaptureViewModel,
    questionVM: questionVM,
    sideStateVM: sideStateVM,
    padding: PaddingValues,
    navHostController: NavHostController
) {

    val context= LocalContext.current
    disease_results = remember {
        mutableStateOf(disease_data_dataClass())
    }
    val qList= remember {
        mutableListOf<questions>()
    }

    val index= remember {
        mutableIntStateOf(0)
    }
    val yesCount= remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = true){
        delay(500L)
        photoCaptureViewModel.onClassify(context, index = indexClassification)

    }
    disease_results.value= photoCaptureViewModel.classificationData.collectAsState().value
    otherDiseaseData = photoCaptureViewModel.getDiseaseVersionData(group_number = indexClassification, isMaxIndex = false)

    Log.d("ScanQuestions", "scanMainItem: ${disease_results.value}")
    Log.d("ScanQuestions", "scanMainItem: ${otherDiseaseData}")

    if (disease_results.value.domain != ""){
        LaunchedEffect(key1 = true) {
            questionVM.getQuestions(
                domain = disease_results.value.domain.toLong(),
                index = disease_results.value.diseaseIndex.toLong()
            )
        }
    }
    val q= questionVM.questionList.collectAsState().value.take(5)
    Log.d("ScanQuestions", "q: ${q}")
    val questions= remember {
        mutableListOf<questions>()
    }
    if (q.isNotEmpty() && !questions.contains(q[index.intValue])){
        questions.add(q[index.intValue])
    }
    /*LaunchedEffect(key1 = index.intValue, key2 = true) {
        if (q.isNotEmpty()){
            questions.add(q[index.intValue])
        }
    }*/

    Log.d("ScanQuestions", "scanMainQuwstion: ${qList}")

    val listGreet= listOf(
        moreActions("Yes, I have symptoms") {
            questions.add(questions(
                domain = Int.MAX_VALUE.toLong(),
                index = Int.MAX_VALUE.toLong(),
                question = "Yes"
            ))
            yesCount.intValue++
        },
        moreActions("No") {
            questions.add(questions(
                domain = Int.MAX_VALUE.toLong(),
                index = Int.MAX_VALUE.toLong(),
                question = "No"
            ))
        },
    )

    Log.d("ScanQuestions", "scanMainQuwstionYESCount: ${yesCount.intValue}, index: ${index.intValue}")

    val scrollState= rememberLazyListState()

    LaunchedEffect(key1 = questions.size) {
        if (questions.isNotEmpty()){
            scrollState.animateScrollToItem(questions.size-1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Self Declaration",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BGMain
                ),
                actions = {
                    Image(
                        imageVector = Icons.Outlined.BlurOn,
                        contentDescription = "Show accuracy button",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp)
                            .clickable {
                                sideStateVM.toggleState()
                            }
                    )
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
            .fillMaxSize()
            .then(sideBarModifier(sideStateVM = sideStateVM)),
        containerColor = BGMain
    ){value->
        Column(
            modifier = Modifier
                .padding(value)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                .padding(top = 26.dp, start = 24.dp, end = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                state = scrollState
            ) {
                items(questions){
                    Messages(messageInfo = messageEntity(
                        message = it.question,
                        isError =  false,
                        isBotMessage = true,
                        chatId = Int.MAX_VALUE,
                        timestamp = System.currentTimeMillis()
                    ))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (q.size > (questions.size/2) && q.isNotEmpty()){
                LazyRow(
                    Modifier.offset(x= (-4).dp)
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
                                .clickable {
                                    Log.d("ScanQuestions", "scanMainQuwstion: clicked")
                                    it.onClick()
                                    Log.d(
                                        "ScanQuestions",
                                        "scanMainQuwstionBoolean: ${q.size != (index.intValue - 1)}, index: ${index.intValue}, q.size: ${q.size}"
                                    )
                                    if (q.size != (index.intValue + 1)) {
                                        Log.d("ScanQuestions", "scanMainQuwstion: emterted")
                                        index.intValue++
                                    } else {
                                        questions.add(
                                            questions(
                                                domain = Int.MAX_VALUE.toLong(),
                                                index = Int.MAX_VALUE.toLong(),
                                                question = "You are done with the self declaration. Navigating to main menu"
                                            )
                                        )
                                        Log.d("ScanQuestions", "scanMainQuwstion: navigation initiated")
                                    }
                                }
                                .padding(vertical = 3.dp, horizontal = 14.dp)
                        )
                    }
                }
            }
        }
    }


}