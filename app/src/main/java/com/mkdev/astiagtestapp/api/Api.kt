package com.mkdev.astiagtestapp.api

import com.mkdev.astiagtestapp.models.LocationModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("reverse")
    fun getCurrentLocationData(@Query("format") format: String,
                               @Query("lat") lat: Double,
                               @Query("lon") lon: Double): Single<LocationModel?>
}