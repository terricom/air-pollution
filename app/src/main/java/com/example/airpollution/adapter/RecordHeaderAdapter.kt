package com.example.airpollution.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.airpollution.R
import com.example.airpollution.data.Record

class RecordHeaderAdapter : PagingDataAdapter<Record, HeaderItemViewHolder>(ResultComparator) {

    override fun onBindViewHolder(holder: HeaderItemViewHolder, position: Int) {
        val record = getItem(position) ?: return
        holder.bindHeaderItem(record)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderItemViewHolder {
        return HeaderItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_view_header_item, parent, false))
    }

    companion object {
        object ResultComparator : DiffUtil.ItemCallback<Record>() {
            override fun areItemsTheSame(
                oldItem: Record,
                newItem: Record
            ): Boolean {
                return oldItem.siteId == newItem.siteId
            }

            override fun areContentsTheSame(
                oldItem: Record,
                newItem: Record
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}