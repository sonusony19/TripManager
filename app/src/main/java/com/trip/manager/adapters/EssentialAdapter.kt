package com.trip.manager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.trip.manager.R
import com.trip.manager.databinding.EssentialLayoutBinding
import com.trip.manager.ui.trip.model.Essential

class EssentialAdapter(private val context: Context, private val essentials: List<Essential>, private val listener: View.OnClickListener)
    : RecyclerView.Adapter<EssentialAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: EssentialLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.essential_layout, parent, false))

    override fun onBindViewHolder(holder: EssentialAdapter.MyViewHolder, position: Int) {
        holder.binding.essential = essentials[position]
        holder.itemView.setOnClickListener {
            it.tag = essentials[position].id
            listener.onClick(it)
        }
    }

    override fun getItemCount() = essentials.size
}