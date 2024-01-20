package com.rohnsha.medbuddyai.database.userdata.scan_history

class scanHistoryRepo(private val scanHistoryDAO: scanHistoryDAO) {

    suspend fun addScanHistory(scanHistory: scanHistory){
        scanHistoryDAO.addScanHistory(scanHistory)
    }

}