package com.rohnsha.medbuddyai

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.BlurOn
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FlashAuto
import androidx.compose.material.icons.outlined.MotionPhotosAuto
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PsychologyAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.domain.analyzer
import com.rohnsha.medbuddyai.domain.dataclass.classification
import com.rohnsha.medbuddyai.domain.viewmodels.classificationVM
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private lateinit var viewModelPhotoSave: photoCaptureViewModel
private lateinit var viewModelClassification: classificationVM
private lateinit var isConfirming: MutableState<Boolean>
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    padding: PaddingValues,
    navController: NavHostController,
    photoCaptureVM: photoCaptureViewModel,
    classifierVM: classificationVM,
    index: Int
) {
    viewModelPhotoSave= photoCaptureVM
    viewModelClassification= classifierVM
    var cameraPermissionState: PermissionState= rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted){
        Log.d("permission", "permissionGranted")
    } else {
        Log.d("permission", "permissionNOtGranted")
        ActivityCompat.requestPermissions(
            LocalContext.current as Activity,
            arrayOf(Manifest.permission.CAMERA),
            0
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Scan",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
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
            .fillMaxSize(),
        containerColor = BGMain
    ) { value ->
        Column(
            modifier = Modifier
                .padding(value)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
        ) {
            ScanMainScreen(
                navController,
                index
            )
        }
    }
}

@Composable
fun ScanOptions() {
    val autoBool= remember {
        mutableStateOf(true)
    }
    val xRayBool= remember {
        mutableStateOf(false)
    }
    val mriBool= remember {
        mutableStateOf(false)
    }
    val skinBool = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        ScanOptionsItem(unselectedIcon = Icons.Outlined.MotionPhotosAuto, selectedIcon = Icons.Filled.MotionPhotosAuto, description_icon = "auto", clickAction = {
            autoBool.value= true
            xRayBool.value=false
            mriBool.value= false
            skinBool.value= false
            Log.d(
                "clicked",
                "xray $xRayBool, autobool $autoBool"
            )
        }, enabledState = autoBool, text = "Auto")
        Spacer(modifier = Modifier.width(13.dp))
        ScanOptionsItem(unselectedIcon = Icons.Outlined.CenterFocusWeak, description_icon = "data_array", clickAction = {
            autoBool.value= false
            xRayBool.value=true
            mriBool.value= false
            skinBool.value= false
            Log.d(
                "clicked",
                "xray $xRayBool, autobool $autoBool"
            )
        }, enabledState = xRayBool, text = "X-Ray", selectedIcon = Icons.Filled.CenterFocusStrong)
        Spacer(modifier = Modifier.width(13.dp))
        ScanOptionsItem(unselectedIcon = Icons.Outlined.PsychologyAlt, description_icon = "data_array", clickAction = {
            autoBool.value= false
            xRayBool.value=false
            mriBool.value=true
            skinBool.value=false
            Log.d(
                "clicked",
                "xray $xRayBool, autobool $autoBool"
            )
        }, enabledState = mriBool, text = "MRI", selectedIcon = Icons.Filled.PsychologyAlt)
        Spacer(modifier = Modifier.width(13.dp))
        ScanOptionsItem(unselectedIcon = Icons.Outlined.Person, description_icon = "data_array", clickAction = {
            autoBool.value= false
            xRayBool.value=false
            mriBool.value=false
            skinBool.value=true
            Log.d(
                "clicked",
                "xray $xRayBool, autobool $autoBool"
            )
        }, enabledState = skinBool, text = "Skin Manifestation", selectedIcon = Icons.Filled.Person)
    }
}

