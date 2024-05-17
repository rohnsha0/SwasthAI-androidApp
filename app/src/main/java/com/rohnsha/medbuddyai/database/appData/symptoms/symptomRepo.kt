package com.rohnsha.medbuddyai.database.appData.symptoms

class symptomRepo(private val symptomDAO: symptomDAO) {

    suspend fun addSymptom(symptom: symptomDC) {
        symptomDAO.addSymptom(symptom)
    }

}