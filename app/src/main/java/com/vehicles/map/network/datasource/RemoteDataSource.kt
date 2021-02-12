package com.vehicles.map.network.datasource

import com.vehicles.map.data.VehicleList
import com.vehicles.map.network.ApiInterface
import retrofit2.Response

/**
 * Interface implementation for AppDataSource
 * @param - [ApiInterface] - class is used for fetching data from server
 */
class RemoteDataSource(private val apiInterface: ApiInterface) : AppDataSource {

    override suspend fun getVehicles(
        latitude1: String,
        longitude1: String,
        latitude2: String,
        longitude2: String
    ): Response<VehicleList> =
        apiInterface.getVehicles(latitude1, longitude1, latitude2, longitude2)
}