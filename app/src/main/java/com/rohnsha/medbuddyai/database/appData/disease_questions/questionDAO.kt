package com.rohnsha.medbuddyai.database.appData.disease_questions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface questionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: questions)

}