package com.rohnsha.medbuddyai.domain.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class userAuthVM: ViewModel() {

    private lateinit var _auth: FirebaseAuth

    fun initialize(instance: FirebaseAuth){
        _auth = instance
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
                onSuccess()
            }
    }

    fun registerUser(password: String, email: String, onSucess: () -> Unit, name: String) {
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSucess()
            }
    }

}