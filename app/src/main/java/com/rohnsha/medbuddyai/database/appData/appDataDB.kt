package com.rohnsha.medbuddyai.database.appData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rohnsha.medbuddyai.database.appData.disease.diseaseDAO
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass

@Database(
    entities = [disease_data_dataClass::class],
    version = 1,
    exportSchema = true
)
abstract class appDataDB: RoomDatabase() {

    abstract fun diseaseDAO(): diseaseDAO

    companion object{
        @Volatile
        private var INSTANCE: appDataDB? = null

        fun getAppDBReference(context: Context): appDataDB {
            val getUserDBtemp = INSTANCE
            if (getUserDBtemp!=null){
                return getUserDBtemp
            }
            synchronized(this){
                val instanceUserDB= Room.databaseBuilder(
                    context.applicationContext,
                    appDataDB::class.java,
                    "appDataDB"
                ).build()
                INSTANCE= instanceUserDB
                return instanceUserDB
            }
        }
    }

}