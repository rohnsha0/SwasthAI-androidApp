package com.rohnsha.dermbuddyai.api

import retrofit2.http.GET
import retrofit2.http.Url

interface results_interface {

    @GET
    suspend fun getDiseaseData(@Url url: String): disease_data_dataClass

}