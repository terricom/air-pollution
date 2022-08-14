package com.example.airpollution.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.example.airpollution.R
import com.example.airpollution.data.Record
import kotlinx.android.synthetic.main.holder_view_header.view.*

class HeaderAdapter: RecyclerView.Adapter<HeaderViewHolder>() {

    private val pagingAdapter: RecordHeaderAdapter = RecordHeaderAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_view_header, parent, false)).apply {
            pagingAdapter.addLoadStateListener {
                itemView.headerProgressBar.visibility = if (it.refresh is LoadState.NotLoading) View.GONE else View.VISIBLE
            }
            itemView.headerRecyclerView.adapter = pagingAdapter
        }
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        pagingAdapter.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 1

    suspend fun submitData(filter: PagingData<Record>) {
        pagingAdapter.submitData(filter)
        notifyDataSetChanged()
    }

    fun retry() {
        pagingAdapter.retry()
        notifyDataSetChanged()
    }
}