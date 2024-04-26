package com.rohnsha.medbuddyai.domain.dataclass

data class messageDC(
    val message: String,
    val isBotMessage: Boolean,
    val timestamp: Long,
    val isError: Boolean= false
)
