package com.vehicles.map.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.vehicles.map.R
import com.vehicles.map.data.Vehicle
import com.vehicles.map.utils.Logger
import com.vehicles.map.utils.Status
import com.vehicles.map.vehiclelist.VehicleListViewModel
import kotlin.math.cos
import kotlin.math.sin

class VehicleMapFragment : Fragment(), OnMarkerClickListener  {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: VehicleListViewModel by activityViewModels()
    private var mMarkerIconTaxi: BitmapDescriptor? = null
    private  var mMarkerIconPooling:BitmapDescriptor? = null

    var latLng = LatLng(-34.0, 151.0)

//    private val callback = OnMapReadyCallback { googleMap ->
//
//        googleMap.addMarker(MarkerOptions().position(latLng).title("Marker in here"))
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }


    /**
     * Observe update on view model live data
     */
    private fun setupObserver() {

        sharedViewModel.vehicleList.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    it.data?.let { vehicle ->
                        Logger.d("VehicleMapFragment SUCCESS", vehicle.poiList.toList().toString())
                        if (vehicle.poiList.isNotEmpty()) {
                            updateMap(vehicle.poiList)
                        }
                    }

                }

                Status.LOADING -> {

                    Logger.d("VehicleMapFragment LOADING", "LOADING")
                }

                Status.ERROR -> {

                    //Handle Error
                    Logger.d("VehicleMapFragment ERROR", it.message.toString())

                }
            }
        })
    }

    private fun updateMap(vehicleList: List<Vehicle>) {

        val callback = OnMapReadyCallback { googleMap ->
            val builder = LatLngBounds.Builder()

            vehicleList.forEach {
                val marker: Marker
                val markerOptions = MarkerOptions()
                    .position(
                        LatLng(
                            it.coordinate.latitude,
                            it.coordinate.longitude
                        )
                    )
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pooling))
                    .rotation(it.heading.toFloat())
                    .anchor(0.5f, 0.5f)

                marker = googleMap.addMarker(markerOptions)
                val infoAnchorX =
                    (sin(-it.heading * Math.PI / 180) * 0.5 + 0.5).toFloat()
                val infoAnchorY =
                    (-(cos(-it.heading * Math.PI / 180) * 0.5 - 0.5)).toFloat()
                marker.setInfoWindowAnchor(infoAnchorX, infoAnchorY)

                marker.title = it.fleetType
                marker.snippet = "Id: " + it.id
                builder.include(marker.position)

            }

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    builder.build(),
                    resources.getDimensionPixelSize(R.dimen.dimens_24dp)
                )
            )

        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }



//
//    override fun onMapReady(googleMap: GoogleMap) {
//
//    }
        companion object {
        fun newInstance() = VehicleMapFragment()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        return false
    }
}