package com.rohnsha.medbuddyai.database.appData.symptoms

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface symptomDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSymptom(symptom: symptomDC)

    @Query("SELECT * FROM symptoms order by RANDOM() LIMIT 20")
    suspend fun getAllSymptoms(): List<symptomDC>
}