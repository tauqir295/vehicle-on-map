package com.vehicles.map.network.datasource

import com.vehicles.map.data.VehicleList
import retrofit2.Response

/**
 * Interface for API class
 */
interface AppDataSource {
    suspend fun getVehicles(
        latitude1: String,
        longitude1: String,
        latitude2: String,
        longitude2: String
    ): Response<VehicleList>
}