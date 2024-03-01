package com.rohnsha.medbuddyai

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoMode
import androidx.compose.material.icons.outlined.BlurOn
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.domain.dataclass.modelMarketPlace
import com.rohnsha.medbuddyai.domain.viewmodels.photoCaptureViewModel
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanCategoryScreen(
    padding: PaddingValues,
    navController: NavHostController,
    photoCaptureViewModel: photoCaptureViewModel
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Select AI Model",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
    ){ value ->
        LaunchedEffect(key1 = true, block = {
            photoCaptureViewModel.flushValues()
        })

        val respiratoryDiseases= listOf(
            modelMarketPlace("Lungs", "V2023.06.51",
                "Includes pneumonia, tuberculosis", false
            ),
        )
        val brainCateg= listOf(
            modelMarketPlace("Brain", "V2024.01.51",
                "Includes Gioma, Meningioma, Pituitary Tumor", false
            ),
            modelMarketPlace("Brain (Cancerous)", "V2024.01.51",
                "Includes pneumonia, tuberculosis", true
            )
        )

        val miscellaneousCateg= listOf(
            modelMarketPlace("Oral (Cancerous)", "V2024.01.51",
                "Inludes oral cancer", true)
        )

        val kidneyCateg= listOf(
            modelMarketPlace("Kidney", "V2024.01.24",
                "Includes tests for kidney tumor", false)
        )

        LazyColumn(
            modifier = Modifier
                .padding(value)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
        ){
            item{
                Text(
                    text = "Respiratory Diseases",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 30.dp, start = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(respiratoryDiseases){
                ModelItem(
                    title = it.modelName,
                    subtitle = it.modelVersion,
                    colorLogo = Color.White,
                    onClickListener = { navController.navigate(route = bottomNavItems.Scan.returnScanIndex(0)) },
                    description = it.description,
                    isCancerous = it.isCancerous
                )
            }
            item {
                Text(
                    text = "Neural Diseases",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 26.dp, start = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(brainCateg){
                ModelItem(
                    title = it.modelName,
                    subtitle = it.modelVersion,
                    colorLogo = Color.White,
                    onClickListener = { navController.navigate(bottomNavItems.Scan.returnScanIndex(1)) },
                    description = it.description,
                    isCancerous = it.isCancerous
                )
            }
            item {
                Text(
                    text = "Kidney",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 26.dp, start = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(kidneyCateg){
                ModelItem(
                    title = it.modelName,
                    subtitle = it.modelVersion,
                    colorLogo = Color.White,
                    onClickListener = { navController.navigate(bottomNavItems.Scan.returnScanIndex(1)) },
                    description = it.description,
                    isCancerous = it.isCancerous
                )
            }
            item {
                Text(
                    text = "Miscellaneous",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(top = 26.dp, start = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(miscellaneousCateg){
                ModelItem(
                    title = it.modelName,
                    subtitle = it.modelVersion,
                    colorLogo = Color.White,
                    onClickListener = { navController.navigate(bottomNavItems.Scan.returnScanIndex(1)) },
                    description = it.description,
                    isCancerous = it.isCancerous
                )
            }
        }
    }
}

@Composable
fun ModelItem(
    title: String,
    subtitle: String?=null,
    data: String? = null,
    additionData: String? =null,
    isCancerous:Boolean,
    imageVector: ImageVector?= if(isCancerous) Icons.Outlined.AutoMode else Icons.Outlined.Hub,
    iconText: String?= null,
    colorLogo: Color = Color.White,
    actionIcon: ImageVector = Icons.Outlined.ArrowForward,
    additionalDataColor: Color?= null,
    colorLogoTint: Color? = null,
    onClickListener: (() -> Unit)? = null,
    description:String,
) {
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .height(90.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
            .then(
                if (onClickListener != null) Modifier.clickable { onClickListener() } else Modifier
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
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
                        colorFilter = ColorFilter.tint(colorLogoTint ?: Color.Black)
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
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, start = 13.dp),
                text = description,
                fontSize = 14.sp,
                fontFamily = fontFamily,
                color = lightTextAccent
            )
        }
    }
}