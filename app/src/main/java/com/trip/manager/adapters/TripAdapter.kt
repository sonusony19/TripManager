package com.trip.manager.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.trip.manager.R
import com.trip.manager.databinding.TripLayoutBinding
import com.trip.manager.ui.trip.model.Trip
import com.trip.manager.ui.trip.view.TripDetailsActivity

class TripAdapter(private val context: Context, private val trips: List<Trip>) : RecyclerView.Adapter<TripAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: TripLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.trip_layout, parent, false))

    override fun onBindViewHolder(holder: TripAdapter.MyViewHolder, position: Int) {
        holder.binding.trip = trips[position]
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, TripDetailsActivity::class.java).putExtra("ID", trips[position].id))
        }
    }

    override fun getItemCount() = trips.size
}