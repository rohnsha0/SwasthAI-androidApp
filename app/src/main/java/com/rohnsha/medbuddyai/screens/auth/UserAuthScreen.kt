package com.rohnsha.medbuddyai.screens.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.R
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.domain.viewmodels.userAuthVM
import com.rohnsha.medbuddyai.navigation.bottombar.bottomNavItems
import com.rohnsha.medbuddyai.screens.TextInputThemed
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAuthScreen(
    mode: Int, // 1 for login, 0 for signup
    userAuthVM: userAuthVM,
    navController: NavHostController,
    snackBarToggleVM: snackBarToggleVM
) {

    val selectedMode= remember {
        mutableStateOf(mode)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (selectedMode.value == 0) "Hello" else "Welcome Back",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                actions = {
                    Image(
                        imageVector = Icons.Outlined.AdminPanelSettings,
                        contentDescription = "Show accuracy button",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp)
                            .clickable {
                                //navController.navigate(bottomNavItems.Preferences.route)
                            }
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
    ){
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .scrollable(
                    orientation = Orientation.Vertical,
                    enabled = true,
                    state = scrollState
                )
                .background(BGMain)
        ) {

            Spacer(modifier = Modifier.height(48.dp))
            Image(painter = painterResource(id = R.drawable.login), contentDescription = null, modifier= Modifier.padding(horizontal = 25.dp))
            when (selectedMode.value) {
                0 -> { RegisterUI(userAuthVM = userAuthVM, selectedMode = { selectedMode.value= it }, onSucess = { navController.navigate(
                    bottomNavItems.Home.route) }, snackBarToggleVM = snackBarToggleVM)  }
                1 -> { LoginUI(userAuthVM, selectedMode = { selectedMode.value= it }, onSucess = { navController.navigate(
                    bottomNavItems.Home.route) }) }
            }

        }
    }

}

@Composable
fun RegisterUI(
    userAuthVM: userAuthVM,
    onSucess: () -> Unit,
    selectedMode: (Int) -> Unit,
    snackBarToggleVM: snackBarToggleVM
) {

    val firstName= remember {
        mutableStateOf("")
    }
    val lastName= remember {
        mutableStateOf("")
    }
    val mail= remember {
        mutableStateOf("")
    }
    val password= remember {
        mutableStateOf("")
    }
    val username= remember {
        mutableStateOf("")
    }
    val scope= rememberCoroutineScope()
    val isUsernameValid= remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = username.value != "" && !isUsernameValid.value) {
        Log.d("Username", isUsernameValid.value.toString())
        try {
            isUsernameValid.value = userAuthVM.isUsernameValid(username.value)
            Log.d("Username", "isUsernameValid: ${isUsernameValid.value}")
        } catch (e: Exception) {
            Log.d("Username", "error: ${e.message}")
            snackBarToggleVM.SendToast(
                message = e.message.toString(),
                indicator_color = Color.Red,
                icon = Icons.Outlined.Warning,
            )
        }
    }

    Log.d("Username", "usernmae; ${isUsernameValid.value}")


    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                TextInputThemed(
                    value = firstName.value,
                    onValueChanged = { firstName.value = it },
                    label = "First name",
                    onClose = { /*TODO*/ },
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextInputThemed(
                    value = lastName.value,
                    onValueChanged = { lastName.value = it },
                    label = "Last name",
                    onClose = { /*TODO*/ },
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextInputThemed(
                    value = mail.value,
                    onValueChanged = { mail.value = it },
                    label = "Email address",
                    onClose = { /*TODO*/ },
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextInputThemed(
                    modifier2 = Modifier
                        .onFocusChanged { focusState ->
                            when {
                                focusState.isFocused -> {
                                    Log.d("Focus", "Focused")
                                    scope.launch {
                                        isUsernameValid.value = userAuthVM.isUsernameValid(username.value)
                                    }
                                }

                                focusState.hasFocus ->
                                    Log.d("Focus", "Has focus")


                                else ->
                                    Log.d("Focus", "Not focused")
                            }
                        },
                    value = username.value,
                    onValueChanged = { username.value = it },
                    label = "Username",
                    onClose = { /*TODO*/ },
                )
                Spacer(modifier = Modifier.height(15.dp))

                TextInputThemed(
                    value = password.value,
                    onValueChanged = { password.value = it },
                    label = "Password",
                    onClose = { /*TODO*/ },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Spacer(modifier = Modifier.height(24.dp))
                AuthCTA(btnAction = {
                    scope.launch {
                        if (isUsernameValid.value){
                            userAuthVM.registerUser(
                                password.value,
                                mail.value, onSucess,
                                fname = firstName.value,
                                lname = lastName.value,
                                username = username.value,
                                snackBarToggleVM = snackBarToggleVM
                            )
                        } else {
                            if (username.value==""){
                                snackBarToggleVM.SendToast(
                                    message = "Username cant be empty",
                                    indicator_color = Color.Red,
                                    icon = Icons.Outlined.Warning,
                                )
                            } else {
                                val isUsernameTaken = userAuthVM.isUsernameValid(username.value)
                                if (!isUsernameTaken){
                                    userAuthVM.registerUser(
                                        password.value,
                                        mail.value,
                                        onSucess,
                                        fname = firstName.value,
                                        lname = lastName.value,
                                        username = username.value,
                                        snackBarToggleVM = snackBarToggleVM
                                    )
                                } else {
                                    snackBarToggleVM.SendToast(
                                        message = "Username already taken",
                                        indicator_color = Color.Red,
                                        icon = Icons.Outlined.Warning,
                                    )
                                }
                            }
                        }
                    }
                }, txtAction =  {
                    selectedMode(1)
                })
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun LoginUI(userAuthVM: userAuthVM, onSucess: () -> Unit, selectedMode: (Int) -> Unit) {

    val mail= remember {
        mutableStateOf("")
    }

    val password= remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        TextInputThemed(
            value = mail.value,
            onValueChanged = { mail.value = it },
            label = "Enter your mail",
            onClose = { /*TODO*/ },
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextInputThemed(
            value = password.value,
            onValueChanged = { password.value = it },
            label = "Enter your Password",
            onClose = { /*TODO*/ },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.weight(1f))
        AuthCTA(btnAction = { userAuthVM.loginUser(password.value, mail.value, onSucess) }, txtAction = {
            selectedMode(0)
        })
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun AuthCTA(
    btnAction: () -> Unit,
    txtAction: () -> Unit,
) {
    Button(
        onClick = { btnAction() },
        colors = ButtonDefaults.buttonColors(
            containerColor = customBlue
        )
    ) {
        Text(text = "Login", color = Color.White, fontSize = 18.sp, fontFamily = fontFamily)
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row {
        Text(text = "Don't have an account? ", fontSize = 14.sp, fontFamily = fontFamily, color = Color.Black)
        Text(text = "Sign up", fontSize = 14.sp, fontFamily = fontFamily, color = Color.Black, fontWeight = FontWeight(600),
            modifier = Modifier.clickable { txtAction() })
    }
    Spacer(modifier = Modifier.height(25.dp))
}