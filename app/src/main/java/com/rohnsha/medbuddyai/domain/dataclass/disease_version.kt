package com.rohnsha.medbuddyai.domain.dataclass

data class disease_version(
    val disease_name: String,
    val version: String,
    val confidence: Float=0f
)