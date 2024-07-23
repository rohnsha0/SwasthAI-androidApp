package com.rohnsha.medbuddyai.database.userdata.keys

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rohnsha.medbuddyai.database.userdata.userDataDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class keyVM(application: Application): AndroidViewModel(application) {

    private val repo: keyRepo
    private val dao: keyDAO

    private val _defaultEngine= MutableStateFlow("testOne")
    val defaultEngine= _defaultEngine.asStateFlow()

    init {
        dao= userDataDB.getUserDBRefence(application).keyDAO()
        repo= keyRepo(dao)
    }

    suspend fun addKeySecretPair(keyDC: keyDC){
        repo.insertKeyPair(keyDC)
    }

    suspend fun getSecretKey(serviceName: String): String {
        return repo.querySecrets(serviceName)
    }

    suspend fun getKeySecretPairs(): List<keyDC> {
        return repo.queryKeyPairs()
    }

    fun switchDefaultEngine(engine: String){
        _defaultEngine.value= engine
    }
}