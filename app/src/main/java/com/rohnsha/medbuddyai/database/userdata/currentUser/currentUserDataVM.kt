package com.rohnsha.medbuddyai.database.userdata.currentUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rohnsha.medbuddyai.database.userdata.userDataDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class currentUserDataVM(application: Application): AndroidViewModel(application) {

    private val repo: currentUserRepo
    private val dao: currentUserDAO

    private val _defaultUserIndex= MutableStateFlow(1)
    val defaultUserIndex= _defaultUserIndex.asStateFlow()

    private val _userName= MutableStateFlow("")
    val userName= _userName.asStateFlow()

    init {
        dao= userDataDB.getUserDBRefence(application).currentUserDAO()
        repo= currentUserRepo(dao)
    }

    suspend fun addDataCurrentUser(data: fieldValueDC){
        repo.addUserData(data)
    }

    suspend fun getQueryData(isDefaultUser: Boolean){
        _userName.value= repo.searchQuery(isDefaultUser).username
    }

    suspend fun getAllUsers(): List<fieldValueDC>{
        return repo.getAllUsers()
    }

    suspend fun getUserInfo(userIndex: Int): fieldValueDC{
        return repo.getUserInfo(userIndex)
    }

    suspend fun deleteUser(userIndex: Int){
        repo.deleteUser(userIndex)
    }

    fun switchDefafultUser(index: Int){
        _defaultUserIndex.value= index
    }
}