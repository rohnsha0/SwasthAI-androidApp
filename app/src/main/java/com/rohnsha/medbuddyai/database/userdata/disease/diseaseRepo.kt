package com.rohnsha.medbuddyai.database.userdata.disease

import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass

class diseaseRepo(private val diseaseDAO: diseaseDAO) {

    suspend fun addDisease(diseaseDataDataclass: disease_data_dataClass){
        diseaseDAO.addDiseaseData(diseaseDataDataclass)
    }

}