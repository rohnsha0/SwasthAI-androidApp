package com.rohnsha.medbuddyai.database.appData.disease_questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rohnsha.medbuddyai.database.appData.appDataDB

class questionVM(application: Application): AndroidViewModel(application) {

    private val repo: questionRepo
    private val dao: questionDAO

    init {
        dao= appDataDB.getAppDBReference(application).questionsDAO()
        repo= questionRepo(dao)
    }

    suspend fun insert(questions: questions){
        repo.insert(questions)
    }

}