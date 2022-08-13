package com.example.airpollution.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.airpollution.R
import com.example.airpollution.Record

class RecordAdapter: RecyclerView.Adapter<RecordViewHolder>() {

    private val dataList = mutableListOf<RecordUiData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return when (viewType) {
            LIST_VIEW_TYPE -> ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_view_row, parent, false))
            else -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_view_header, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataList[position]) {
            is RecordUiData.Header -> HEADER_VIEW_TYPE
            else -> LIST_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val data = dataList[position]
        if (holder is ListViewHolder && data is RecordUiData.Item) {
            holder.bindResult(data)
        } else if (holder is HeaderViewHolder && data is RecordUiData.Header) {
            holder.bindHeader(data)
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun setData(list: List<RecordUiData>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val LIST_VIEW_TYPE = 1
    }

    sealed class RecordUiData {
        data class Header(val header: List<Record>): RecordUiData()
        data class Item(val item: Record): RecordUiData()
    }
}