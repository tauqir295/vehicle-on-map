package com.vehicles.map.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val coordinate: Coordinate,
    val fleetType: String,
    val heading: Double,
    val id: Int
) : Parcelable