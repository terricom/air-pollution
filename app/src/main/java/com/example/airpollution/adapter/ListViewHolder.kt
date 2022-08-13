package com.example.airpollution.adapter

import android.view.View
import com.example.airpollution.R
import kotlinx.android.synthetic.main.holder_view_row.view.*

class ListViewHolder(view: View): RecordViewHolder(view) {
    fun bindResult(data: RecordAdapter.RecordUiData.Item) {
        val record = data.item
        with(itemView) {
            siteId.text = record.siteId
            name.text = record.siteName
            value.text = "${record.pmValue}"
            county.text = record.county
            if (record.status == context.getString(R.string.status_good)) {
                status.text = context.getString(R.string.status_good_description)
                next.visibility = View.GONE
            } else {
                status.text = record.status
                next.visibility = View.VISIBLE
            }
        }
    }
}