package com.rohnsha.medbuddyai.domain.modules

import android.util.Log
import com.rohnsha.medbuddyai.api.results_interface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://zmlj5pmmhqmkgnksb23omr5hmy0nprsa.lambda-url.ap-south-1.on.aws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideResultsAPIService(retrofit: Retrofit, @Named("string2") str1: String): results_interface {
        Log.e("DaggerImpl", "Working, $str1")
        return retrofit.create(results_interface::class.java)
    }

    @Singleton
    @Provides
    @Named("string1")
    fun string1(): String{
        return "string1"
    }

    @Singleton
    @Provides
    @Named("string2")
    fun string2(): String{
        return "string2"
    }

}