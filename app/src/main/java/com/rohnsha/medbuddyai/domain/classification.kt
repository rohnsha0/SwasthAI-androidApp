package com.rohnsha.medbuddyai.domain

data class classification(
    val indexNumber: Int,
    val confident: Float,
    val parentIndex: Int?=null
)