package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohnsha.medbuddyai.ui.theme.dashBG
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class snackBarToggleVM: ViewModel() {

    private val _readyToSend= MutableStateFlow(false)
    val readyToSendToast= _readyToSend.asStateFlow()

    private val _message= MutableStateFlow("")
    private val _color= MutableStateFlow(Color.Black)
    private val _padding= MutableStateFlow(PaddingValues(0.dp))

    fun SendToast(
        message: String,
        indicator_color: Color,
        padding: PaddingValues,
    ){
        viewModelScope.launch {
            Log.d("snackbarStateTrue", "doing truth")
            _message.value=message
            _color.value= indicator_color
            _padding.value= padding
            _readyToSend.value= true
            Log.d("snackbarStateTrue", "done truth")
        }
    }

    @Composable
    fun MySnackbar() {

        val snackbarHostState = remember { SnackbarHostState() }

        SnackbarHost(
            hostState = snackbarHostState
        ) {
            Row(
                modifier = Modifier
                    .padding(_padding.collectAsState().value)
                    .padding(horizontal = 25.dp, vertical = 16.dp)
                    .height(56.dp)
                    .background(color = dashBG, shape = RoundedCornerShape(size = 28.dp))
                    .shadow(elevation = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                        .background(_color.collectAsState().value, CircleShape)
                        .padding(4.dp),
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "snackbar image"
                )
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    text = _message.collectAsState().value,
                    color = Color.White,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    fontFamily = fontFamily
                )
                Box(modifier = Modifier.fillMaxWidth()){
                    Image(
                        modifier = Modifier
                            .size(12.dp)
                            .clickable {
                                snackbarHostState.currentSnackbarData?.dismiss()
                            },
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "snackbar image",
                        colorFilter = ColorFilter.tint(color = Color.White)
                    )
                }
            }
        }


        LaunchedEffect(key1 = true) {
            delay(500L)
            snackbarHostState.showSnackbar(
                message = _message.value,
                actionLabel = null,
                duration = SnackbarDuration.Short
            )
            _readyToSend.value= false
        }
    }

}