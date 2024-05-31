package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.rohnsha.medbuddyai.database.userdata.currentUser.currentUserDataVM
import com.rohnsha.medbuddyai.database.userdata.currentUser.fieldValueDC
import com.rohnsha.medbuddyai.domain.dataclass.userInfoDC
import kotlinx.coroutines.launch

class userAuthVM: ViewModel() {

    private lateinit var _auth: FirebaseAuth
    private lateinit var _firestoreRef: DatabaseReference
    private lateinit var _currentUserVM: currentUserDataVM

    fun initialize(
        instance: FirebaseAuth,
        dbReference: DatabaseReference,
        currentUserVM: currentUserDataVM,
    ){
        _auth = instance
        _firestoreRef = dbReference
        _currentUserVM= currentUserVM
    }

    fun isUserUnAuthenticated(): Boolean {
        if(_auth.currentUser == null){
            return true
        }
        return false
    }

    fun loginUser(password: String, email: String, onSuccess: () -> Unit) {
        _auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _firestoreRef.child("users").child(it.user?.uid.toString()).get()
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            for(values in it.children){
                                _currentUserVM.addDataCurrentUser(
                                    fieldValueDC(field = values.key.toString(), value = values.value.toString())
                                )
                            }
                            onSuccess()
                        }
                    }
            }
    }

    suspend fun registerUser(password: String, email: String, onSucess: () -> Unit, fname: String, lname: String, username: String) {
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userInfo= userInfoDC(
                    firstName = fname,
                    lastName = lname,
                    username = username
                )
                _firestoreRef.child("users").child(it.user?.uid.toString()).setValue(userInfo)
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            _currentUserVM.addDataCurrentUser(
                                fieldValueDC(field = "firstName", value = fname)
                            )
                            _currentUserVM.addDataCurrentUser(
                                fieldValueDC(field = "lastName", value = lname)
                            )
                            _currentUserVM.addDataCurrentUser(
                                fieldValueDC(field = "username", value = username)
                            )
                        }
                        onSucess()
                    }
            }
            .addOnFailureListener {
                it.printStackTrace()
                Log.d("loginError", it.printStackTrace().toString())
                Log.d("loginError", it.message.toString())
            }
    }
}