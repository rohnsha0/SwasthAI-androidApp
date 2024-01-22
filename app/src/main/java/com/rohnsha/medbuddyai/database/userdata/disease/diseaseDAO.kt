package com.rohnsha.medbuddyai.database.userdata.disease

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass

@Dao
interface diseaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDiseaseData(diseaseDataDataclass: disease_data_dataClass)

}