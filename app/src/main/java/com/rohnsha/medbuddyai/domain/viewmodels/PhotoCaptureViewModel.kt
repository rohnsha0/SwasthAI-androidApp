package com.rohnsha.medbuddyai.domain.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rohnsha.medbuddyai.ContextUtill
import com.rohnsha.medbuddyai.database.userdata.disease.diseaseDBviewModel
import com.rohnsha.medbuddyai.domain.dataclass.classification
import com.rohnsha.medbuddyai.domain.dataclass.disease_data_dataClass
import com.rohnsha.medbuddyai.domain.dataclass.disease_version
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class photoCaptureViewModel: ViewModel() {

    val viewModelClassifier: classificationVM=ViewModelProvider.AndroidViewModelFactory
        .getInstance(ContextUtill.ContextUtils.getApplicationContext() as Application)
        .create(
            classificationVM::class.java
        )

    val viewModelDiseaseDB= ViewModelProvider.AndroidViewModelFactory
        .getInstance(ContextUtill.ContextUtils.getApplicationContext() as Application)
        .create(
            diseaseDBviewModel::class.java
        )

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    val bitmaps = _bitmaps.asStateFlow()

    private val _listPrediction = MutableStateFlow<List<classification>>(emptyList())
    val unsortedClassification= _listPrediction.asStateFlow()

    private val _classificationData = MutableStateFlow(disease_data_dataClass())
    val classificationData = _classificationData.asStateFlow()

    private val _maxIndex= MutableStateFlow(
        classification(confident = 0f, indexNumber = 404, parentIndex = 404)
    )
    val maxIndex= _maxIndex.asStateFlow()

    private val _notMaxElements= MutableStateFlow<List<classification>>(emptyList())
    val minIndex= _notMaxElements.asStateFlow()

    private val _loadingBoolean= MutableStateFlow(true)
    val isLoadingBoolean= _loadingBoolean.asStateFlow()

    private val _reloadingBoolean= MutableStateFlow(false)
    val isReLoadingBoolean= _reloadingBoolean.asStateFlow()

    private val _normalBoolean = MutableStateFlow(false)
    val isNormalBoolean = _normalBoolean.asStateFlow()

    private val _erroredBoolean = MutableStateFlow(false)
    val isErroredBoolean = _erroredBoolean.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value = bitmap
    }
    fun resetReloadBoolean(){
        _reloadingBoolean.value= true
    }

    suspend fun onClassify(context: Context, index: Int){
        _listPrediction.value= emptyList()
        _normalBoolean.value=false
        _reloadingBoolean.value= true
        val rawClassification= try {
            viewModelClassifier.classify(context, _bitmaps.value!!, index)
        } catch (e: Exception){
            Log.d("successIndex", e.printStackTrace().toString())
            emptyList()
        }
        if (rawClassification.isEmpty()){
            _erroredBoolean.value=true
            _loadingBoolean.value=false
            _reloadingBoolean.value=false
        } else {
            _listPrediction.value= rawClassification.filter { it.indexNumber == 1 }
            if (_listPrediction.value.isEmpty()){
                _normalBoolean.value=true
                _loadingBoolean.value=false
                _reloadingBoolean.value=false
            } else {
                classificationData(group_number = index)
            }
        }
        Log.d("successIndexVMNormal", _normalBoolean.value.toString())
        Log.d("successIndexVMErrored", _erroredBoolean.value.toString())
        Log.d("successIndexVMList", _listPrediction.value.toString())
    }

    private suspend fun classificationData(group_number: Int){
        try {
            val data= mutableStateOf(disease_data_dataClass())
            sortClassifiedList()

            data.value= viewModelDiseaseDB.searchDB(
                domain = group_number.toString(),
                indexItem = _maxIndex.value.parentIndex.toString()
            )

            Log.d("maxIndex", _maxIndex.value.toString())
            if (data.value.disease_name != "") {
                _classificationData.value= data.value
                _loadingBoolean.value=false
                _reloadingBoolean.value=false
            } else {
                val collectionName= when(group_number){
                    0 -> "lung"
                    1 -> "brain"
                    else -> "null"
                }
                val dataInstance= Firebase.firestore.collection(collectionName)
                val querySnapshot=dataInstance.document(_maxIndex.value.parentIndex.toString()).get().await()
                data.value= querySnapshot.toObject<disease_data_dataClass>()!!
                Log.d("classificationDataInit", querySnapshot.data.toString())
            }
        } catch (e: Exception){
            Log.e("classificationError", e.message.toString())
            _erroredBoolean.value= true
            _loadingBoolean.value=false
            _reloadingBoolean.value=false
        }
    }

    private fun sortClassifiedList(){
        val maxValue= _listPrediction.value.maxByOrNull { it.confident }?.confident ?: 0
        val maximumElement= mutableListOf<classification>()
        val notMaxElements= mutableListOf<classification>()

        Log.d("classificationData", _listPrediction.value.toString())
        for (data in _listPrediction.value){
            if (data.confident==maxValue) _maxIndex.value= (data) else notMaxElements.add(data)
        }
        _notMaxElements.value= notMaxElements
        Log.d("classificationDataHighest", _maxIndex.value.toString())
        Log.d("classificationDataSorted", _notMaxElements.value.toString())
    }

    fun getDiseaseVersionData(
        group_number: Int,
        isMaxIndex: Boolean
    ): List<disease_version> {
        val reqList: MutableList<disease_version> = mutableListOf()
        val lungs= listOf(
            disease_version("Pneumonia", "V2023.04.30", modelAccuracy = 97.70),
            disease_version("Tuberculosis", "V2023.11.14", modelAccuracy = 97.60),
        )
        if (isMaxIndex){
            when(group_number){
                0 -> {
                    reqList.add(
                        disease_version(
                        lungs[_maxIndex.value.parentIndex!!].disease_name, lungs[_maxIndex.value.parentIndex!!].version,
                            _maxIndex.value.confident, modelAccuracy = lungs[_maxIndex.value.parentIndex!!].modelAccuracy
                    ))
                }
            }
            return reqList
        }
        _notMaxElements.value.forEach { classification ->
            when(group_number){
                0-> {
                    reqList.add(disease_version(
                        lungs[classification.parentIndex!!].disease_name, lungs[classification.parentIndex].version, classification.confident
                    ))
                }
            }
        }
        return reqList
    }
}