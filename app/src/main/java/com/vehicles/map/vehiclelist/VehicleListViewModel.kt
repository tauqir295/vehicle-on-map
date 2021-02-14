package com.vehicles.map.vehiclelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vehicles.map.data.VehicleList
import com.vehicles.map.network.datasource.AppDataSource
import com.vehicles.map.utils.Resource
import com.vehicles.map.utils.VehicleNetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * Custom ViewModel class
 */
@HiltViewModel
class VehicleListViewModel @Inject constructor(
    private val dataSource: AppDataSource,
    private val networkHelper: VehicleNetworkHelper
) : ViewModel() {

    private val _vehicles = MutableLiveData<Resource<VehicleList>>()
    val vehicleList: LiveData<Resource<VehicleList>>
        get() = _vehicles

    init {
        //initiating the API calls
        fetchVehicleList()
    }

    /**
     * Fetch the data from API
     * @param - [latitude1] - variable used for lat1
     * @param - [longitude1] - variable used for long2
     * @param - [latitude2] - variable used for lat1
     * @param - [longitude2] - variable used for long2
     */
    fun fetchVehicleList(
        latitude1: String = "53.694865",
        longitude1: String = "9.757589",
        latitude2: String = "53.394655",
        longitude2: String = "10.099891"
    ) {
        viewModelScope.launch {
            _vehicles.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    dataSource.getVehicles(latitude1, longitude1, latitude2, longitude2).let {
                        if (it.isSuccessful) {
                            _vehicles.postValue(Resource.success(it.body()))
                        } else _vehicles.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                } catch (e: IOException) {
                    _vehicles.postValue(Resource.error("Something went wrong.", null))
                }

            } else _vehicles.postValue(Resource.error("Something went wrong.", null))
        }
    }

    /**
     * clear data in view model
     */
    fun clearViewModelData() {
        _vehicles.value = null
    }

}