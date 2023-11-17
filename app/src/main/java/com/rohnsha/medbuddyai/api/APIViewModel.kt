package com.rohnsha.medbuddyai.api

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class APIViewModel @Inject constructor(
    private val resultsAPIService: results_interface
) : ViewModel() {

    suspend fun getDiseaseResults(dynamicURL: String): disease_data_dataClass {
        return resultsAPIService.getDiseaseData(dynamicURL)
    }
}