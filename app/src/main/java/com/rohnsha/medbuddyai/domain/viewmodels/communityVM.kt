package com.rohnsha.medbuddyai.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rohnsha.medbuddyai.domain.dataclass.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class communityVM: ViewModel() {

    private val _auth= FirebaseAuth.getInstance()
    private val _database= Firebase.database
    private val _postRef= _database.reference
    private val _postFeed= MutableStateFlow<List<Post>>(emptyList())

    val feedContents= _postFeed.asStateFlow()

    fun post(

    ){
        viewModelScope.launch {
            Log.d("authUserAction", "post invoked")
            if (_auth.currentUser!=null){
                val username= _auth.currentUser!!.email!!.substringBefore("@")
                Log.d("authUsername", username)
                val newPost= Post(
                    author = "username",
                    content = "This is a test2"
                )
                Log.d("authUserAction", newPost.toString())
                _postRef.child("posts").child("username").setValue(newPost)
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

    fun readDB(){
        viewModelScope.launch {
            _postRef.child("posts").get()
                .addOnSuccessListener {
                    val posts = it.children.mapNotNull { childSnapshot ->
                        val postData = childSnapshot.getValue<Map<String, String>>()
                        postData?.let {
                            Post(
                                author = it["author"] ?: "Unknown",
                                content = it["content"] ?: "No content",
                                title = it["title"]
                            )
                        }
                    }
                    _postFeed.value = posts
                    Log.d("dataSnap", _postFeed.value.toString())
                }
        }
    }

    fun loginUser(){
        viewModelScope.launch {
            if (_auth.currentUser==null){
                _auth.signInWithEmailAndPassword("rohnsha0@gmail.com", "test123").await()
                Log.d("auth", "auth unsuccessfull")
            } else{
                Log.d("auth", "auth successfull")
            }
        }
    }

}