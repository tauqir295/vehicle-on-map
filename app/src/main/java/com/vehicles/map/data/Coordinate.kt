package com.vehicles.map.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coordinate(
    val latitude: Double,
    val longitude: Double
): Parcelable