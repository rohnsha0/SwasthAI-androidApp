package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rohnsha.medbuddyai.api.chatbot.chatbot_obj.chatService
import com.rohnsha.medbuddyai.database.userdata.chatbot.chatDB_VM
import com.rohnsha.medbuddyai.database.userdata.chatbot.chats.chatEntity
import com.rohnsha.medbuddyai.database.userdata.chatbot.messages.messageEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

class chatVM: ViewModel() {

    private val _listMessages= MutableSharedFlow<messageEntity>()
    val messagesList= _listMessages.asSharedFlow()

    private val _messageCount= MutableStateFlow(0)
    val messageCount= _messageCount.asStateFlow()

    suspend fun chat(
        message: String,
        resetMessageFeild: () -> Unit,
        vmChat: chatDB_VM,
        chatID: Int,
        mode: Int //0 -> qna, 1 -> ai_symptoms_checker
    ){

        vmChat.addMessages(messageEntity(
            message = message,
            isBotMessage = false,
            timestamp = System.currentTimeMillis(),
            isError = false,
            chatId = chatID
        ))

        val dynamicURL= when(mode){
            0 -> {
                "https://api-jjtysweprq-el.a.run.app/chat/$message"
            }
            1 -> {
                "https://api-jjtysweprq-el.a.run.app/symptoms/$message"
            }
            else -> {
                "https://api-jjtysweprq-el.a.run.app/chat/$message"
            }
        }
        if (_messageCount.value==0){
            vmChat.addChat(
                chatEntity(timestamp = System.currentTimeMillis())
            )
        }
        try {
            _listMessages.emit(messageEntity(message = message, isBotMessage =  false, timestamp = System.currentTimeMillis(), isError = false, chatId = chatID))
            _messageCount.value += 1
            resetMessageFeild()
            val response= chatService.getChatReply(dynamicURL)
            val resultAPI= messageEntity(
                message = response.message,
                isBotMessage = true,
                timestamp = System.currentTimeMillis(),
                isError = false,
                chatId = chatID
            )
            _listMessages.emit(resultAPI)
            _messageCount.value += 1
            vmChat.addMessages(resultAPI)
            Log.d("errorChat", response.message)
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
                    val errorData= messageEntity(
                        message = errorMessage,
                        isBotMessage = true,
                        timestamp = System.currentTimeMillis(),
                        isError = true,
                        chatId = chatID
                    )
                    _listMessages.emit(errorData)
                    vmChat.addMessages(errorData)
                }
                is IOException -> {
                    Log.d("errorChat", e.stackTrace.toString())
                    Log.d("errorChat", "Network error: ${e.message}")
                    Log.d("errorChat", e.toString())
                    val errorData= messageEntity(
                        message = "Network error occurred, please check your connection",
                        isBotMessage = true,
                        timestamp = System.currentTimeMillis(),
                        isError = true,
                        chatId = chatID
                    )
                    _listMessages.emit(errorData)
                    vmChat.addMessages(errorData)
                }
                else -> {
                    Log.d("errorChat", e.stackTrace.toString())
                    Log.d("errorChat", e.message ?: "An unknown error occurred")
                    Log.d("errorChat", e.toString())
                    val errorData= messageEntity(
                        message = "An unknown error occurred, please try again later",
                        isBotMessage = true,
                        timestamp = System.currentTimeMillis(),
                        isError = true,
                        chatId = chatID
                    )
                    _listMessages.emit(errorData)
                    vmChat.addMessages(errorData)
                }
            }
            _messageCount.value += 1
        }
    }

}