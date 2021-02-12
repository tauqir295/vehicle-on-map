package com.vehicles.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vehicles.map.vehiclelist.VehicleListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            VehicleListFragment.newInstance()
        ).commitNow()
    }
}