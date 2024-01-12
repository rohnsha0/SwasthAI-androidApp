package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rohnsha.medbuddyai.domain.dataclass.Post
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class communityVM: ViewModel() {

    private val auth= FirebaseAuth.getInstance()
    private val database= Firebase.database
    private val postRef= database.reference

    fun post(

    ){
        viewModelScope.launch {
            Log.d("authUserAction", "post invoked")
            if (auth.currentUser!=null){
                val username= auth.currentUser!!.email!!.substringBefore("@")
                Log.d("authUsername", username.toString())
                val newPost= Post(
                    author = username,
                    content = "This is a test"
                )
                Log.d("authUserAction", newPost.toString())
                postRef.child("posts").child(username).setValue(newPost)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("authUserAction", "Post successful")
                        } else {
                            Log.e("authUserAction", "Post unsuccessful", task.exception)
                        }
                    }
            }
        }
    }

    fun loginUser(){
        viewModelScope.launch {
            if (auth.currentUser==null){
                auth.signInWithEmailAndPassword("rohnsha0@gmail.com", "test123").await()
                Log.d("auth", "auth unsuccessfull")
            } else{
                Log.d("auth", "auth successfull")
            }
        }
    }

}