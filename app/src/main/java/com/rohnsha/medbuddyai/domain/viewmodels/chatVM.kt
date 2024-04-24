package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rohnsha.medbuddyai.api.chatbot.chatbot_obj.chatService
import com.rohnsha.medbuddyai.domain.dataclass.messageDC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

class chatVM: ViewModel() {

    private val _listMessages= MutableSharedFlow<messageDC>()
    val messagesList= _listMessages.asSharedFlow()

    private val _messageCount= MutableStateFlow(0)
    val messageCount= _messageCount.asStateFlow()

    suspend fun chat(message: String, resetMessageFeild: () -> Unit, onCompletion: () -> Unit){
        val dynamicURL= "https://api-jjtysweprq-el.a.run.app/chat/$message"
        try {
            _listMessages.emit(messageDC(message, false, System.currentTimeMillis()))
            _messageCount.value += 1
            resetMessageFeild()
            val response= chatService.getChatReply(dynamicURL)
            _listMessages.emit(messageDC(response.message, true, System.currentTimeMillis()))
            _messageCount.value += 1
            Log.d("errorChat", response.message)
            onCompletion()
        } catch (e: Exception){
            when (e) {
                is retrofit2.HttpException -> {
                    val errorMessage = when (e.code()) {
                        504 -> "Gateway timeout: The server is not responding"
                        500 -> "Internal server error"
                        404 -> "Resource not found"
                        // Add more cases for other HTTP error codes
                        else -> "An HTTP error occurred: ${e.code()} ${e.message()}"
                    }
                    Log.d("errorChat", e.stackTrace.toString())
                    Log.d("errorChat", errorMessage)
                    Log.d("errorChat", e.toString())
                    _listMessages.emit(messageDC(errorMessage, true, System.currentTimeMillis()))
                }
                is IOException -> {
                    Log.d("errorChat", e.stackTrace.toString())
                    Log.d("errorChat", "Network error: ${e.message}")
                    Log.d("errorChat", e.toString())
                    _listMessages.emit(messageDC("Network error occurred, please check your connection", true, System.currentTimeMillis()))
                }
                else -> {
                    Log.d("errorChat", e.stackTrace.toString())
                    Log.d("errorChat", e.message ?: "An unknown error occurred")
                    Log.d("errorChat", e.toString())
                    _listMessages.emit(messageDC("An unknown error occurred, please try again later", true, System.currentTimeMillis()))
                }
            }
            _messageCount.value += 1
        }
    }

}