package com.example.airpollution.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.airpollution.R
import com.example.airpollution.data.Record

class RecordPagingAdapter(val recordClick: (record: Record) -> Unit) : PagingDataAdapter<Record, ListViewHolder>(ResultComparator) {

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val record = getItem(position) ?: return
        holder.bindResult(record, recordClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_view_row, parent, false))
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