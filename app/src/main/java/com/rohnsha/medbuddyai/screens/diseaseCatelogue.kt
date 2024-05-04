package com.rohnsha.medbuddyai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.BlurOn
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.database.userdata.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiseasesCatelogue(
    padding: PaddingValues,
    diseaseDBviewModel: diseaseDBviewModel,
    navController: NavHostController,
    domainSelection: Int
) {
    val selectedDomain= remember {
        mutableStateOf(Int.MAX_VALUE)
    }

    selectedDomain.value= domainSelection

    val isSelected= remember {
        mutableStateOf(selectedDomain.value!=Int.MAX_VALUE)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = bottomNavItems.DiseaseCatelogue.title,
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
    ){ value ->
        val lists= remember {
            mutableStateOf(emptyList<disease_data_dataClass>())
        }

        LaunchedEffect(key1 = true) {
            lists.value= if (selectedDomain.value==Int.MAX_VALUE){
                diseaseDBviewModel.readDB()
            } else{
                diseaseDBviewModel.searchByDomain(selectedDomain.value.toString())
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(value)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
        ){

            item {
                Spacer(modifier = Modifier.height(24.dp))
                LazyRow(
                    modifier = Modifier
                        .padding(start = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        if (isSelected.value){
                            Text(
                                text = "Clear all",
                                fontFamily = fontFamily,
                                fontSize = 14.sp,
                                color = customBlue,
                                fontWeight = FontWeight(600),
                                modifier = Modifier.clickable { isSelected.value= false }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        OptionsFilter(
                            text = "Organ System Type",
                            imageVector = Icons.Outlined.ArrowDropDown,
                            onClickListener = {
                                isSelected.value= !isSelected.value
                            },
                            state = isSelected.value
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(lists.value){
                DataListFull(
                    imageVector = Icons.Outlined.Book,
                    title = it.disease_name,
                    colorLogo = Color.White,
                    colorLogoTint = Color.Black,
                    onClickListener = {
                        diseaseDBviewModel.saveData(data = it){
                            navController.navigate(
                                bottomNavItems.ScanResult.returnScanResIndex(
                                    2,
                                    9999
                                )
                            )
                        }
                    },
                    subtitle = when(it.domain){
                        "0" -> "Blood & Lymphatics"
                        "1" -> "Digestive"
                        "2" -> "Hand & Neck"
                        "3" -> "Nervous System"
                        "4" -> "Reproductive System"
                        "6", "7" -> "Respiratory System"
                        "8", "9" -> "Skin"
                        "11" -> "Urinary System"
                        else -> ""
                    }
                )
            }
        }
    }
}

@Composable
fun OptionsFilter(text: String, imageVector: ImageVector, onClickListener: () -> Unit, state: Boolean) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (state) {
                    Modifier.background(customBlue)
                } else {
                    Modifier.background(ViewDash)
                }
            )
            .clickable { onClickListener() }
            .padding(vertical = 6.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = text,
            fontFamily = fontFamily,
            fontSize = 14.sp,
            color = if (state) Color.White else lightTextAccent
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .size(18.dp),
            tint = if (state) Color.White else lightTextAccent
        )
    }
}