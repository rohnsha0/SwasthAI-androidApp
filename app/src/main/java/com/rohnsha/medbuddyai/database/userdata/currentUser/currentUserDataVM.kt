package com.rohnsha.medbuddyai.database.userdata.currentUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rohnsha.medbuddyai.database.userdata.userDataDB

class currentUserDataVM(application: Application): AndroidViewModel(application) {

    private val repo: currentUserRepo
    private val dao: currentUserDAO

    init {
        dao= userDataDB.getUserDBRefence(application).currentUserDAO()
        repo= currentUserRepo(dao)
    }

    suspend fun addDataCurrentUser(data: fieldValueDC){
        repo.addUserData(data)
    }

}