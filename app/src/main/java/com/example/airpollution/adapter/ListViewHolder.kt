package com.example.airpollution.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.airpollution.R
import com.example.airpollution.data.Record
import kotlinx.android.synthetic.main.holder_view_row.view.*

class ListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bindResult(record: Record, recordClick: (record: Record) -> Unit) {
        with(itemView) {
            siteId.text = record.siteId
            name.text = record.siteName
            value.text = record.pmValue.takeUnless { it.isBlank() } ?: context.getString(R.string.empty_value_place_holder)
            county.text = record.county
            next.setOnClickListener { recordClick(record) }
            if (record.status == context.getString(R.string.status_good)) {
                status.text = context.getString(R.string.status_good_description)
                next.visibility = View.GONE
            } else {
                status.text = record.status.takeUnless { it.isBlank() } ?: context.getString(R.string.empty_value_place_holder)
                next.visibility = View.VISIBLE
            }
        }
    }
}