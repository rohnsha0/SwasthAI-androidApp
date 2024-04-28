package com.rohnsha.medbuddyai.domain.dataclass

data class Post(
    val id: String? = null,
    val title: String? = null,
    val author: String? = null,
    val content: String? = null,
    val timestamp: String? = null,
    val domain: String? = null,
)

data class Reply(
    val id: String? = null,
    val author: String? = null,
    val content: String? = null,
    val timestamp: String? = null
)

data class postWithReply(
    val post: Post,
    val replies: List<Reply>
)