@Composable
fun ScanOptionsItem(
    unselectedIcon: ImageVector,
    selectedIcon: ImageVector,
    description_icon: String,
    clickAction: () -> Unit,
    enabledState: MutableState<Boolean>,
    text: String
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = clickAction),
        color = MaterialTheme.colorScheme.primary.copy(0f)
    ) {
        Row(
            modifier = Modifier
                .height(34.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
                .then(
                    if (enabledState.value) Modifier.background(
                        color = Color.White,
                        shape = RoundedCornerShape(6.dp)
                    ) else Modifier
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier
                        .padding(start = 9.dp, end = 3.dp)
                        .height(24.dp)
                        .width(24.dp),
                    imageVector = if (enabledState.value) selectedIcon else unselectedIcon,
                    contentDescription = description_icon
                )
            }
            if (enabledState.value){
                Text(
                    modifier = Modifier
                        .padding(start = 3.dp, end = 9.dp),
                    text = text,
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier= Modifier,
    imgBitmap: Bitmap? = null,
) {
    val lifecycleOwner= LocalLifecycleOwner.current
    if (isConfirming.value){
        if (imgBitmap!=null){
            Image(
                bitmap = imgBitmap.asImageBitmap(),
                contentDescription = "Captured Image",
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
    } else{
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller= controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = modifier
        )
    }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    toCcamFeed: (classification) -> Unit,
){
    Log.d("successIndexModelTF", "entered")

    var classificationResult: classification = classification(indexNumber = 404, confident = 404f)
    controller.takePicture(
        ContextCompat.getMainExecutor(ContextUtill.ContextUtils.getApplicationContext()),
        object : OnImageCapturedCallback(){
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                Log.d("successIndexModelTF", "entered1")

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                onPhotoTaken(rotatedBitmap)
                toCcamFeed(classificationResult)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)

                Log.d("successIndexModelTF", "jii $exception", )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanMainScreen(
    navController: NavHostController,
    index: Int
) {
    val conttext= LocalContext.current
    var itt= classification(0, 6f)
    val detecteddClassification= remember {
        mutableStateOf(itt.indexNumber)
    }
    Log.d("classificationCLassifier", detecteddClassification.value.toString())
   val errorText= remember {
       mutableStateOf("")
   }
    errorText.value= if (detecteddClassification.value==0){
        "Works best with ${when(index){
            0 -> "chest xray"
            else -> "brain mri"
        }}"
    } else {
        "Perfect"
    }
    val scope= rememberCoroutineScope()
    val analyzer= remember {
        analyzer(
            context = conttext,
            onResults = {
                detecteddClassification.value= it.indexNumber
            },
            index = index
        )
    }
    val controller= remember {
        LifecycleCameraController(ContextUtill.ContextUtils.getApplicationContext()).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                CameraController.IMAGE_ANALYSIS
            )
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(ContextUtill.ContextUtils.getApplicationContext()),
                analyzer
            )
        }
    }
    val isPredictingBool= remember {
        mutableStateOf(false)
    }

    isConfirming= remember {
        mutableStateOf(false)
    }

    val bomError= rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize()
            .background(color = BGMain, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp))
                .fillMaxHeight(.85f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                imgBitmap = viewModelPhotoSave.bitmaps.collectAsState().value,
            )
            CameraPreviewSegmentOp(
                title = "C1: ",
                dataItem = errorText.value
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = if (!isConfirming.value) Icons.Outlined.FlashAuto else Icons.Outlined.Delete,
                contentDescription = if (!isConfirming.value) "Show accuracy button" else "Rescan",
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp)
                    .clickable {
                        if (!isConfirming.value){

                        } else {
                            isConfirming.value= false
                        }
                    }
            )
            val bitmapImg= viewModelPhotoSave.bitmaps.collectAsState().value
            Button(
                onClick = {
                    if (!isConfirming.value){
                        isPredictingBool.value= true
                        scope.launch {
                            takePhoto(
                                controller = controller,
                                onPhotoTaken = viewModelPhotoSave::onTakePhoto,
                                toCcamFeed = {
                                    Log.d("checkConfirmation", "clicked")
                                    isConfirming.value= true
                                }
                            )
                        }
                        isPredictingBool.value= false
                    } else {
                        scope.launch {
                            if (bitmapImg != null) {
                                isPredictingBool.value= true
                                delay(600L)
                                val branchClassification = viewModelClassification.classify(
                                    conttext, bitmapImg, scanOption = 999, index = index)[0]
                                Log.d("bitmapResults", branchClassification.toString())
                                if (branchClassification.indexNumber==0){
                                    bomError.value=true
                                } else {
                                    navController.navigate(bottomNavItems.ScanResult.returnScanResIndex(0, index = index))
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = customBlue,
                    contentColor = Color.White
                )
            ){
                Text(
                    text = if (!isConfirming.value) "Capture" else "Proceed",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily
                )
            }
            if (!isConfirming.value){
                val imageURI = remember {
                    mutableStateOf<Uri?>(null)
                }
                val bitmap = remember {
                    mutableStateOf<Bitmap?>(null)
                }
                val getImageFromGallery = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()){
                    imageURI.value=it
                }
                imageURI.value.let {
                    if (Build.VERSION.SDK_INT < 28){
                        bitmap.value = MediaStore.Images
                            .Media.getBitmap(conttext.contentResolver, it)
                    } else {
                        val source=
                            it?.let { it1 ->
                                ImageDecoder.createSource(conttext.contentResolver,
                                    it1
                                )
                            }
                        bitmap.value= source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                    }
                    val convertedBitmap= bitmap.value?.copy(Bitmap.Config.ARGB_8888, false)
                    convertedBitmap?.let { it1 ->
                        viewModelPhotoSave.onTakePhoto(it1)
                        isConfirming.value= true
                        errorText.value= if (viewModelClassification.classify(
                                context = conttext,
                                it1,
                                scanOption = 999, index = index)[0].indexNumber==0){
                            "Works best with ${when(index){
                                0 -> "chest xray"
                                else -> "brain mri"
                            }}"
                        } else {
                            "Perfect"
                        }
                    }
                }
                Image(
                    imageVector = Icons.Outlined.Collections,
                    contentDescription = "Import from Gallery icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp)
                        .clickable {
                            getImageFromGallery.launch("image/*")
                        }
                )
            } else {
                Spacer(modifier = Modifier.width(24.dp))
            }
            if (bomError.value){
                ModalBottomSheet(onDismissRequest = {
                    isConfirming.value= false
                    bomError.value= false
                    isPredictingBool.value= false
                }) {
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
                                    isConfirming.value= false
                                    bomError.value = false
                                    isPredictingBool.value = false
                                }
                                .padding(end = 30.dp),
                            text = "Rescan",
                            fontSize = 17.sp,
                            fontWeight = FontWeight(600),
                            fontFamily = fontFamily
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 30.dp),
                        fontFamily = fontFamily,
                        fontSize = 14.sp,
                        text = "The image you uploaded didn't matched to any of the currently supported image types."
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 30.dp, top = 14.dp, end = 30.dp, bottom = 45.dp),
                        fontFamily = fontFamily,
                        fontSize = 14.sp,
                        text = "Please upload correct image type to proceed or rescan?"
                    )
                }
            }
        }
    }
}

@Composable
fun CameraPreviewSegmentOp(
    title: String,
    dataItem: String
) {
    Row(
        modifier = Modifier
            .padding(bottom = 6.dp, top = 20.dp)
            .height(30.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontFamily = fontFamily
        )
        Text(
            text = dataItem,
            fontFamily = fontFamily,
            fontWeight = FontWeight(600),
            fontSize = 14.sp
        )
    }
}