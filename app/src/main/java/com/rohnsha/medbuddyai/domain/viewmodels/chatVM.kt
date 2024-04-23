package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rohnsha.medbuddyai.api.chatbot.chatbot_obj.chatService
import com.rohnsha.medbuddyai.domain.dataclass.messageDC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

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
            Log.d("errorChat", e.toString())
            _listMessages.emit(messageDC("there might be any other issues", true, System.currentTimeMillis()))
        }
    }

}