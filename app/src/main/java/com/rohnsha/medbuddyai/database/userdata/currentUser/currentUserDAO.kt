package com.rohnsha.medbuddyai.database.userdata.currentUser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface currentUserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: fieldValueDC)

    @Query("SELECT * FROM currentUserTable where field like :fieldQuery")
    suspend fun getFieldData(fieldQuery: String): fieldValueDC
}