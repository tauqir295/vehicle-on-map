package com.vehicles.map.vehiclelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.vehicles.map.R
import com.vehicles.map.data.Vehicle
import com.vehicles.map.databinding.FragmentVehicleListBinding
import com.vehicles.map.map.VehicleMapFragment
import com.vehicles.map.utils.Logger
import com.vehicles.map.utils.Status
import com.vehicles.map.utils.VehicleConstants.VEHICLE
import com.vehicles.map.utils.replaceWithNextFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * [VehicleListFragment] Landing screen to show list of vehicles
 */
@AndroidEntryPoint
class VehicleListFragment : Fragment(), VehicleListAdapter.OnRecyclerItemClickListener {

    private val vehicleListViewModel: VehicleListViewModel by activityViewModels()

    // Binding object instance corresponding to the fragment_vehicle_list.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentVehicleListBinding? = null
    private val adapter = VehicleListAdapter()

    companion object {
        fun newInstance() = VehicleListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val fragmentBinding = FragmentVehicleListBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        // data binding is used
        binding?.apply {
            val layoutManager = GridLayoutManager(requireContext(), 1)

            vehicleListRv.layoutManager = layoutManager
            vehicleListRv.adapter = adapter

            adapter.setOnItemClickListener(this@VehicleListFragment)

            // Specify the current fragment as the lifecycle owner of the binding.
            // This is necessary so that the binding can observe updates.
            lifecycleOwner = this@VehicleListFragment
        }
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initiating the API calls
        vehicleListViewModel.fetchVehicleList(
            getString(R.string.lat1),
            getString(R.string.long1),
            getString(R.string.lat2),
            getString(R.string.long2)
        )

        setupObserver()
    }

    /**
     * Observe update on view model live data
     */
    private fun setupObserver() {

        vehicleListViewModel.vehicleList.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding?.progressBar?.visibility = View.GONE

                    it.data?.let { vehicle ->
                        Logger.d("LandingFragment SUCCESS", vehicle.poiList.toList().toString())
                        if (vehicle.poiList.isNotEmpty()) {
                            adapter.updateCatList(vehicle.poiList as ArrayList<Vehicle>)
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.no_data_found), Toast.LENGTH_SHORT).show()
                        }
                    } ?: handleAPIFail() // if no data found then show generic error message

                }

                Status.LOADING -> {
                    binding?.progressBar?.visibility = View.VISIBLE

                    Logger.d("LandingFragment LOADING", "LOADING")
                }

                Status.ERROR -> {
                    binding?.progressBar?.visibility = View.GONE

                    //Handle Error
                    Logger.d("LandingFragment ERROR", it.message.toString())

                    handleAPIFail()

                }
            }
        })
    }

    /**
     * handing API failed case by showing alert dialog
     */
    private fun handleAPIFail() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.api_failed))
            .setMessage(getString(R.string.something_went_wrong))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun onItemClick(item: View, vehicle: Vehicle) {

        // using extension method for navigation to DeliveryDetailsFragment
        replaceWithNextFragment(
            this.id,
            parentFragmentManager,
            VehicleMapFragment.newInstance(),
            Bundle().apply {
                      putParcelable(VEHICLE, vehicle)
            },
            true
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        vehicleListViewModel.clearViewModelData()
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}