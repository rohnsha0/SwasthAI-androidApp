package com.rohnsha.dermbuddyai

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.DataArray
import androidx.compose.material.icons.outlined.MotionPhotosAuto
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.PsychologyAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohnsha.dermbuddyai.ui.theme.BGMain
import com.rohnsha.dermbuddyai.ui.theme.fontFamily

data class ToggleableInfo(
    val isChecked: Boolean,
    val text: String
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    padding: PaddingValues
) {
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
fun ScanMainScreen() {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {

    }
}


