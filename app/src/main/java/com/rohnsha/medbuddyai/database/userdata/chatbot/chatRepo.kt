package com.rohnsha.medbuddyai.database.userdata.chatbot

import com.rohnsha.medbuddyai.database.userdata.chatbot.chats.chatDAO
import com.rohnsha.medbuddyai.database.userdata.chatbot.chats.chatEntity
import com.rohnsha.medbuddyai.database.userdata.chatbot.messages.messageDAO
import com.rohnsha.medbuddyai.database.userdata.chatbot.messages.messageEntity
import kotlinx.coroutines.flow.Flow

class chatRepo(private val chatDAO: chatDAO, private val  messageDAO: messageDAO) {

    val chatHistory: Flow<List<chatEntity>> = chatDAO.readChats()

    suspend fun addChat(chatEntity: chatEntity){
        chatDAO.addChat(chatEntity)
    }

    suspend fun addMessages(messageEntity: messageEntity){
        messageDAO.addChat(messageEntity)
    }

    suspend fun readChats(chatID: Int): List<chatWithMessage> {
        return chatDAO.getChatWithMessages(chatID)
    }

}