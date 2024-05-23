package com.rohnsha.medbuddyai.database.userdata.currentUser

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currentUserTable")
data class fieldValueDC(
    @PrimaryKey
    val field: String,
    val value: String
)
