package com.rohnsha.medbuddyai

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Transgender
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customRed
import com.rohnsha.medbuddyai.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIScreen(
    padding: PaddingValues,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "BMI Calculator",
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
        Column(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(top = 30.dp)
        ){
            val height= remember {
                mutableStateOf("")
            }
            val weight= remember {
                mutableStateOf("")
            }
            val age= remember {
                mutableStateOf("")
            }
            val genderColExpanded= remember {
                mutableStateOf(false)
            }
            val gender= remember {
                mutableIntStateOf(0)
            } // 1-> Male, 2-> Female

            Text(
                text = "Key Data",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextInputThemed(value = height.value, onValueChanged = { height.value= it },
                icon = Icons.Outlined.Height, placeholder = "Enter your height",
                onClose = { height.value= "" }, regex = "^(?:5[6-9]|[6-9]\\d|1\\d\\d|22[0-5])\$\n", suffix = "cm",
                isNumKey = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextInputThemed(value = weight.value, onValueChanged = { weight.value= it },
                icon = Icons.Outlined.Scale, placeholder = "Enter your weight",
                onClose = { weight.value= "" }, suffix = "kg", isNumKey = true, regex = "\\b(?:[2-9]|[1-9][0-9]|110)\\b\n"
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Get more insights (optional)",
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextInputThemed(
                value = age.value,
                onValueChanged = { age.value= it },
                icon = Icons.Outlined.Add,
                placeholder = "Enter your age",
                onClose = { /*TODO*/ },
                isNumKey = true,
                suffix = "years"
            )
            Spacer(modifier = Modifier.height(12.dp))
            ExposedDropdownMenuBox(
                expanded = genderColExpanded.value,
                onExpandedChange = { genderColExpanded.value = it },
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                TextInputThemed(
                    value = when(gender.intValue){
                        1 -> "Male"
                        2 -> "Female"
                        else -> ""
                    },
                    onValueChanged = {  },
                    icon = when(gender.intValue){
                        1 -> Icons.Outlined.Male
                        2 -> Icons.Outlined.Female
                        else -> Icons.Outlined.Transgender
                        },
                    placeholder = "Select Gender",
                    onClose = {  },
                    isNumKey = true,
                    readOnly = true,
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = genderColExpanded.value,
                    onDismissRequest = { genderColExpanded.value = false },
                    ) {
                    DropdownMenuItem(text = { Text(text = "Male") }, onClick = {
                        gender.intValue= 1
                        genderColExpanded.value = false
                    })
                    DropdownMenuItem(text = { Text(text = "Female") }, onClick = {
                        gender.intValue= 2
                        genderColExpanded.value = false
                    })
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 26.dp)
                    .align(Alignment.CenterHorizontally)
                ,
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Analyze",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = fontFamily
                )
            }
        }
    }
}

@Composable
fun TextInputThemed(
    value: String,
    onValueChanged: (String) -> Unit,
    icon: ImageVector,
    placeholder: String,
    onClose: () -> Unit,
    regex: String?=null,
    suffix: String? = null,
    isNumKey: Boolean,
    readOnly: Boolean= false,
    modifier: Modifier= Modifier
) {
    val errorState= remember {
        mutableStateOf(false)
    }
    if (regex!=null){
        errorState.value= !value.matches(Regex(regex)) && value!= ""
    }
    TextField(
        modifier = Modifier
            .then(
                if (readOnly) {
                    Modifier
                } else {
                    Modifier.padding(horizontal = 24.dp)
                }
            )
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .then(
                if (readOnly) {
                    modifier
                } else {
                    Modifier
                }
            ),
        value = value,
        readOnly = readOnly,
        onValueChange = { onValueChanged(it) },
        leadingIcon = {
            Image(
                modifier = Modifier
                    .size(34.dp)
                    .background(Color.White, CircleShape)
                    .padding(5.dp),
                imageVector = icon,
                contentDescription = null
            )
        },
        suffix = { if (suffix!=null) Text(text = suffix) },
        keyboardOptions = if (isNumKey) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions(keyboardType = KeyboardType.Text),
        trailingIcon = {
            if (value!="" && !readOnly){
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable {
                            onClose()
                        }
                        .padding(3.dp),
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close Icon"
                )
            }
        },
        placeholder = { Text(text = placeholder) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = ViewDash,
            unfocusedContainerColor = ViewDash,
            focusedIndicatorColor = BGMain,
            unfocusedIndicatorColor = BGMain,
            cursorColor = Color.Black,
            errorContainerColor = ViewDash,
            errorIndicatorColor = customRed,
            errorSupportingTextColor = customRed,
        ),
        isError = errorState.value,
        singleLine = true
    )
}

