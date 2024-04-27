package com.rohnsha.medbuddyai.database.userdata.chatbot.messages

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages"
)
data class messageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val message: String,
    val isBotMessage: Boolean,
    val timestamp: Long,
    val isError: Boolean,
    val chatId: Int
)
