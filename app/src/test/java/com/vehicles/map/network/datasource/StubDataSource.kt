package com.vehicles.map.network.datasource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vehicles.map.data.VehicleList
import com.vehicles.map.network.ApiInterface
import com.vehicles.map.utils.Logger
import retrofit2.Response

/**
 * Interface implementation for AppDataSource
 * @param - [ApiInterface] - class is used for reading data from mock file present debug/resources folder
 */
class StubDataSource : AppDataSource {

    override suspend fun getVehicles(
        latitude1: String,
        longitude1: String,
        latitude2: String,
        longitude2: String
    ): Response<VehicleList> {
        val classLoader = Thread.currentThread().contextClassLoader
        classLoader?.getResourceAsStream("mock.json").use {
            val rawData = it?.reader()?.readText()
            val data = Gson().fromJson(rawData, VehicleList::class.java)
            return Response.success(data)
        }
    }
}