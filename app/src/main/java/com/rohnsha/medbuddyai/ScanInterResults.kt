package com.rohnsha.medbuddyai

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.rohnsha.medbuddyai.domain.classification
import com.rohnsha.medbuddyai.domain.classifier
import com.rohnsha.medbuddyai.domain.photoCaptureViewModel
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import kotlinx.coroutines.delay

private lateinit var isClassifying: MutableState<Boolean>
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanInterResultScreen(
    padding: PaddingValues,
    photoVM: photoCaptureViewModel
) {
    isClassifying= remember {
        mutableStateOf(true)
    }
    Scaffold(
        topBar = {
            if (!isClassifying.value){
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "See Related Diseases",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight(600),
                            fontSize = 26.sp
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = BGMain
                    )
                )
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ) { values ->
        if (isClassifying.value){
            LoadingLayout(titleList = listOf(
                "Charting a Course to Health Discovery", "Preparing to Enlighten and Empower",
                "Loading the Canvas of Your Health Journey", "Awaiting Your Arrival in the Health Universe",
                "Elevating Your Health IQ One Byte at a Time", "Stepping into the Digital Library of Wellness",
                "Welcome to the Wonderland of Health Wisdom", "Fueling Curiosity for a Healthier Tomorrow"

            ))
            PerformClassification(photoVM = photoVM)
        }else{
            Column(
                modifier = Modifier
                    .padding(values)
                    .padding(padding)
                    .padding(top = 20.dp)
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            ) {
            }
        }
    }

}

@Composable
fun PerformClassification(
    photoVM: photoCaptureViewModel
) : List<classification>{
    val bitmap= photoVM.bitmaps.collectAsState().value
    val context= LocalContext.current
    var classificationResults: List<classification> = emptyList()
    LaunchedEffect(key1 = isClassifying.value){
        delay(500)
        classificationResults=
            bitmap?.let {
                classifier.classifyIndex(context, it, 0)
            }!!
        Log.d("successIndexLI", classificationResults.toString())
        isClassifying.value=false
    }
    Log.d("successIndexTAG", classificationResults.toString())
    return classificationResults
}

