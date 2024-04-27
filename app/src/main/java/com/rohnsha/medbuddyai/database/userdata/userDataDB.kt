package com.rohnsha.medbuddyai.database.userdata

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.rohnsha.medbuddyai.database.userdata.chatbot.chats.chatDAO
import com.rohnsha.medbuddyai.database.userdata.chatbot.chats.chatEntity
import com.rohnsha.medbuddyai.database.userdata.chatbot.messages.messageDAO
import com.rohnsha.medbuddyai.database.userdata.chatbot.messages.messageEntity
import com.rohnsha.medbuddyai.database.userdata.disease.diseaseDAO
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistory
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryDAO
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass

@Database(
    entities = [scanHistory::class, disease_data_dataClass::class, chatEntity::class, messageEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(2, 3, AutoMig23::class),
                     ],
    exportSchema = true
)
abstract class userDataDB: RoomDatabase() {

    abstract fun scanDAO(): scanHistoryDAO
    abstract fun diseaseDAO(): diseaseDAO

    abstract fun chatDA0(): chatDAO
    abstract fun messageDAO(): messageDAO

    companion object{
        @Volatile
        private var INSTANCE: userDataDB? = null

        fun getUserDBRefence(context: Context): userDataDB {
            val getUserDBtemp = INSTANCE
            if (getUserDBtemp!=null){
                return getUserDBtemp
            }
            synchronized(this){
                val instanceUserDB= Room.databaseBuilder(
                    context.applicationContext,
                    userDataDB::class.java,
                    "userDB"
                ).build()
                INSTANCE= instanceUserDB
                return instanceUserDB
            }
        }
    }
}

@DeleteTable(
    tableName = "intFastingTable"
)
class AutoMig23: AutoMigrationSpec