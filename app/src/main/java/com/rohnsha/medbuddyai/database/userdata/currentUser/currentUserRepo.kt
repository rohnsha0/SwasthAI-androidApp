package com.rohnsha.medbuddyai.database.userdata.currentUser

class currentUserRepo(private val currentUser: currentUserDAO) {

    suspend fun addUserData(userData: fieldValueDC){
        currentUser.insert(userData)
    }

    suspend fun searchQuery(isDefaultUser: Boolean): fieldValueDC {
        return currentUser.getFieldData(isDefaultUser)
    }

    suspend fun getAllUsers(): List<fieldValueDC>{
        return currentUser.getAllUsers()
    }

}