package com.rohnsha.medbuddyai.database.userdata.scan_history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface scanHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScanHistory(scanHistory: scanHistory)

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    fun readScanHistory(): Flow<List<scanHistory>>

    @Query("select count(*) from scan_history")
    suspend fun getScanHistoryCounts(): Int

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC LIMIT 1")
    suspend fun getMostRecentEntry(): scanHistory
}