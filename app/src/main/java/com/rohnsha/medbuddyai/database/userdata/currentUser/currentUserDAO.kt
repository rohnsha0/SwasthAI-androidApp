package com.rohnsha.medbuddyai.database.userdata.currentUser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface currentUserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: fieldValueDC)

}