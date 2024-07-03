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
                        Log.d("loginSuccess", it.toString())
                        viewModelScope.launch {
                            val userInfo= fieldValueDC(
                                username = it.child("username").value.toString(),
                                fname = it.child("firstName").value.toString(),
                                lname = it.child("lastName").value.toString(),
                                isDefaultUser = true
                            )
                            Log.d("loginSuccess", userInfo.toString())
                            _currentUserVM.addDataCurrentUser(
                                userInfo
                            )
                            onSuccess()
                        }
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        Log.d("loginError", it.printStackTrace().toString())
                        Log.d("loginError", it.message.toString())
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
                                fieldValueDC(
                                    username = username,
                                    fname = fname,
                                    lname = lname,
                                    isDefaultUser = true
                                )
                            )
                        }
                        onSucess()
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        Log.d("loginError", it.printStackTrace().toString())
                        Log.d("loginError", it.message.toString())
                    }
            }
            .addOnFailureListener {
                it.printStackTrace()
                Log.d("loginError", it.printStackTrace().toString())
                Log.d("loginError", it.message.toString())
            }
    }
}