package com.rohnsha.medbuddyai.screens.scan

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.rohnsha.medbuddyai.database.appData.disease_questions.questionVM
import com.rohnsha.medbuddyai.database.appData.disease_questions.questions
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import com.rohnsha.medbuddyai.domain.dataclass.disease_version
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import kotlinx.coroutines.delay

private lateinit var otherDiseaseData: List<disease_version>
private lateinit var disease_results: MutableState<disease_data_dataClass>


@Composable
fun ScanQuestions(
    indexClassification: Int,
    photoCaptureViewModel: photoCaptureViewModel,
    questionVM: questionVM
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

    LazyColumn {
        items(qList){
            Text(text = it.question)
        }
    }

}