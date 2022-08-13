package com.example.airpollution.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.airpollution.R
import com.example.airpollution.Record
import kotlinx.android.synthetic.main.holder_view_header.view.*
import kotlinx.android.synthetic.main.holder_view_header_item.view.*

class HeaderViewHolder(view: View): RecordViewHolder(view) {
    fun bindHeader(data: RecordAdapter.RecordUiData.Header) {
        itemView.headerRecyclerView.adapter = object : RecyclerView.Adapter<HeaderItemViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): HeaderItemViewHolder {
                return HeaderItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_view_header_item, parent, false))
            }

            override fun onBindViewHolder(holder: HeaderItemViewHolder, position: Int) {
                holder.bindHeaderItem(data.header[position])
            }

            override fun getItemCount(): Int {
                return data.header.size
            }
        }
    }

    inner class HeaderItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindHeaderItem(record: Record) {
            with(itemView) {
                siteId.text = record.siteId
                name.text = record.siteName
                value.text = "${record.pmValue}"
                county.text = record.county
                status.text = record.status
            }
        }
    }
}