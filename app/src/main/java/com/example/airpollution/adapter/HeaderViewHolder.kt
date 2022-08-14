package com.example.airpollution.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.airpollution.data.Record
import kotlinx.android.synthetic.main.holder_view_header_item.view.*

class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view)

class HeaderItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bindHeaderItem(record: Record) {
        with(itemView) {
            siteId.text = record.siteId
            name.text = record.siteName
            value.text = record.pmValue
            county.text = record.county
            status.text = record.status
        }
    }
}
