package com.rohnsha.dermbuddyai

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.DataArray
import androidx.compose.material.icons.outlined.MotionPhotosAuto
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.PsychologyAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.rohnsha.dermbuddyai.ml.ModelPotato
import com.rohnsha.dermbuddyai.ui.theme.BGMain
import com.rohnsha.dermbuddyai.ui.theme.fontFamily
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

private lateinit var ResultPred: String

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    padding: PaddingValues
) {
    var cameraPermissionState: PermissionState= rememberPermissionState(permission = Manifest.permission.CAMERA)

    ResultPred= "not predicted"

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
                )
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
            ScanOptions()
            ScanMainScreen()
        }
    }
}

@Composable
fun ScanOptions() {
    var autoBool= remember {
        mutableStateOf(true)
    }
    var xRayBool= remember {
        mutableStateOf(false)
    }
    var mriBool= remember {
        mutableStateOf(false)
    }
    var skinBool = remember {
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
    Row(
        modifier = Modifier
            .clickable(onClick = clickAction)
            .height(34.dp)
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

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier= Modifier
) {
    val lifecycleOwner= LocalLifecycleOwner.current
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

private fun takePhoto(
    controller: LifecycleCameraController,
    context: Context
){
    controller.takePicture(
        ContextCompat.getMainExecutor(ContextUtill.ContextUtils.getApplicationContext()),
        object : OnImageCapturedCallback(){
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val model = ModelPotato.newInstance(context)

                var imageProcessor= ImageProcessor.Builder()
                    .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
                    .build()

                var tensorImage= TensorImage(DataType.FLOAT32)
                tensorImage.load(image.toBitmap())

                tensorImage= imageProcessor.process(tensorImage)

                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
                inputFeature0.loadBuffer(tensorImage.buffer)

                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer
                Log.d("successIndexModelTF", getMaxIndex(outputFeature0.floatArray).toString())
                Log.d("successIndexModelTF", outputFeature0.floatArray[getMaxIndex(outputFeature0.floatArray)].toString())

                val potatoDisease= listOf<String>(
                    "Early Blight",
                    "Healthy",
                    "Late Blight"
                )

                Log.d("successIndexModelTF", "Found: ${potatoDisease[getMaxIndex(outputFeature0.floatArray)]} with ${
                    String.format(
                        "%.2f",
                        outputFeature0.floatArray[getMaxIndex(outputFeature0.floatArray)] * 100
                    )
                }% confidence!\"")

                ResultPred= "Found: ${potatoDisease[getMaxIndex(outputFeature0.floatArray)]} with ${
                    String.format(
                        "%.2f",
                        outputFeature0.floatArray[getMaxIndex(outputFeature0.floatArray)] * 100
                    )
                }% confidence!\""

            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }
        }
    )
}

private fun getMaxIndex(floatArray: FloatArray): Int {
    var max = 0
    for (i in floatArray.indices) {
        if (floatArray[i] > floatArray[max]) {
            max = i
        }
    }
    return max
}

@Composable
fun ScanMainScreen() {
    val controller= remember {
        LifecycleCameraController(ContextUtill.ContextUtils.getApplicationContext()).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    val conttext= LocalContext.current
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Box(modifier =Modifier){
            CameraPreview(controller = controller, modifier = Modifier.fillMaxSize(.8f))
        }
        IconButton(
            onClick = {
                takePhoto(controller = controller, context = conttext)
            }
        ) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "Take photo"
            )
        }
        Text(
            text = ResultPred
        )
    }
}


