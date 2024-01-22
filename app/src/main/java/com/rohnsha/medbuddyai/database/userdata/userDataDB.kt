package com.rohnsha.medbuddyai.database.userdata

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rohnsha.medbuddyai.database.userdata.disease.diseaseDAO
import com.rohnsha.medbuddyai.database.userdata.iFasting.fasting
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistory
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistoryDAO
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass

@Database(
    entities = [scanHistory::class, fasting::class, disease_data_dataClass::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
abstract class userDataDB: RoomDatabase() {

    abstract fun scanDAO(): scanHistoryDAO
    abstract fun diseaseDAO(): diseaseDAO

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