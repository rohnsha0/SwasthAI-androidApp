package com.rohnsha.medbuddyai.api.chatbot

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object chatbot_obj {

    private const val MAX_RETRIES = 3

    private val retryInterceptor = Interceptor { chain ->
        var request = chain.request()
        var responseCount = 0
        var response = chain.proceed(request)

        while (!response.isSuccessful && responseCount < MAX_RETRIES) {
            if (responseCount > 0) {
                Thread.sleep(1000L) // Wait for 1 second before retrying
            }
            responseCount++
            request = request.newBuilder().build()
            response = chain.proceed(request)
        }

        response
    }

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(retryInterceptor)
        .build()

    val retrofit=Retrofit
        .Builder()
        .baseUrl("https://api-jjtysweprq-el.a.run.app/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val chatService = retrofit.create(chatbot_interface::class.java)
}