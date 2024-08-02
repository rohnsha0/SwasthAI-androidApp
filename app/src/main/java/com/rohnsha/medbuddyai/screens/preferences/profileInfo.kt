package com.rohnsha.medbuddyai.screens.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.database.userdata.currentUser.currentUserDataVM
import com.rohnsha.medbuddyai.database.userdata.currentUser.fieldValueDC
import com.rohnsha.medbuddyai.screens.TextInputThemed
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoScreen(
    currentUserDataVM: currentUserDataVM,
    navController: NavHostController
) {

    val userInfo= remember {
        mutableStateOf(fieldValueDC(
            username = "",
            fname = "",
            lname = "",
            isDefaultUser = true
        ))
    }
    LaunchedEffect(key1 = true) {
        userInfo.value= currentUserDataVM.getAllUsers().filter { it.isDefaultUser }[0]
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile Info",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BGMain
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back Icon"
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ){ values ->

        Column(
            modifier = Modifier.padding(values)
        ) {
            Text(
                text = "We get your personal information from the verification process. If you want to make changes on your personal information, contact our support.",
                fontFamily = fontFamily,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp),
                textAlign = TextAlign.Center,
                color = lightTextAccent
            )
            Column(
                modifier = Modifier
                    .padding(top = 34.dp)
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(top = 30.dp, start = 24.dp, end = 24.dp)
            ){
                TextInputThemed(
                    value = userInfo.value.fname,
                    onValueChanged = {  },
                    label = "First Name",
                    onClose = {  },
                    isEnabled = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextInputThemed(
                    value = userInfo.value.lname,
                    onValueChanged = {  },
                    label = "Last Name",
                    onClose = {  },
                    isEnabled = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextInputThemed(
                    value = userInfo.value.username,
                    onValueChanged = {  },
                    label = "Username",
                    onClose = {  },
                    isEnabled = false
                )
            }
        }
    }
}