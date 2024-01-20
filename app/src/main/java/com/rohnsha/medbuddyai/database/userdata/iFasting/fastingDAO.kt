package com.rohnsha.medbuddyai.database.userdata.iFasting

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface fastingDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScanHistory(fasting: fasting)

}