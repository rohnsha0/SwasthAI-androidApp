package com.rohnsha.medbuddyai.domain.dataclass

data class Post(
    val id: String? = null,
    val title : String? = null,
    val author: String? = null,
    val content: String? = null,
    val timestamp: Long? = null,
    val domain: String? = null,
    val replies: List<Reply>? = null
)

data class Reply(
    val id: String? = null,
    val author: String? = null,
    val content: String? = null,
    val timestamp: Long? = null
)
