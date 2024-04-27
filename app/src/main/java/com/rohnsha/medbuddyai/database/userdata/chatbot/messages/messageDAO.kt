package com.rohnsha.medbuddyai.database.userdata.chatbot.messages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface messageDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addChat(messageEntity: messageEntity)

}