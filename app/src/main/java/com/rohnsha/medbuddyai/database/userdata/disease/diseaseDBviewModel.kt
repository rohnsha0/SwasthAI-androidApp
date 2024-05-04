package com.rohnsha.medbuddyai.database.userdata.disease

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rohnsha.medbuddyai.ContextUtill
import com.rohnsha.medbuddyai.database.userdata.scan_history.scanHistory
import com.rohnsha.medbuddyai.database.userdata.userDataDB
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import com.rohnsha.medbuddyai.domain.notifications.dbUpdateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class diseaseDBviewModel(application: Application): AndroidViewModel(application) {

    private val diseaseRepo: diseaseRepo
    private val diseaseDAO: diseaseDAO

    private val _dataList= MutableStateFlow<List<disease_data_dataClass>>(emptyList())
    val notificanService= dbUpdateService(ContextUtill.ContextUtils.getApplicationContext())

    private val _processUpdatingDB= MutableStateFlow(false)
    val updatingDiseaseDB= _processUpdatingDB.asStateFlow()

    private val _loadingBoolean= MutableStateFlow(true)
    val isLoadingBoolean= _loadingBoolean.asStateFlow()

    private val _erroredBoolean = MutableStateFlow(false)
    val isErroredBoolean = _erroredBoolean.asStateFlow()

    private val _dataLoaded= MutableStateFlow(disease_data_dataClass())
    val data= _dataLoaded.asStateFlow()

    private val _dataCached= MutableStateFlow(scanHistory(0L, "", "", 0f))
    private val _dataCachedReadOnly= MutableStateFlow(disease_data_dataClass())

    init {
        diseaseDAO= userDataDB.getUserDBRefence(application).diseaseDAO()
        diseaseRepo= diseaseRepo(diseaseDAO)
    }

    suspend fun searchDB(domain: String, indexItem: String): disease_data_dataClass {
        return diseaseRepo.searchDB(domain, indexItem)
    }

    fun inputNameToSearch(dataCached: scanHistory, onCompleteLambda: () -> Unit){
        _dataCached.value= dataCached
        onCompleteLambda()
    }

    fun saveData(data: disease_data_dataClass, onCompleteLambda: () -> Unit){
        _dataCachedReadOnly.value= data
        onCompleteLambda()
    }

    fun retrieveData(){
        if (_dataCachedReadOnly.value.disease_name!=""){
            _dataLoaded.value= _dataCachedReadOnly.value
            _loadingBoolean.value= false
        }
    }

    suspend fun searchByName(){
        if (_dataCached.value.title!=""){
            _dataLoaded.value=diseaseRepo.searchDBbyName(_dataCached.value.title)
            _loadingBoolean.value= false
        }

        if (_dataLoaded.value.disease_name==""){
            _erroredBoolean.value= true
            _loadingBoolean.value= false
        }
    }

    suspend fun searchByDomain(domain: String): List<disease_data_dataClass>{
        return withContext(viewModelScope.coroutineContext){
            diseaseRepo.searchDBbyDomain(domain)
        }
    }

    suspend fun readDB(): List<disease_data_dataClass>{
        return withContext(viewModelScope.coroutineContext){
            diseaseRepo.readDB()
        }
    }

    suspend fun fetchUpdatedDB(versionName: String, context: Context){
        _processUpdatingDB.value= true
        val sharedPreferences= context.getSharedPreferences("packageVersionName", Context.MODE_PRIVATE)
        val versionNameSF= sharedPreferences.getString("packageVersionName", "null")

        if (versionName!=versionNameSF){
            try {
                addDiseaseData {
                    sharedPreferences.edit().putString("packageVersionName", versionName).apply()
                    notificanService.showNotification(
                        title = "Database Updated Successfully",
                        message = "If you want to update it again then head to preferences"
                    )
                    _processUpdatingDB.value= false
                }
            } catch (e: Exception){
                Log.d("dbStatus", "error occured: ${e.message}")
                _processUpdatingDB.value= false
            }
        } else {
            _processUpdatingDB.value= false
            Log.d("dbStatus", "same version name")
        }
    }

    private suspend fun addDiseaseData(
        onCompleteLambda: () -> Unit
    ){
        Log.d("dbStatus", "entering fetchDIsease()")
        fetchDiseaseData()
        Log.d("dbStatus", "done fetchDIsease()")
        if (_dataList.value.isNotEmpty()){
            for (data in _dataList.value){
                Log.d("dbStatus", "attempting to add entries")
                diseaseRepo.addDisease(data)
                Log.d("dbStatus", "entries added succesfully")
                onCompleteLambda()
            }
        } else {
            Log.d("dbStatus", "entries found")
        }
    }

    private suspend fun fetchDiseaseData(){
        val list= mutableListOf<disease_data_dataClass>()
        try {
            val dataInstance= Firebase.firestore.collection("diseaseData")
            for (data in dataInstance.get().await().documents.map { documentSnapshot -> documentSnapshot.data }){
                Log.d("dbStatus", data.toString())
                list.add(
                    disease_data_dataClass(
                        symptoms = data?.get("symptoms") as String,
                        thresholds = data["thresholds"] as String,
                        domain = data["domain"] as String,
                        diseaseIndex = data["diseaseIndex"] as String,
                        cover_link = data["cover_link"] as String,
                        disease_name = data["disease_name"] as String,
                        cure = data["cure"] as String,
                        cure_cycle = data["cure_cycle"] as String,
                        introduction = data["introduction"] as String
                    )
                )
            }
            Log.d("dbStatus", "list: $list")
            if (list.isEmpty()){
                notificanService.showNotification(
                    "Database Update Failed",
                    "A database update was triggered but failed unintentionally. We will try again later!"
                )
            }
            _dataList.value= list
        } catch (e: Exception){
            Log.d("dbStatus", e.toString())
            notificanService.showNotification(
                "error occured",
                "tap to know more"
            )
        }
    }
}