package com.rohnsha.medbuddyai.database.userdata.scan_history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rohnsha.medbuddyai.database.userdata.userDataDB
import kotlinx.coroutines.launch

class scanHistoryViewModel(application: Application): AndroidViewModel(application) {

    private val scanHistoryRepo: scanHistoryRepo

    init {
        val scanHistoryDAO= userDataDB.getUserDBRefence(application).scanDAO()
        scanHistoryRepo= scanHistoryRepo(scanHistoryDAO)
    }

    suspend fun addScanHistory(scanHistory: scanHistory){
        viewModelScope.launch {
            scanHistoryRepo.addScanHistory(scanHistory)
        }
    }

}