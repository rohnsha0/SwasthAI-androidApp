package com.rohnsha.medbuddyai.domain.modules

import android.util.Log
import com.rohnsha.medbuddyai.api.results_interface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://45halapf2lg7zd42f33g6da7ci0kbjzo.lambda-url.ap-south-1.on.aws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideResultsAPIService(retrofit: Retrofit): results_interface {
        Log.e("DaggerImpl", "Working")
        return retrofit.create(results_interface::class.java)
    }

}