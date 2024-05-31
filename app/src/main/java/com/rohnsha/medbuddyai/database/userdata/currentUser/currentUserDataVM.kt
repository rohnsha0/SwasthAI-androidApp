package com.rohnsha.medbuddyai.database.userdata.currentUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rohnsha.medbuddyai.database.userdata.userDataDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class currentUserDataVM(application: Application): AndroidViewModel(application) {

    private val repo: currentUserRepo
    private val dao: currentUserDAO

    private val _userName= MutableStateFlow("")
    val userName= _userName.asStateFlow()

    init {
        dao= userDataDB.getUserDBRefence(application).currentUserDAO()
        repo= currentUserRepo(dao)
    }

    suspend fun addDataCurrentUser(data: fieldValueDC){
        repo.addUserData(data)
    }

    suspend fun getQueryData(query: String){
        _userName.value= repo.searchQuery(query).value
    }

}