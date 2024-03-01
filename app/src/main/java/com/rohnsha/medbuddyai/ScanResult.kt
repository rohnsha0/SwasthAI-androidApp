package com.rohnsha.medbuddyai

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.BlurOff
import androidx.compose.material.icons.outlined.BlurOn
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ConnectWithoutContact
import androidx.compose.material.icons.outlined.JoinLeft
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.SwitchAccessShortcut
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.rohnsha.medbuddyai.database.userdata.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistory
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryViewModel
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import com.rohnsha.medbuddyai.domain.dataclass.disease_version
import com.rohnsha.medbuddyai.domain.dataclass.rbStructure
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customRed
import com.rohnsha.medbuddyai.ui.theme.dashBG
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.random.Random

private lateinit var disease_results: MutableState<disease_data_dataClass>
private lateinit var photoCaptureViewModel: photoCaptureViewModel
private lateinit var diseaseDBvm: diseaseDBviewModel
private lateinit var scanHistoryVM: scanHistoryViewModel
private lateinit var otherDiseaseData: List<disease_version>
private lateinit var modalState : MutableState<Boolean>
private var isStillLoading by Delegates.notNull<Boolean>()
private var isNormal by Delegates.notNull<Boolean>()
private var isErrored by Delegates.notNull<Boolean>()
private var mode by Delegates.notNull<Int>()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    padding: PaddingValues,
    viewModel: photoCaptureViewModel,
    navController: NavController,
    scanHistoryViewModel: scanHistoryViewModel,
    resultsLevel: Int=1, // 0-> Scan, 1-> Scan History, 2-> Read Only,
    diseaseDBviewModel: diseaseDBviewModel,
    indexClassification: Int // model to be triggered
) {
    photoCaptureViewModel= viewModel
    diseaseDBvm= diseaseDBviewModel
    mode= resultsLevel
    scanHistoryVM= scanHistoryViewModel
    val context= LocalContext.current
    disease_results= remember {
        mutableStateOf(disease_data_dataClass())
    }
    when(resultsLevel){
        0 -> {
            isStillLoading= photoCaptureViewModel.isLoadingBoolean.collectAsState().value
            isNormal= photoCaptureViewModel.isNormalBoolean.collectAsState().value
            isErrored= photoCaptureViewModel.isErroredBoolean.collectAsState().value
            LaunchedEffect(key1 = true){
                delay(500L)
                photoCaptureViewModel.onClassify(context, index = indexClassification)
            }
            disease_results.value= photoCaptureViewModel.classificationData.collectAsState().value
            otherDiseaseData= photoCaptureViewModel.getDiseaseVersionData(group_number = indexClassification, isMaxIndex = false)
        }
        1 -> {
            isStillLoading= diseaseDBviewModel.isLoadingBoolean.collectAsState().value
            isNormal=false
            isErrored= diseaseDBviewModel.isErroredBoolean.collectAsState().value
            LaunchedEffect(key1 = Unit){
                delay(500L)
                diseaseDBviewModel.searchByName()
            }
            disease_results.value= diseaseDBviewModel.data.collectAsState().value
            otherDiseaseData= listOf(disease_version("", "", 0f, 01.0))
        }
    }

    modalState= rememberSaveable {
        mutableStateOf(false)
    }
    val infoStateOpen = remember {
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
                                text = disease_results.value.disease_name,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight(600),
                                fontSize = 26.sp,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = remember {
                                    disease_results.value.domain.toString()
                                },
                                fontFamily = fontFamily,
                                color = lightTextAccent,
                                fontSize = 13.sp,
                            )
                        }
                    },
                    actions = {
                        Image(
                            imageVector = if (infoStateOpen.value) Icons.Outlined.BlurOff else Icons.Outlined.BlurOn,
                            contentDescription = "Show accuracy button",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(24.dp)
                                .clickable {
                                    infoStateOpen.value = !infoStateOpen.value
                                }
                        )
                    },
                    navigationIcon = {
                        Image(
                            imageVector = Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "back button",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(24.dp)
                                .clip(CircleShape)
                                .clickable {
                                    navController.popBackStack()
                                }
                                .padding(5.dp)
                        )
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
                    title = "Lungs"
                ),
                rbStructure(
                    isChecked = false,
                    title = "Brain"
                ),
                rbStructure(
                    isChecked = false,
                    title = "Skin Manifestions"
                ),
            )
        }
        val scope= rememberCoroutineScope()
        if (!isStillLoading){
            if (isNormal){
                Column {
                    Text(text = "isNormal")
                }
                Log.d("loggingStatus", "normal")
            } else if (isErrored){
                Column {
                    Text(text = "isErrrored")
                }
                Log.d("loggingStatus", "errored")
            } else {
                ScanResultsSuccess(padding = padding, values = values)
                if (modalState.value){
                    ModalBottomSheet(
                        onDismissRequest = {
                            modalState.value=false
                        },
                        containerColor = Color.White,
                    ) {
                        BOMContent(
                            rbList = rbList,
                            rbSnapFunc = {
                                rbList.clear()
                                rbList.addAll(it)
                            },
                            coroutineScope=scope,
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
    val confidence= photoCaptureViewModel.maxIndex.collectAsState().value.confident
    if (mode==0){
        LaunchedEffect(key1 = disease_results.value){
            scanHistoryVM.addScanHistory(scanHistory(
                timestamp = System.currentTimeMillis(),
                title = disease_results.value.disease_name,
                domain = disease_results.value.domain,
                confidence = confidence
            ))
        }
    }
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
                    val color= remember {
                        mutableStateOf(Color.Transparent)
                    }
                    val painter= rememberAsyncImagePainter(
                        model = disease_results.value.cover_link,
                        onError = {
                            color.value= customRed.copy(alpha = .25f)
                            Log.e("coil", "error response: ${it.painter}, ${it.result}")
                        },
                    )
                    Image(
                        painter = painter,
                        contentScale = ContentScale.Crop,
                        contentDescription = "null",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color.value)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (mode==0){
                    ClassificationConf()
                }
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
            if (mode==0){
                items(otherDiseaseData){ data ->
                    Box(
                        modifier = Modifier
                            .background(Color.White)
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
                    text = "View Model Parameters",
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
fun ClassificationConf() {
    val modelData= photoCaptureViewModel.getDiseaseVersionData(
        isMaxIndex = true, group_number = 0,
    )
    Log.d("modelAccuracy", modelData[0].toString())
    Row(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .padding(start = 13.dp)
        ) {
            Text(
                modifier = Modifier,
                text = "${String.format("%.2f", modelData[0].modelAccuracy)}%",
                fontSize = 14.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(600)
            )
            Text(
                modifier = Modifier
                    .offset(y = (-2).dp),
                text = "Accuracy",
                fontSize = 14.sp,
                fontFamily = fontFamily,
                color = lightTextAccent
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = modelData[0].version,
                fontSize = 14.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(600)
            )
            Text(
                modifier = Modifier
                    .offset(y = (-2).dp),
                text = "Version",
                fontSize = 14.sp,
                fontFamily = fontFamily,
                color = lightTextAccent
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .padding(end = 18.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                modifier = Modifier,
                text = "${String.format("%.2f", modelData[0].confidence)}%",
                fontSize = 14.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(600)
            )
            Text(
                modifier = Modifier
                    .offset(y = (-2).dp),
                text = "Matched",
                fontSize = 14.sp,
                fontFamily = fontFamily,
                color = lightTextAccent,
                //fontWeight = FontWeight(600)
            )
        }
    }
}

@Composable
fun DataListFull(
    title: String,
    subtitle: String?=null,
    data: String? = null,
    additionData: String? =null,
    imageVector: ImageVector ?= null,
    iconText: String?= null,
    colorLogo: Color,
    actionIcon: ImageVector= Icons.Outlined.ArrowForward,
    additionalDataColor: Color?= null,
    colorLogoTint: Color? = null,
    onClickListener: (() -> Unit)? =null,
) {
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .height(60.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
            .then(
                if (onClickListener != null) Modifier.clickable { onClickListener() } else Modifier
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageVector != null) {
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
            } else {
                Box(
                    modifier = Modifier
                        .padding(start = 13.dp)
                        .size(34.dp)
                        .background(colorLogo, CircleShape)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ){
                    if (iconText != null) {
                        Text(
                            text = iconText,
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight(600),
                            color = colorLogoTint?: Color.White,
                        )
                    }
                }
            }
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
                if (subtitle!=null){
                    Text(
                        modifier = Modifier
                            .offset(y = (-2).dp),
                        text = subtitle,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        color = lightTextAccent
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 18.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (data != null) {
                    Text(
                        modifier = Modifier,
                        text = data,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600)
                    )
                }
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
                if (additionData==null && data==null){
                    Image(
                        imageVector = actionIcon,
                        contentDescription = "front icon",
                        colorFilter = ColorFilter.tint(additionalDataColor ?: lightTextAccent),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BOMContent(
    rbList: SnapshotStateList<rbStructure>,
    rbSnapFunc: (SnapshotStateList<rbStructure>) -> Unit,
    coroutineScope: CoroutineScope,
    buttonClicked: () -> Unit,
) {

    val list= remember {
        rbList.toList().toMutableStateList()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 30.dp)
    ) {
        Row {
            Text(
                text = "Choose other models",
                fontSize = 18.sp,
                fontWeight = FontWeight(600),
                fontFamily = fontFamily
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Done",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily,
                    color = lightTextAccent,
                    modifier = Modifier.clickable {
                        rbSnapFunc(list)
                        buttonClicked()
                    }
                )
                if (photoCaptureViewModel.isReLoadingBoolean.collectAsState().value){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(16.dp),
                        color = lightTextAccent,
                        strokeWidth = 1.5.dp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        list.forEachIndexed { _, data ->
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .pointerInput(key1 = true) {
                            detectTapGestures {
                                list.replaceAll {
                                    it.copy(
                                        isChecked = (it.title==data.title)
                                    )
                                }
                            }},
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = Icons.Outlined.MedicalInformation,
                        contentDescription = "options pre",
                        modifier = Modifier
                            .size(40.dp)
                            .background(ViewDash, RoundedCornerShape(16.dp))
                            .padding(10.dp)
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        text = data.title,
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .then(
                                if (!data.isChecked) Modifier.border(
                                    width = 1.dp,
                                    color = Color(0xFFD4D4D4),
                                    shape = CircleShape
                                ) else Modifier
                            )
                    ){
                        if (data.isChecked){
                            Image(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = "options",
                                modifier = Modifier
                                    .background(Color.Black, CircleShape)
                                    .padding(3.dp),
                                colorFilter = ColorFilter.tint(color = Color.White)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
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
            title = "Ask mAI",
            icon = Icons.Outlined.QuestionAnswer,
            onClickListener = {  }
        )
        OptionsScanResultUNI(
            title = "Compose",
            icon = Icons.Outlined.ConnectWithoutContact,
            onClickListener = {}
        )
        OptionsScanResultUNI(
            title = "Doctors",
            icon = Icons.Outlined.VolunteerActivism,
            onClickListener = {}
        )
        OptionsScanResultUNI(
            title = when(mode){
                0 -> { "Domains" }
                1 -> { "Recheck" }
                else -> { "" }
            },
            icon = when(mode){
                0 -> { Icons.Outlined.SwitchAccessShortcut }
                else -> { Icons.Outlined.Camera }
            },
            onClickListener = { modalState.value= true }
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
            fontSize = 14.sp,
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