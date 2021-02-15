package com.vehicles.map.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.vehicles.map.R
import com.vehicles.map.data.Vehicle
import com.vehicles.map.utils.Logger
import com.vehicles.map.utils.Status
import com.vehicles.map.utils.VehicleConstants.FOCUS_TILT
import com.vehicles.map.utils.VehicleConstants.FOCUS_ZOOM_LEVEL
import com.vehicles.map.utils.VehicleConstants.POOLING
import com.vehicles.map.utils.VehicleConstants.VEHICLE
import com.vehicles.map.vehiclelist.VehicleListViewModel
import kotlin.math.cos
import kotlin.math.sin

class VehicleMapFragment : Fragment(), OnMarkerClickListener, OnMapReadyCallback {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: VehicleListViewModel by activityViewModels()
    private var googleMap: GoogleMap? = null
    private var vehicleList = ArrayList<Vehicle>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //enabling the home back button
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this@VehicleMapFragment)

        setupObserver()
    }

    /**
     * Observe update on shared view model live data
     */
    private fun setupObserver() {

        sharedViewModel.vehicleList.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.SUCCESS -> {

                    it.data?.let { vehicle ->
                        Logger.d("VehicleMapFragment SUCCESS", vehicle.poiList.toList().toString())
                        if (vehicle.poiList.isNotEmpty()) {
                            vehicleList.addAll(vehicle.poiList as ArrayList<Vehicle>)
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

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {
            animateMarkerToCenter(it)
        }
        return true
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.setOnMarkerClickListener(this)
        val builder = LatLngBounds.Builder()
        val markersMap = mutableMapOf<Int, Marker>()
        googleMap?.run {
            vehicleList.forEach {
                val markerOptions = MarkerOptions()
                    .position(
                        LatLng(
                            it.coordinate.latitude,
                            it.coordinate.longitude
                        )
                    )
                    .icon(getIcon(vehicle = it))
                    .rotation(it.heading.toFloat())
                    .anchor(0.5f, 0.5f)

                val marker = addMarker(markerOptions)
                val infoAnchorX =
                    (sin(-it.heading * Math.PI / 180) * 0.5 + 0.5).toFloat()
                val infoAnchorY =
                    (-(cos(-it.heading * Math.PI / 180) * 0.5 - 0.5)).toFloat()
                marker.setInfoWindowAnchor(infoAnchorX, infoAnchorY)

                marker.title = it.fleetType
                marker.snippet = "Id: ${it.id}"

                markersMap[it.id] = marker
                builder.include(marker.position)
            }

            val vehicle: Vehicle? = arguments?.getParcelable(VEHICLE)

            vehicle?.let {
                if (markersMap.containsKey(it.id)) {
                    markersMap[it.id]?.let { marker -> animateMarkerToCenter(marker) }
                }
            }?: moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    builder.build(),
                    resources.getDimensionPixelSize(R.dimen.dimens_24dp)
                )
            )
        }
    }

    /**
     * get marker icon based on fleetType
     * @param - [Vehicle] - fleetType is used as filter
     */
    private fun getIcon(vehicle: Vehicle): BitmapDescriptor? {
        return if (vehicle.fleetType == POOLING) {
            BitmapDescriptorFactory.fromResource(R.drawable.pooling)
        } else {
            BitmapDescriptorFactory.fromResource(R.drawable.taxi)
        }
    }

    /**
     * animate map to selected marker
     * @param - [Marker] - selected marker to view on map
     */
    private fun animateMarkerToCenter(selectedMarker: Marker) {
        val cameraPosition = CameraPosition.Builder()
            .target(selectedMarker.position)
            .zoom(FOCUS_ZOOM_LEVEL)
            .bearing(selectedMarker.rotation)
            .tilt(FOCUS_TILT.toFloat())
            .build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        selectedMarker.showInfoWindow()
    }

    companion object {
        fun newInstance() = VehicleMapFragment()
    }
}