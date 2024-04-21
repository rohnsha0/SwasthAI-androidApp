package com.rohnsha.medbuddyai.api.chatbot

import retrofit2.http.GET
import retrofit2.http.Url

interface chatbot_interface {

    @GET
    suspend fun getChatReply(@Url url: String): chatbot_dc

}