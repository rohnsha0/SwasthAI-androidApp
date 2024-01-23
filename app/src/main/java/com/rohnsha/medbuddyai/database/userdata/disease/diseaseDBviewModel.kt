package com.rohnsha.medbuddyai.database.userdata.disease

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rohnsha.medbuddyai.database.userdata.userDataDB
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class diseaseDBviewModel(application: Application): AndroidViewModel(application) {

    private val diseaseRepo: diseaseRepo
    private val diseaseDAO: diseaseDAO

    private val _dataList= MutableStateFlow<List<disease_data_dataClass>>(emptyList())

    private val _processUpdatingDB= MutableStateFlow(false)
    val updatingDiseaseDB= _processUpdatingDB.asStateFlow()


    init {
        diseaseDAO= userDataDB.getUserDBRefence(application).diseaseDAO()
        diseaseRepo= diseaseRepo(diseaseDAO)
    }

    suspend fun fetchUpdatedDB(versionName: String, context: Context){
        _processUpdatingDB.value= true
        val sharedPreferences= context.getSharedPreferences("packageVersionName", Context.MODE_PRIVATE)
        val versionNameSF= sharedPreferences.getString("packageVersionName", "null")

        if (versionName!=versionNameSF){
            try {
                addDiseaseData {
                    sharedPreferences.edit().putString("packageVersionName", versionName).apply()
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

        fetchDiseaseData()
        if (_dataList.value.isNotEmpty()){
            for (data in _dataList.value){
                Log.d("dbStatus", "attempting to add entries")
                diseaseRepo.addDisease(data)
                Log.d("dbStatus", "entries added succesfully")
                onCompleteLambda()
            }
        } else{
            Log.d("dbStatus", "entries found")
        }
    }

    private suspend fun fetchDiseaseData(){
        val list= mutableListOf<disease_data_dataClass>()
        val dataInstance= Firebase.firestore.collection("lung")
        for (data in dataInstance.get().await().documents.map { documentSnapshot -> documentSnapshot.data }){
            list.add(
                disease_data_dataClass(
                    symptoms = data?.get("symptoms") as String,
                    model_accuracy = data["model_accuracy"] as String,
                    model_version = data["model_version"] as String,
                    thresholds = data["thresholds"] as String,
                    domain = data["domain"] as String,
                    cover_link = data["cover_link"] as String,
                    disease_name = data["disease_name"] as String,
                    cure = data["cure"] as String,
                    cure_cycle = data["cure_cycle"] as String,
                    introduction = data["introduction"] as String
                )
            )
        }
        _dataList.value= list
    }
}