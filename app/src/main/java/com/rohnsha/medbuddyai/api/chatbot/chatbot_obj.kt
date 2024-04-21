package com.rohnsha.medbuddyai.api.chatbot

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object chatbot_obj {

    val retrofit=Retrofit.Builder()
        .baseUrl("https://api-jjtysweprq-el.a.run.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val chatService = retrofit.create(chatbot_interface::class.java)

}