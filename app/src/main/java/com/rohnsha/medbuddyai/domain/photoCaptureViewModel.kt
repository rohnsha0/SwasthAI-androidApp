package com.rohnsha.medbuddyai.domain

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rohnsha.medbuddyai.api.disease_data_dataClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class photoCaptureViewModel: ViewModel() {

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    val bitmaps = _bitmaps.asStateFlow()

    private val _listPrediction = MutableStateFlow<List<classification>>(emptyList())
    private val _classificationData = MutableStateFlow<List<disease_data_dataClass>>(emptyList())
    val classificationData = _classificationData.asStateFlow()
    private val _maxIndex= MutableStateFlow(
        classification(confident = 0f, indexNumber = 404, parentIndex = 404)
    )
    private val _notMaxElements= MutableStateFlow<List<classification>>(emptyList())

    private val _normalBoolean = MutableStateFlow(false)
    val isNormalBoolean = _normalBoolean.asStateFlow()

    private val _erroredBoolean = MutableStateFlow(false)
    val isErroredBoolean = _erroredBoolean.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value = bitmap
    }

    suspend fun onClassify(context: Context, index: Int){
        _listPrediction.value= emptyList()
        _normalBoolean.value=false
        val rawClassification= try {
            classifier.classifyIndex(context, _bitmaps.value!!, index)
        } catch (e: Exception){
            Log.d("successIndex", e.printStackTrace().toString())
            emptyList()
        }
        if (rawClassification.isEmpty()){
            _erroredBoolean.value=true
        } else {
            _listPrediction.value= rawClassification.filter { it.indexNumber == 1 }
            if (_listPrediction.value.isEmpty()){
                _normalBoolean.value=true
            } else {
                classificationData(group_number = index)
            }
        }
        Log.d("successIndexVMNormal", _normalBoolean.value.toString())
        Log.d("successIndexVMErrored", _erroredBoolean.value.toString())
        Log.d("successIndexVMList", _listPrediction.value.toString())
    }

    private suspend fun classificationData(group_number: Int){
        val collectionName= when(group_number){
            0 -> "lung"
            else -> "null"
        }
        val dataInstance= Firebase.firestore.collection(collectionName)
        try {
            sortClassifiedList()
            val listRetrieved= mutableListOf<disease_data_dataClass>()
            val querySnapshot=dataInstance.document(_maxIndex.value.parentIndex.toString()).get().await()
            val data=querySnapshot.toObject<disease_data_dataClass>()
            Log.d("classificationDataInit", querySnapshot.data.toString())
            if (data != null) {
                listRetrieved.add(data)
            }
            _classificationData.value=listRetrieved
        } catch (e: Exception){
            Log.e("classificationError", e.message.toString())
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
}