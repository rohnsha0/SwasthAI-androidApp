package com.rohnsha.medbuddyai.database.userdata.iFasting

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intFastingTable")
data class fasting(
    @PrimaryKey(autoGenerate = false)
    val timeStamp: Long,
    val targetTime: Long,
    val isCompleted: Boolean
)
