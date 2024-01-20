package com.rohnsha.medbuddyai.database.userdata.scan_history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface scanHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScanHistory(scanHistory: scanHistory)

}