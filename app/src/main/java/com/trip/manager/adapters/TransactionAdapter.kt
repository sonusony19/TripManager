package com.trip.manager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.trip.manager.R
import com.trip.manager.databinding.TransactionLayoutBinding
import com.trip.manager.ui.trip.model.Transaction

class TransactionAdapter(private val context: Context, private val transactions: List<Transaction>)
    : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: TransactionLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.transaction_layout, parent, false))

    override fun onBindViewHolder(holder: TransactionAdapter.MyViewHolder, position: Int) {
        holder.binding.transaction = transactions[position]
    }

    override fun getItemCount() = transactions.size
}