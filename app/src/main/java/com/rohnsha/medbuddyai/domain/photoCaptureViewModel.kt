package com.rohnsha.medbuddyai.domain

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class photoCaptureViewModel: ViewModel() {

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    val bitmaps = _bitmaps.asStateFlow()

    private val _listPrediction = MutableStateFlow<List<classification>>(emptyList())
    val classiedList = _listPrediction.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value = bitmap
    }

    fun onClassify(context: Context, index: Int){
        viewModelScope.launch {
            _listPrediction.value= emptyList()
            _listPrediction.value= try {
                classifier.classifyIndex(context, _bitmaps.value!!, index)
            } catch (e: Exception){
                Log.d("successIndex", e.printStackTrace().toString())
                emptyList()
            }
        }
    }
}