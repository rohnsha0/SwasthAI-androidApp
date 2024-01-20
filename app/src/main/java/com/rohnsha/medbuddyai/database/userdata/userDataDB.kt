package com.rohnsha.medbuddyai.database.userdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rohnsha.medbuddyai.database.userdata.iFasting.fasting
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistory
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryDAO

@Database(entities = [scanHistory::class, fasting::class], version = 1)
abstract class userDataDB: RoomDatabase() {

    abstract fun scanDAO(): scanHistoryDAO

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