package com.rohnsha.medbuddyai.domain.viewmodels

import android.annotation.SuppressLint
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
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

class communityVM: ViewModel() {

    private val _auth= FirebaseAuth.getInstance()
    private val _database= Firebase.database
    private val _postRef= _database.reference
    private val _postFeed= MutableStateFlow<List<Post>>(emptyList())

    val feedContents= _postFeed.asStateFlow()

    fun post(
        title: String,
        content: String,
        domainIndex: Int,
        onCompleteLambda: () -> Unit
    ){
        viewModelScope.launch {
            Log.d("authUserAction", "post invoked")
            if (_auth.currentUser!=null){
                val username= _auth.currentUser!!.email!!.substringBefore("@")
                Log.d("authUsername", username)
                val newPost= Post(
                    author = username,
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis().toString(),
                    domain = returnDomain(domainIndex)
                )
                Log.d("authUserAction", newPost.toString())
                _postRef.child("posts").child(username).child(title).setValue(newPost)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("authUserAction", "Post successful")
                            onCompleteLambda()
                        } else {
                            Log.e("authUserAction", "Post unsuccessful", task.exception)
                        }
                    }
            }
        }
    }

    fun getFeed(){
        if (_auth.currentUser!=null){
            viewModelScope.launch {
                val posts = mutableListOf<Post>()
                _postRef.get()
                    .addOnSuccessListener {
                        Log.d("dataSnap", it.toString())
                        for (childSnapshot in it.child("posts").children) {
                            val userKey = childSnapshot.key
                            for (postSnapshot in childSnapshot.children) {
                                val postData = postSnapshot.getValue<Map<String, String>>()

                                val author = postData?.get("author") ?: "Unknown"
                                val title = postData?.get("title") ?: "No title"
                                val content = postData?.get("content") ?: "No content"
                                val timestamp= postData?.get("timestamp") ?: "Unnoticed"
                                val domain = postData?.get("domain") ?: "Unspecified"

                                val post = Post(
                                    author = author,
                                    title = title,
                                    content = content,
                                    timestamp = timestamp,
                                    domain = domain
                                )
                                posts.add(post)
                            }
                        }
                        Log.d("dataSnap", "data: $posts")
                        _postFeed.value= posts
                    }
            }
        }
    }

    fun returnDomain(
        index: Int
    ): String{
        return when(index){
            1 -> "Lungs"
            2 -> "Brain"
            else -> ""
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun calculateTimeDifference(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val differenceInMillis = currentTime - timestamp

        val seconds = TimeUnit.MILLISECONDS.toSeconds(differenceInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(differenceInMillis)

        return when {
            seconds < 60 -> "$seconds seconds ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            else -> {
                val instant = Instant.ofEpochMilli(timestamp)
                val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
                formatter.format(dateTime)
            }
        }
    }

    fun loginUser(){
        viewModelScope.launch {
            try {
                if (_auth.currentUser==null){
                    _auth.signInWithEmailAndPassword("test@test.com", "test123456789").await()
                    Log.d("auth", "auth unsuccessfull")
                } else{
                    Log.d("auth", "auth successfull")
                }
            } catch (_: Exception){

            }
        }
    }

}