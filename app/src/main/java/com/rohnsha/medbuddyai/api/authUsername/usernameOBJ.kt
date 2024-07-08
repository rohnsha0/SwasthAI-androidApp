package com.rohnsha.medbuddyai.api.authUsername

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object usernameOBJ {

    val retrofit= Retrofit
        .Builder()
        .baseUrl("https://api-jjtysweprq-el.a.run.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val usernameCheckService = retrofit.create(usernameINTERFACE::class.java)

}