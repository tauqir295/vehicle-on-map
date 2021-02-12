package com.vehicles.map.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleList(
    val poiList: List<Vehicle>
) : Parcelable