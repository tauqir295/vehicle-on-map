package com.vehicles.map.network

import com.vehicles.map.data.VehicleList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * retrofit service interface for defining the api call methods
 */
interface ApiInterface {
    @GET("/")
    suspend fun getVehicles(
        @Query("p1Lat") latitude1: String,
        @Query("p1Lon") longitude1: String,
        @Query("p2Lat") latitude2: String,
        @Query("p2Lon") longitude2: String
    ): Response<VehicleList>
}