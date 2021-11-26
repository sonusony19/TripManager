package com.trip.manager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.trip.manager.R
import com.trip.manager.databinding.MemberLayoutBinding
import com.trip.manager.ui.trip.model.Member

class MemberAdapter(private val context: Context, private val members: List<Member>) : RecyclerView.Adapter<MemberAdapter.MyViewHolder>() {

    val selectedMembers = arrayListOf<Member>()
    private var selectionMode: Boolean = false

    inner class MyViewHolder(val binding: MemberLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.member_layout, parent, false))

    override fun onBindViewHolder(holder: MemberAdapter.MyViewHolder, position: Int) {
        holder.binding.member = members[position]
        holder.binding.checkbox.visibility = if (selectionMode) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            if (selectionMode) {
                if (selectedMembers.contains(members[position])) {
                    selectedMembers.remove(members[position])
                } else {
                    selectedMembers.add(members[position])
                }
                holder.binding.selected = selectedMembers.contains(members[position])
            } else {

            }
        }
    }

    override fun getItemCount() = members.size

    fun setSelectionMode(mode: Boolean): MemberAdapter {
        selectionMode = mode
        return this
    }

}