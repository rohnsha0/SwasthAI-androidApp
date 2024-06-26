package com.rohnsha.medbuddyai.domain.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.rohnsha.medbuddyai.domain.dataclass.Post
import com.rohnsha.medbuddyai.domain.dataclass.Reply
import com.rohnsha.medbuddyai.domain.dataclass.postWithReply
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

class communityVM: ViewModel() {

    private lateinit var _auth: FirebaseAuth
    private lateinit var _firestoreRef: DatabaseReference
    private lateinit var _username: String
    private val _postFeed= MutableStateFlow<List<Post>>(emptyList())
    private val _replyFeed= MutableStateFlow<List<Reply>>(emptyList())
    private val _postCount= MutableStateFlow(0L)

    val feedContents= _postFeed.asStateFlow()
    val replyContents= _replyFeed.asStateFlow()

    fun initialize(instance: FirebaseAuth, dbReference: DatabaseReference, username: String){
        _auth= instance
        _firestoreRef= dbReference
        Log.d("loginStatus", _auth.currentUser?.uid ?: "nullified")
        _username= username
    }

    fun addReply(
        replyContent: String,
        postID: String,
        onCompleteLambda: (Reply) -> Unit
    ){
        viewModelScope.launch {
            if (_auth.currentUser!=null){
                _firestoreRef.child("replies").child(_username).child(postID).get()
                    .addOnSuccessListener {
                        val replyCount= it.childrenCount
                        val replyID= "${postID}: ${replyCount+1}"
                        Log.d("authUserActionInner", replyCount.toString())
                        Log.d("authUserAction", replyCount.toString())
                        val reply= Reply(
                            id = replyID,
                            content = replyContent,
                            timestamp = System.currentTimeMillis().toString(),
                            author = _username
                        )

                        _firestoreRef
                            .child("replies")
                            .child(_username)
                            .child(postID)
                            .child(replyID)
                            .setValue(reply)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onCompleteLambda(reply)
                                    Log.d("authUserAction", "Reply successful")
                                } else {
                                    Log.e("authUserAction", "Reply unsuccessful", task.exception)
                                }
                            }
                    }
            }
        }
    }

    fun post(
        content: String,
        onCompleteLambda: () -> Unit
    ){
        viewModelScope.launch {
            Log.d("authUserAction", "post invoked")
            if (_auth.currentUser!=null){
                Log.d("authUsername", _username)
                    _firestoreRef.child("posts").child(_username).get()
                    .addOnSuccessListener {
                        _postCount.value= it.childrenCount+1L
                        Log.d("postCountInner", _postCount.value.toString())
                    }
                Log.d("postCount", _postCount.value.toString())
                val postID= "${_username}: ${_postCount.value}"
                val newPost= Post(
                    id = postID,
                    author = _username,
                    content = content,
                    timestamp = System.currentTimeMillis().toString(),
                )
                Log.d("authUserAction", newPost.toString())
                _firestoreRef.child("posts").child(_username).child(postID).setValue(newPost)
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
                val replies= mutableListOf<Reply>()
                _firestoreRef.get()
                    .addOnSuccessListener {
                        Log.d("dataSnap", it.toString())
                        for (childSnapshot in it.child("posts").children) {
                            for (postSnapshot in childSnapshot.children) {
                                val postData = postSnapshot.getValue<Map<String, String>>()

                                val author = postData?.get("author") ?: "Unknown"
                                val content = postData?.get("content") ?: "No content"
                                val timestamp= postData?.get("timestamp") ?: "Unnoticed"
                                val id= postData?.get("id") ?: "null"

                                val post = Post(
                                    id = id,
                                    author = author,
                                    content = content,
                                    timestamp = timestamp,
                                )
                                posts.add(post)
                            }
                        }
                        for (childSnapshot in it.child("replies").children) {
                            for (postSnapshot in childSnapshot.children) {
                                for (itemm in postSnapshot.children){
                                    val postData = itemm.getValue<Map<String, String>>()
                                    val id = postData?.get("id") ?: "Unknown"
                                    val author = postData?.get("author") ?: "Unknown"
                                    val content = postData?.get("content") ?: "Unknown"
                                    val timestamp = postData?.get("timestamp") ?: "Unknown"

                                    val reply= Reply(
                                        id, author, content, timestamp
                                    )
                                    replies.add(reply)
                                    Log.d("dataSnap", "data: $reply")
                                }
                            }
                        }
                        _postFeed.value=posts
                        _replyFeed.value= replies
                        Log.d("dataSnapReplyPosts", "posts: ${_postFeed.value}\nreplies: ${_replyFeed.value}")
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

    fun mergePostReplies(postData: List<Post> = _postFeed.value, replyData: List<Reply> = _replyFeed.value, postID: String? = null): MutableList<postWithReply> {
        val postsWithReplies = mutableListOf<postWithReply>()
        for (post in postData) {
            val postReplies = if (postID == null) {
                replyData.filter { it.id?.contains(post.id ?: "") ?: false }
            } else {
                replyData.filter { it.id == post.id }
            }
            postsWithReplies.add(postWithReply(post, postReplies))
        }
        return postsWithReplies
    }

}