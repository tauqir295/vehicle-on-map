package com.vehicles.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vehicles.map.vehiclelist.VehicleListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                VehicleListFragment.newInstance()
            ).commit()
        }

    }
}