package com.rohnsha.medbuddyai.database.userdata.scan_history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class scanHistory(
    @PrimaryKey(autoGenerate = false)
    val timestamp: Long,
    val title: String,
    val domain: String,
)
