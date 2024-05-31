package com.rohnsha.medbuddyai.database.userdata.currentUser

class currentUserRepo(private val currentUser: currentUserDAO) {

    suspend fun addUserData(userData: fieldValueDC){
        currentUser.insert(userData)
    }

    suspend fun searchQuery(query: String): fieldValueDC {
        return currentUser.getFieldData(query)
    }

}