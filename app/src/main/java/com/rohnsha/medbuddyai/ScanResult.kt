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
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.JoinLeft
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.SwitchAccessShortcut
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.rohnsha.medbuddyai.domain.dataclass.rbStructure
import com.rohnsha.medbuddyai.domain.photoCaptureViewModel
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.dashBG
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

private lateinit var disease_results: MutableState<disease_data_dataClass>
private lateinit var photoCaptureViewModel: photoCaptureViewModel
private lateinit var otherDiseaseData: List<disease_version>
private lateinit var modalState : MutableState<Boolean>
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
    LaunchedEffect(key1 = true){
        delay(500L)
        photoCaptureViewModel.onClassify(context, 0)
    }
    disease_results= remember {
        mutableStateOf(disease_data_dataClass())
    }
    disease_results.value= photoCaptureViewModel.classificationData.collectAsState().value
    Log.d("dataSwitch", photoCaptureViewModel.classificationData.collectAsState().value.toString())
    Log.d("dataSwitchVariable", photoCaptureViewModel.classificationData.collectAsState().value.toString())
    otherDiseaseData= photoCaptureViewModel.getDiseaseVersionData(0)
    Log.d("confidenceMax",
        otherDiseaseData.toString()
    )
    modalState= rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            if (!isStillLoading && !isErrored && !isNormal){
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = viewModel.classificationData.collectAsState().value.disease_name,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight(600),
                                fontSize = 26.sp,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = remember {
                                    disease_results.value.domain
                                },
                                fontFamily = fontFamily,
                                color = Color.Black.copy(.75f),
                                fontSize = 13.sp,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = BGMain
                    ),
                )
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ) { values ->
        val rbList= remember {
            mutableStateListOf(
                rbStructure(
                    isChecked = true,
                    title = "Item 1"
                ),
                rbStructure(
                    isChecked = false,
                    title = "Item 2"
                ),
                rbStructure(
                    isChecked = false,
                    title = "Item 3"
                ),
            )
        }
        val scope= rememberCoroutineScope()
        if (!isStillLoading){
            if (isNormal){
                Log.d("loggingStatus", "normal")
            } else if (isErrored){
                Log.d("loggingStatus", "errored")
            } else {
                ScanResultsSuccess(padding = padding, values = values)
                if (modalState.value){
                    ModalBottomSheet(
                        onDismissRequest = {
                            modalState.value=false
                        },
                        containerColor = Color.White
                    ) {
                        BOMContent(
                            rbList = rbList,
                            rbSnapFunc = { rbStructures ->
                                rbList.replaceAll {
                                    it.copy(
                                        isChecked = (it.title==rbStructures.title)
                                    )
                                }
                            },
                            buttonClicked = {
                                if (rbList[0].isChecked){
                                    scope.launch {
                                        photoCaptureViewModel.resetReloadBoolean()
                                        delay(500L)
                                        photoCaptureViewModel.onClassify(context, 0)
                                        modalState.value= false
                                    }
                                    Log.d("checkedState", "lungs checked")
                                } else if (rbList[1].isChecked){
                                    scope.launch {
                                        photoCaptureViewModel.resetReloadBoolean()
                                        delay(500L)
                                        photoCaptureViewModel.onClassify(context, 1)
                                        modalState.value= false
                                    }
                                    Log.d("checkedState", "brain checked")
                                } else if (rbList[2].isChecked){
                                    Log.d("checkedState", "skin checked")
                                    modalState.value= false
                                }
                            }
                        )
                    }
                }
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
                        model = disease_results.value.cover_link,
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
                    data = disease_results.value.introduction
                )
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
                DataBox(
                    title = "Symptoms",
                    data = disease_results.value.symptoms
                )
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
                DataBox(
                    title = "When to see doctor",
                    data = disease_results.value.thresholds
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
                    ) {}
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
    colorLogo: Color,
    additionalDataColor: Color?= null,
    colorLogoTint: Color? = null,
    onClickListener: (() -> Unit)? =null
) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
            .then(
                if (onClickListener!=null) Modifier.clickable { onClickListener() } else Modifier
            ),
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
                colorFilter = ColorFilter.tint(colorLogoTint ?: Color.White)
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
                        color = additionalDataColor ?: colorLogo,
                        fontWeight = FontWeight(600)
                    )
                }
            }
        }
    }
}


@Composable
fun BOMContent(
    rbList: SnapshotStateList<rbStructure>,
    rbSnapFunc: (rbStructure) -> Unit,
    buttonClicked: () -> Unit,
) {
    Column {
        rbList.forEachIndexed { index, data ->
            Row(
                modifier = Modifier
                    .clickable { rbSnapFunc(data) }
            ) {
                Text(
                    text = data.title
                )
                RadioButton(
                    selected = data.isChecked,
                    onClick = { rbSnapFunc(data) })

            }
        }
        if (photoCaptureViewModel.isReLoadingBoolean.collectAsState().value){
            CircularProgressIndicator()
        } else {
            Button(onClick = { buttonClicked() }) {
                Text(text = "classify")
            }
        }
    }
}

@Composable
fun MySnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    indicator_color: Color,
    padding: PaddingValues,
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
            title = "Options",
            icon = Icons.Outlined.SwitchAccessShortcut,
            onClickListener = { modalState.value= true }
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