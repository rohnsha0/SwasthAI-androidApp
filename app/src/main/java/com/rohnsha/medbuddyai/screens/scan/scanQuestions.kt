package com.rohnsha.medbuddyai.screens.scan

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohnsha.medbuddyai.database.appData.disease_questions.questionVM
import com.rohnsha.medbuddyai.database.appData.disease_questions.questions
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import com.rohnsha.medbuddyai.domain.dataclass.disease_version
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import com.rohnsha.medbuddyai.domain.viewmodels.sideStateVM
import com.rohnsha.medbuddyai.navigation.sidebar.screens.sideBarModifier
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import kotlinx.coroutines.delay
import org.tensorflow.lite.schema.Padding

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
    padding: PaddingValues
) {

    val context= LocalContext.current
    disease_results = remember {
        mutableStateOf(disease_data_dataClass())
    }
    val qList= remember {
        mutableListOf<questions>()
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
        for (question in questionVM.questionList.collectAsState().value) {
            qList.add(question)
        }
    }

    Log.d("ScanQuestions", "scanMainQuwstion: ${qList}")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Select AI Model",
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
        LazyColumn(
            modifier = Modifier
                .padding(value)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                .padding(top = 26.dp, start = 24.dp, end = 24.dp)
        ) {
            items(qList){
                Text(text = it.question, modifier = Modifier.padding(vertical = 9.dp))
            }
        }
    }


}