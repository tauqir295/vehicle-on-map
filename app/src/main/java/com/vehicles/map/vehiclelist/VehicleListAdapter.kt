package com.vehicles.map.vehiclelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vehicles.map.data.Vehicle
import com.vehicles.map.data.VehicleList
import com.vehicles.map.databinding.VehicleListItemBinding

/**
 * [RecyclerView.Adapter] that can display a [VehicleList].
 */
class VehicleListAdapter : RecyclerView.Adapter<VehicleListAdapter.VehicleViewHolder>() {
    private var mOnItemClickListener: OnRecyclerItemClickListener? = null
    private val vehicleList = ArrayList<Vehicle>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        return VehicleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]
        holder.onBind(vehicle)
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(it, vehicle)
        }
    }

    override fun getItemCount(): Int = vehicleList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    /**
     * adding the list received to adapter
     * @param: [list] - used to populate the adapter items
     */
    fun updateCatList(list: ArrayList<Vehicle>) {
        val vehicleListSize = vehicleList.size
        vehicleList.addAll(list)
        notifyItemRangeChanged(vehicleListSize, vehicleList.size)
    }

    /**
     * initiating the listener
     *
     * @param:[listener] - use this to assign the class level listener
     */
    fun setOnItemClickListener(listener: OnRecyclerItemClickListener) {
        mOnItemClickListener = listener
    }

    class VehicleViewHolder(private val binding: VehicleListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): VehicleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VehicleListItemBinding.inflate(layoutInflater, parent, false)
                return VehicleViewHolder(binding)
            }
        }

        fun onBind(vehicle: Vehicle) {
            binding.vehicle = vehicle
            binding.executePendingBindings()
        }
    }

    /**
     * custom interface to pass the click event on item to the listener
     */
    interface OnRecyclerItemClickListener {
        fun onItemClick(
                item: View,
                vehicle: Vehicle
        )
    }

}