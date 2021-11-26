package com.trip.manager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.trip.manager.R
import com.trip.manager.databinding.UserItemViewBinding
import com.trip.manager.ui.user.model.User

class UserAdapter(private val context: Context, private val users: List<User>) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    val selectedUsers = arrayListOf<User>()

    inner class MyViewHolder(val binding: UserItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.user_item_view, parent, false))

    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {
        holder.binding.user = users[position]
        holder.itemView.setOnClickListener {
            if (selectedUsers.contains(users[position])) {
                selectedUsers.remove(users[position])
            } else {
                selectedUsers.add(users[position])
            }
            holder.binding.selected = selectedUsers.contains(users[position])
        }
    }

    override fun getItemCount() = users.size
}