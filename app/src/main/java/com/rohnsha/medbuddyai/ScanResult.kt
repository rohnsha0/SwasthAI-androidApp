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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.JoinLeft
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import com.rohnsha.medbuddyai.domain.dataclass.disease_version
import com.rohnsha.medbuddyai.domain.photoCaptureViewModel
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.dashBG
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent
import kotlinx.coroutines.delay
import kotlin.random.Random

private lateinit var disease_results: disease_data_dataClass
private lateinit var photoCaptureViewModel: photoCaptureViewModel
private lateinit var otherDiseaseData: List<disease_version>
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    padding: PaddingValues,
    viewModel: photoCaptureViewModel
) {
    photoCaptureViewModel= viewModel
    var isStillLoading= photoCaptureViewModel.isLoadingBoolean.collectAsState().value
    val isNormal= photoCaptureViewModel.isNormalBoolean.collectAsState().value
    val isErrored= photoCaptureViewModel.isErroredBoolean.collectAsState().value
    val context= LocalContext.current
    LaunchedEffect(key1 = isStillLoading){
        delay(500L)
        photoCaptureViewModel.onClassify(context, 0)
    }
    disease_results= photoCaptureViewModel.classificationData.collectAsState().value
    otherDiseaseData= photoCaptureViewModel.getDiseaseVersionData(0)
    Log.d("confidenceMax",
        otherDiseaseData.toString()
    )
    Scaffold(
        topBar = {
            if (!isStillLoading && !isErrored && !isNormal){
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
                    ),
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Tune Results"
                            )
                        }
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ) { values ->
        if (!isStillLoading){
            if (isNormal){
                Log.d("loggingStatus", "normal")
            } else if (isErrored){
                Log.d("loggingStatus", "errored")
            } else {
                ScanResultsSuccess(padding = padding, values = values)
            }
        } else {
            LoadingLayout(
                titleList = listOf(
                    "Charting a Course to Health Discovery", "Preparing to Enlighten and Empower",
                    "Loading the Canvas of Your Health Journey", "Awaiting Your Arrival in the Health Universe",
                    "Elevating Your Health IQ One Byte at a Time", "Stepping into the Digital Library of Wellness",
                    "Welcome to the Wonderland of Health Wisdom", "Fueling Curiosity for a Healthier Tomorrow"

            ))
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
    LazyColumn(
        modifier = Modifier
            .padding(values)
            .padding(padding)
    ){
        item {
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
        }
        item {
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
            }
        }
        if (otherDiseaseData.isNotEmpty()){
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ){
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Other Possible Outcomes",
                        fontFamily = fontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier
                            .padding(start = 25.dp)
                    )
                    Spacer(modifier = Modifier.height(13.dp))
                }
            }
            items(otherDiseaseData){ data ->
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(vertical = 7.dp)
                ) {
                    DataListFull(
                        title =data.disease_name,
                        subtitle =data.version,
                        data ="${String.format(" % .2f", data.confidence)}%",
                        imageVector = Icons.Outlined.JoinLeft,
                        colorLogo = dashBG
                    )
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (otherDiseaseData.isNotEmpty()){
                    Spacer(modifier = Modifier.height(13.dp))
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                Text(
                    text = "View All Results",
                    fontFamily = fontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    modifier = Modifier
                        .padding(start = 25.dp, bottom = 18.dp)
                )
            }
        }
    }
}

@Composable
fun DataListFull(
    title: String,
    subtitle: String,
    data: String,
    additionData: String? =null,
    imageVector: ImageVector,
    colorLogo: Color
) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
            .clickable {

            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 13.dp)
                    .size(34.dp)
                    .background(colorLogo, CircleShape)
                    .padding(6.dp),
                imageVector = imageVector,
                contentDescription = "$imageVector icon",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Column(
                modifier = Modifier
                    .padding(start = 13.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = title,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600)
                )
                Text(
                    modifier = Modifier
                        .offset(y = (-2).dp),
                    text = subtitle,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                    color = lightTextAccent
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 18.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier,
                    text = data,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600)
                )
                additionData?.let {
                    Text(
                        modifier = Modifier
                            .offset(y = (-2).dp),
                        text = it,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        color = colorLogo,
                        fontWeight = FontWeight(600)
                    )
                }
            }
        }
    }
}

/*
@Composable
fun BOMContent(

) {
    RadioButton(
        selected = {  },
        onClick = { /*TODO*/ },
        colors = RadioButtonDefaults.colors(
            selectedColor = ,
            unselectedColor =
        )
    )
}*/

@Composable
fun MySnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    indicator_color: Color,
    padding: PaddingValues,
    action: () -> Unit
) {
    SnackbarHost(
        hostState = snackbarHostState
    ) {
        Row(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 25.dp, vertical = 16.dp)
                .height(56.dp)
                .background(color = dashBG, shape = RoundedCornerShape(size = 28.dp))
                .shadow(elevation = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp)
                    .background(indicator_color, CircleShape)
                    .padding(4.dp),
                imageVector = Icons.Outlined.Close,
                contentDescription = "snackbar image"
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = message,
                color = Color.White,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                fontFamily = fontFamily
            )
            Box(modifier = Modifier.fillMaxWidth()){
                Image(
                    modifier = Modifier
                        .size(12.dp)
                        .clickable {
                            snackbarHostState.currentSnackbarData?.dismiss()
                        },
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "snackbar image",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
        }
    }

    LaunchedEffect(key1 = true) {
        delay(500L)
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = null,
            duration = SnackbarDuration.Short
        )
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
        Box(
            modifier = Modifier
                .height(49.dp)
                .align(Alignment.Top),
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(15.dp)
                    .background(ViewDash, RoundedCornerShape(4.dp))
            )
        }
        OptionsScanResultUNI(
            title = "Rescan",
            icon = Icons.Outlined.RestartAlt,
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
            text = data.replace("new_line", "\n"),
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