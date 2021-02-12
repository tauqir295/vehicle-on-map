package com.vehicles.map.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VehicleNetworkHelperTest {

    @Test
    fun checkWiFi() {
        val networkHelper = VehicleNetworkHelper(getApplicationContext<Context>().applicationContext)
        assertTrue(networkHelper.isNetworkConnected())
    }
}