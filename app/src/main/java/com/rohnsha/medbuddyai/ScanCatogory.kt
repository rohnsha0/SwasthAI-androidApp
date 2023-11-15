package com.rohnsha.medbuddyai

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.SensorOccupied
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.domain.photoCaptureViewModel
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanCatogoryScreen(
    padding: PaddingValues,
    navController: NavHostController,
    photoCaptureVM: photoCaptureViewModel
) {
    val bitmaps= photoCaptureVM.bitmaps.collectAsState()
    LaunchedEffect(key1 = bitmaps){
        Log.e("bitmapsEmpty", ((bitmaps.value==null).toString()))
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Analysis Region",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BGMain
                )
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ){ values ->
        val optionsData= listOf<String>(
            "Lungs",
            "Heart",
            "Brain",
            "Skin",
            "Limbs"
        )
        val scrollState= rememberScrollState()
        Column(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            /*Text(
                text = "Your document photo helps us prove your identity. It should match the information you have provided in the previous steps.",
                modifier = Modifier
                    .padding(top = 30.dp, start = 24.dp, end = 24.dp),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily
            )*/
            LazyColumn(
                modifier = Modifier
                    .padding(top = 40.dp, start = 24.dp, end = 24.dp)
            ){
                item {
                    bitmaps.value?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(bottom = 30.dp)
                                .fillMaxWidth()
                                .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                                .background(color = ViewDash, shape = RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                items(optionsData){items ->
                    OptionsItem(data = items, navController = navController)
                }
            }
        }
    }
}

@Composable
fun OptionsItem(
    data: String,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(54.dp)
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate(bottomNavItems.Scan.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            imageVector = Icons.Outlined.SensorOccupied,
            contentDescription = "$data icon",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .padding(3.dp),
            colorFilter = ColorFilter.tint(Color(0xFFB5BBC9))
        )

        Text(
            text = data,
            modifier = Modifier
                .padding(start = 8.dp),
            fontSize = 19.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight(600)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ){
            Image(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "move to scan",
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp),
                colorFilter = ColorFilter.tint(Color(0xFFB5BBC9))
            )
        }
    }
}