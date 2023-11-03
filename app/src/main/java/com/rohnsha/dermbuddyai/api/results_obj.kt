package com.rohnsha.dermbuddyai.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object results_obj {

    val retrofitResultAPI= Retrofit.Builder()
        .baseUrl("https://45halapf2lg7zd42f33g6da7ci0kbjzo.lambda-url.ap-south-1.on.aws/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val resultsAPIService= retrofitResultAPI.create(results_interface::class.java)

}