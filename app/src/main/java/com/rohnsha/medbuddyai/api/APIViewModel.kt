package com.rohnsha.medbuddyai.api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohnsha.medbuddyai.domain.dataclass.doctor
import com.rohnsha.medbuddyai.domain.dataclass.doctors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class APIViewModel @Inject constructor(
    private val resultsAPIService: results_interface
) : ViewModel() {

    private val _doctorsList= MutableStateFlow<List<doctor>>(emptyList())
    val doctorData= _doctorsList.asStateFlow()

    fun fetchInitiateDate(){
        if (_doctorsList.value.isEmpty()){
            viewModelScope.launch {
                val lists= mutableListOf<doctor>()
                try {
                    val dynamicURL= "https://zmlj5pmmhqmkgnksb23omr5hmy0nprsa.lambda-url.ap-south-1.on.aws/gen"
                    val results= getDiseaseResults(dynamicURL)
                    for (data in results.physical){
                        lists.add(doctor(data[0], data[1], data[2]))
                    }
                    _doctorsList.value= lists
                } catch (e: Exception){
                    Log.d("resultsAPI", e.toString())
                }

            }
        }
    }

    suspend fun getDiseaseResults(dynamicURL: String): doctors {
        return withContext(Dispatchers.IO){
            resultsAPIService.getDiseaseData(dynamicURL)
        }
    }
}