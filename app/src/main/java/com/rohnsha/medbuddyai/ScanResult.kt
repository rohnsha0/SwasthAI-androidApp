package com.rohnsha.medbuddyai

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Dangerous
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Share
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.rohnsha.medbuddyai.api.APIViewModel
import com.rohnsha.medbuddyai.api.disease_data_dataClass
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent
import kotlin.random.Random

private lateinit var disease_results: disease_data_dataClass
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    padding: PaddingValues,
    group: String,
    serial_number: String,
) {
    val dynamicURL= "https://quuicqg435fkhjzpkawkhg4exi0vjslb.lambda-url.ap-south-1.on.aws/disease_data/$group/$serial_number"
    var isAPIcalling= remember {
        mutableStateOf(true)
    }
    val apiViewModel= hiltViewModel<APIViewModel>()
    LaunchedEffect(key1 = isAPIcalling){
        try {
            disease_results= apiViewModel.getDiseaseResults(dynamicURL)
            isAPIcalling.value= false
            Log.d("notablesInner", disease_results.toString())
        } catch (e: Exception){
            Log.e("error", e.printStackTrace().toString())
        }
    }
    Scaffold(
        topBar = {
            if (!isAPIcalling.value){
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = disease_results.disease_name,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight(600),
                                fontSize = 26.sp,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = disease_results.domain,
                                fontFamily = fontFamily,
                                color = Color.Black.copy(.75f),
                                fontSize = 13.sp,
                            )
                        }
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
        if (isAPIcalling.value){
            LoadingLayout(
                titleList = listOf(
                    "Charting a Course to Health Discovery", "Preparing to Enlighten and Empower",
                    "Loading the Canvas of Your Health Journey", "Awaiting Your Arrival in the Health Universe",
                    "Elevating Your Health IQ One Byte at a Time", "Stepping into the Digital Library of Wellness",
                    "Welcome to the Wonderland of Health Wisdom", "Fueling Curiosity for a Healthier Tomorrow"

                ))
        }else{
            ScanResultsSuccess(padding = padding, values = values)
        }
    }
}

@Composable
fun LoadingLayout(
    titleList: List<String>
) {
    val random = Random(System.currentTimeMillis())
    val title= titleList[random.nextInt(0, (titleList.size-1))]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BGMain)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.95f)
        ){

        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            Alignment.BottomCenter
        ){
            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = title,
                fontWeight = FontWeight(600),
                fontFamily = fontFamily,
                color = formAccent,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ScanResultsSuccess(
    padding: PaddingValues,
    values: PaddingValues,
) {
    val scrollState= rememberScrollState(0)
    Column(
        modifier = Modifier
            .padding(values)
            .padding(padding)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment= Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
            ){
                val painter= rememberAsyncImagePainter(
                    model = disease_results.cover_link,
                    onError = {
                        Log.e("coil", "error response: ${it.painter}, ${it.result}")
                    }
                )
                Image(
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    contentDescription = "null",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            OptionScanResults()
            Spacer(
                modifier = Modifier.height(30.dp)
            )
            DataBox(
                title = "Know About Disease",
                data = disease_results.introduction
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            DataBox(
                title = "Symptoms",
                data = disease_results.symptoms
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            DataBox(
                title = "When to see doctor",
                data = disease_results.thresholds
                )
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun OptionScanResults() {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OptionsScanResultUNI(
            title = "Chatbot",
            icon = Icons.Outlined.Chat,
            onClickListener = {}
        )
        OptionsScanResultUNI(
            title = "Post",
            icon = Icons.Outlined.Forum,
            onClickListener = {}
        )
        OptionsScanResultUNI(
            title = "Incorrect",
            icon = Icons.Outlined.Dangerous,
            onClickListener = {}
        )
        OptionsScanResultUNI(
            title = "Share",
            icon = Icons.Outlined.Share,
            onClickListener = {}
        )
    }
}

@Composable
fun OptionsScanResultUNI(
    title: String,
    icon:ImageVector,
    onClickListener: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(49.dp)
                .height(49.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                .background(ViewDash, shape = RoundedCornerShape(8.dp))
                .clickable(
                    onClick = onClickListener
                ),
            contentAlignment = Alignment.Center
        ){
            Image(
                imageVector =icon,
                contentDescription = null,
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight(400)
        )
    }
}

@Composable
fun DataBox(
    title: String,
    data: String
) {
    Column {
        Text(
            text = title,
            fontFamily = fontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier
                .padding(start = 25.dp)
        )
        Spacer(
            modifier = Modifier.height(14.dp)
        )
        Text(
            text = data,
            color = Color.Black,
            fontFamily = fontFamily,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 25.dp),
            textAlign = TextAlign.Start
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewScanResults() {
    LoadingLayout(title = "Hold on! We are loading stuffs")
}*/