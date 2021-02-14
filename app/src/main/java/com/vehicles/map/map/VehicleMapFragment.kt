package com.vehicles.map.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vehicles.map.R
import com.vehicles.map.data.Vehicle

class VehicleMapFragment : Fragment() {

    var latLng = LatLng(-34.0, 151.0)
    lateinit var vehicle: Vehicle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vehicle = requireNotNull(arguments?.getParcelable("vehicle")) as Vehicle

        latLng = LatLng(vehicle.coordinate.latitude, vehicle.coordinate.longitude)

    }

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.addMarker(MarkerOptions().position(latLng).title("Marker in here"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    companion object {
        fun newInstance(vehicle:Vehicle) = VehicleMapFragment().apply {
            arguments = Bundle().apply {
                putParcelable("vehicle", vehicle)
            }
        }
    }
}