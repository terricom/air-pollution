package com.example.airpollution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.filter
import androidx.recyclerview.widget.ConcatAdapter
import com.example.airpollution.adapter.HeaderAdapter
import com.example.airpollution.adapter.LoadingViewHolder
import com.example.airpollution.adapter.RecordPagingAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.holder_loading.view.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var headerAdapter: HeaderAdapter? = null
    private var recordPagingAdapter: RecordPagingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLayout()
        viewModel.observe()
    }

    private fun initLayout() {
        headerAdapter = HeaderAdapter()
        recordPagingAdapter = RecordPagingAdapter {
            Toast.makeText(this@MainActivity, getString(R.string.toast_message, it.county, it.siteName), Toast.LENGTH_LONG).show()
        }
        val footerAdapter = object : LoadStateAdapter<LoadingViewHolder>() {
            override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
                with(holder.itemView.retryButton) {
                    visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
                    setOnClickListener {
                        it.visibility = View.GONE
                        headerAdapter?.retry()
                        recordPagingAdapter?.retry()
                    }
                }
                holder.itemView.progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                loadState: LoadState
            ): LoadingViewHolder {
                return LoadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.holder_loading, parent, false)
                )
            }
        }
        lifecycleScope.launch {
            recordPagingAdapter?.let {
                it.addLoadStateListener { loadState ->
                    footerAdapter.loadState = when (loadState.refresh) {
                        is LoadState.NotLoading -> loadState.append
                        else -> loadState.refresh
                    }
                }
                it.loadStateFlow
                    .distinctUntilChanged { old, new ->
                        old.prepend.endOfPaginationReached == new.prepend.endOfPaginationReached
                    }.filter {
                        it.refresh is LoadState.NotLoading && it.prepend.endOfPaginationReached && !it.append.endOfPaginationReached
                    }.collect {
                        recyclerView.scrollToPosition(0)
                    }
            }
        }
        recyclerView.adapter = ConcatAdapter(headerAdapter, recordPagingAdapter, footerAdapter)
    }

    private fun MainViewModel.observe() {
        headerPagingData.observe(this@MainActivity) {
            lifecycleScope.launch {
                headerAdapter?.submitData(it.filter { it.pmValue.toIntOrNull() != null && it.pmValue.toInt() < filterValue })
            }
        }
        recordPagingData.observe(this@MainActivity) {
            lifecycleScope.launch {
                recordPagingAdapter?.submitData(it.filter { (it.pmValue.toIntOrNull() ?: filterValue) >= filterValue })
            }
        }
    }
}
