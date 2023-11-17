package com.rohnsha.medbuddyai

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.domain.classification
import com.rohnsha.medbuddyai.domain.photoCaptureViewModel
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent
import kotlinx.coroutines.delay
import kotlin.random.Random

private lateinit var isClassifying: MutableState<Boolean>
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanInterResultScreen(
    padding: PaddingValues,
    photoVM: photoCaptureViewModel,
    group_index: Int,
    navController: NavHostController
) {
    val index_grp= remember {
        mutableIntStateOf(group_index)
    }
    Log.d("TAG", group_index.toString())
    isClassifying= remember {
        mutableStateOf(true)
    }
    val isNormalCase= photoVM.isNormalBoolean.collectAsState().value
    val isErrored= photoVM.isErroredBoolean.collectAsState().value
    val classifiedList= photoVM.classiedList.collectAsState().value
    val context= LocalContext.current
    LaunchedEffect(key1 = isClassifying.value){
        delay(500L)
        photoVM.onClassify(context = context, index = index_grp.value)
        isClassifying.value=false
    }
    Log.d("successIndexValuee", classifiedList.toString())
    Scaffold(
        topBar = {
            if (!isClassifying.value && !isNormalCase && !isErrored){
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
        }else{
            if (isNormalCase){
                Log.d("successIndexNormal", "Normal Case")
                NormalCase(titleList = listOf(
                    "You are fit!",
                    "No need to get any treatment!"
                ))
            } else if (isErrored){
                ErroredLayout(titleList = listOf(
                    "errored",
                    "something went wrong"
                ))
            } else{
                InterResultSuccess(
                    values = values,
                    padding = padding,
                    navController = navController,
                    classificationList = classifiedList,
                    group_index = group_index
                )
            }
        }
    }
}

@Composable
fun InterResultSuccess(
    values: PaddingValues,
    padding: PaddingValues,
    navController: NavHostController,
    classificationList: List<classification>,
    group_index: Int
) {
    LazyColumn(
        modifier = Modifier
            .padding(values)
            .padding(padding)
            .padding(top = 20.dp)
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ){
        items(classificationList){ item ->
            InterResultElement(
                navController = navController,
                group_index = group_index,
                disease_index = item.parentIndex!!
            )
        }
    }
}

@Composable
fun NormalCase(
    titleList: List<String>
) {
    val random = Random(System.currentTimeMillis())
    val title= titleList[random.nextInt(0, (titleList.size-1))]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
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
fun ErroredLayout(
    titleList: List<String>
) {
    val random = Random(System.currentTimeMillis())
    val title= titleList[random.nextInt(0, (titleList.size-1))]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
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
fun InterResultElement(
    navController: NavHostController,
    group_index: Int,
    disease_index: Int
) {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 25.dp)
            .height(54.dp)
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate(
                    bottomNavItems.ScanResult.passGrpAndSerialNumber(
                        group_index,
                        disease_index
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInterRes() {
    InterResultElement(navController = rememberNavController(), 0,1)
}