package com.rohnsha.medbuddyai.database.userdata.scan_history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rohnsha.medbuddyai.database.userdata.userDataDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class scanHistoryViewModel(application: Application): AndroidViewModel(application) {

    private val scanHistoryRepo: scanHistoryRepo
    private val scanHistoryDAO: scanHistoryDAO
    private var _scanHistoryEntries= MutableStateFlow<List<scanHistory>>(emptyList())
    val scanHistoryEntries= _scanHistoryEntries.asStateFlow()

    init {
        scanHistoryDAO= userDataDB.getUserDBRefence(application).scanDAO()
        scanHistoryRepo= scanHistoryRepo(scanHistoryDAO)
    }

    suspend fun addScanHistory(scanHistory: scanHistory){
        viewModelScope.launch {
            scanHistoryRepo.addScanHistory(scanHistory)
        }
    }

    suspend fun readScanHistory(){
        scanHistoryDAO.readScanHistory().collect {
            Log.d("dataDBA", it.toString())
            _scanHistoryEntries.value=it
        }
    }

}