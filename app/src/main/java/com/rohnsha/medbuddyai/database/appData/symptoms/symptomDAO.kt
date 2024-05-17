package com.rohnsha.medbuddyai.database.appData.symptoms

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface symptomDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSymptom(symptom: symptomDC)

}