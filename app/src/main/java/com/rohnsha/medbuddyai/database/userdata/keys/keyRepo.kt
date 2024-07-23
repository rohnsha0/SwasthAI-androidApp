package com.rohnsha.medbuddyai.database.userdata.keys

class keyRepo(private val keyDAO: keyDAO) {

    suspend fun insertKeyPair(keyDC: keyDC){
        keyDAO.insertSecretKey(keyDC)
    }

    suspend fun querySecrets(serviceName: String): String{
        return keyDAO.getSecretKey(serviceName)
    }

    suspend fun queryKeyPairs(): List<keyDC>{
        return keyDAO.querySecrets()
    }

}