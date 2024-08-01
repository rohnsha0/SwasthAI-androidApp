package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.rohnsha.medbuddyai.api.authUsername.usernameOBJ.usernameCheckService
import com.rohnsha.medbuddyai.database.userdata.currentUser.currentUserDataVM
import com.rohnsha.medbuddyai.database.userdata.currentUser.fieldValueDC
import com.rohnsha.medbuddyai.database.userdata.keys.keyDC
import com.rohnsha.medbuddyai.database.userdata.keys.keyVM
import com.rohnsha.medbuddyai.domain.dataclass.userInfoDC
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class userAuthVM: ViewModel() {

    private lateinit var _auth: FirebaseAuth
    private lateinit var _firestoreRef: DatabaseReference
    private lateinit var _currentUserVM: currentUserDataVM
    private lateinit var _keyVM: keyVM

    fun initialize(
        instance: FirebaseAuth,
        dbReference: DatabaseReference,
        currentUserVM: currentUserDataVM,
        keyVM: keyVM
    ){
        _auth = instance
        _firestoreRef = dbReference
        _currentUserVM= currentUserVM
        _keyVM= keyVM
    }

    suspend fun isUsernameValid(username: String): Boolean {
        if (username==""){
            return false
        }

        val dynamicURL= "https://api-jjtysweprq-el.a.run.app/getUsername/$username"
        return withContext(viewModelScope.coroutineContext){
            usernameCheckService.getUsernameDetails(dynamicURL).username!=null
        }
    }

    fun isUserUnAuthenticated(): Boolean {
        if(_auth.currentUser == null){
            return true
        }
        return false
    }

    fun loginUser(
        password: String,
        email: String,
        onSuccess: () -> Unit,
        snackBarToggleVM: snackBarToggleVM
        ) {
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
                            _keyVM.updateKeySecretPair(
                                listOf(keyDC(serviceName = "swasthai", secretKey = userInfo.username))
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
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                Log.d("loginError", exception.message.toString())
                snackBarToggleVM.SendToast(
                    message = exception.message.toString(),
                    indicator_color = Color.Red,
                    icon = Icons.Outlined.Warning,
                )
            }
    }

    suspend fun registerUser(
        password: String,
        email: String,
        onSucess: () -> Unit,
        fname: String,
        lname: String,
        username: String,
        snackBarToggleVM: snackBarToggleVM
    ) {
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
                            _keyVM.updateKeySecretPair(
                                listOf(keyDC(serviceName = "swasthai", secretKey = username))
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
                snackBarToggleVM.SendToast(
                    message = it.message.toString(),
                    indicator_color = Color.Red,
                    icon = Icons.Outlined.Warning,
                )
            }
    }
}