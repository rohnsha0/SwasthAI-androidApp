package com.rohnsha.medbuddyai.database.userdata.keys

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.rohnsha.medbuddyai.database.userdata.userDataDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class keyVM(application: Application): AndroidViewModel(application) {

    private val repo: keyRepo
    private val dao: keyDAO

    private val _defaultEngine= MutableStateFlow(keyDC(
        serviceName = "swasthai",
        secretKey = "secretOne"
    ))
    val defaultEngine= _defaultEngine.asStateFlow()

    init {
        dao= userDataDB.getUserDBRefence(application).keyDAO()
        repo= keyRepo(dao)
    }

    private suspend fun addKeySecretPair(keyDC: keyDC){
        repo.insertKeyPair(keyDC)
    }

    suspend fun getSecretKey(serviceName: String): keyDC {
        return repo.querySecrets(serviceName)
    }

    suspend fun getKeySecretPairs(): List<keyDC> {
        return repo.queryKeyPairs()
    }

    fun switchDefaultEngine(engine: keyDC){
        _defaultEngine.value= engine
    }

    suspend fun updateKeySecretPair(keyDC: List<keyDC>){
        repo.clearKeys()
        for (pair in keyDC){
            Log.d("adding", "adding")
            if (pair.secretKey != ""){
                addKeySecretPair(pair)
            }
            Log.d("adding", "added ${pair.serviceName}")
        }
    }
}