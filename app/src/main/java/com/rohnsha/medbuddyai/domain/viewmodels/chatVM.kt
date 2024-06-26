package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rohnsha.medbuddyai.api.chatbot.chatbot_obj.chatService
import com.rohnsha.medbuddyai.database.appData.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.database.appData.symptoms.symptomDC
import com.rohnsha.medbuddyai.database.userdata.chatbot.chatDB_VM
import com.rohnsha.medbuddyai.database.userdata.chatbot.chats.chatEntity
import com.rohnsha.medbuddyai.database.userdata.chatbot.messages.messageEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import kotlin.math.pow

class chatVM: ViewModel() {

    private val _listMessages= MutableSharedFlow<messageEntity>()
    val messagesList= _listMessages.asSharedFlow()

    private val _messageCount= MutableStateFlow(0)
    val messageCount= _messageCount.asStateFlow()

    private var retryCount = 0

    suspend fun endSymptomTest(mode: Int, chatID: Int){
        if (mode==1){
            Log.d("chatVM", "symptoms init")
            _listMessages.emit(
                messageEntity(
                    message = "Either send the message for getting the final disease, or add the symptom by search",
                    isBotMessage = true,
                    timestamp = System.currentTimeMillis(),
                    isError = false,
                    chatId = chatID
                )
            )
            Log.d("chatVM", "symptom done")
            _messageCount.value += 1
            Log.d("chatVM", "incremnetd done")

        }
        Log.d("chatVM", _listMessages.toString())
    }

    suspend fun symptomChat(
        symptom: String,
        isRetrying: Boolean= false,
        vmChat: chatDB_VM,
        diseaseDBviewModel: diseaseDBviewModel,
        chatID: Int, outcome: ((symptomDC) -> Unit)? = null
    ){
        if (_messageCount.value==0){
            vmChat.addChat(
                chatEntity(timestamp = System.currentTimeMillis(), mode = 1, id = chatID)
            )
        }
        Log.d("chatVMSymptom", symptom)
        Log.d("chatVMSymptom", symptom.substringAfterLast(", ").trim())
        if (!isRetrying){
            val messageBody= messageEntity(
                message = "Selected symptom: ${symptom.substringAfterLast(", ").trim()}",
                isBotMessage =  false,
                timestamp = System.currentTimeMillis(),
                isError = true,
                chatId = chatID
            )
            vmChat.addMessages(messageBody)
            _listMessages.emit(
                messageBody
            )
            _messageCount.value += 1
        }

        val dynamicURL= "https://api-jjtysweprq-el.a.run.app/next_symptom/$symptom"
        try {
            val response= chatService.getChatReply(dynamicURL)
            val symptomData= diseaseDBviewModel.searchSymptomByAbbreviation(response.message)
            val resultAPI= messageEntity(
                message = "You may also have: ${symptomData.symptom}",
                isBotMessage = true,
                timestamp = System.currentTimeMillis(),
                isError = false,
                chatId = chatID
            )
            if (outcome != null) {
                outcome(symptomData)
            }
            _listMessages.emit(resultAPI)
            _messageCount.value += 1
            vmChat.addMessages(resultAPI)
            Log.d("errorChat", response.message)
        } catch (e: Exception){
            symptomChat(symptom = symptom, vmChat =  vmChat, chatID =  chatID, isRetrying = true, diseaseDBviewModel = diseaseDBviewModel)
        }
    }

    suspend fun chat(
        message: String,
        resetMessageFeild: () -> Unit,
        vmChat: chatDB_VM,
        chatID: Int,
        isRetrying: Boolean= false,
        mode: Int //0 -> qna, 1 -> ai_symptoms_checker
    ){
        if (_messageCount.value==0){
            vmChat.addChat(
                chatEntity(timestamp = System.currentTimeMillis(), mode = mode, id = chatID)
            )
        }
        if (!isRetrying){
            val messageBody= messageEntity(
                message = message,
                isBotMessage =  false,
                timestamp = System.currentTimeMillis(),
                isError = false,
                chatId = chatID
            )
            vmChat.addMessages(messageBody)
            _listMessages.emit(
                messageBody
            )
            _messageCount.value += 1
            resetMessageFeild()
        }

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
        try {
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

            val delayMillis = calculateExponentialBackoff(retryCount)
            val retryMessage= messageEntity(
                message = "Retrying in ${delayMillis/1000} second(s)",
                isBotMessage = true,
                timestamp = System.currentTimeMillis(),
                isError = true,
                chatId = chatID
            )
            _listMessages.emit(retryMessage)
            vmChat.addMessages(retryMessage)
            delay(delayMillis)
            chat(message = message, resetMessageFeild = resetMessageFeild, vmChat = vmChat, chatID = chatID, mode = mode, isRetrying = true)
        }
    }

    private fun calculateExponentialBackoff(retryCount: Int): Long {
        // Exponential backoff formula: base * 2^retryCount
        val base = 1000L // Base delay in milliseconds (1 second)
        val delayMillis = base * (2.0).pow(retryCount.toDouble()).toLong()
        return delayMillis.coerceAtMost(30000L) // Cap the maximum delay to 30 seconds
    }

}