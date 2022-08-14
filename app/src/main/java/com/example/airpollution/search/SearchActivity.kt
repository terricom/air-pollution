package com.example.airpollution.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.filter
import androidx.recyclerview.widget.ConcatAdapter
import com.example.airpollution.R
import com.example.airpollution.adapter.LoadingViewHolder
import com.example.airpollution.adapter.RecordPagingAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.editor
import kotlinx.android.synthetic.main.activity_search.hintText
import kotlinx.android.synthetic.main.activity_search.view.*
import kotlinx.android.synthetic.main.holder_loading.view.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class SearchActivity: AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels()
    private var recordPagingAdapter: RecordPagingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initLayout()
        viewModel.observe()
    }

    private fun initLayout() {
        recordPagingAdapter = RecordPagingAdapter {
            Toast.makeText(this, getString(R.string.toast_message, it.county, it.siteName), Toast.LENGTH_LONG).show()
        }
        val footerAdapter = object : LoadStateAdapter<LoadingViewHolder>() {
            override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
                with(holder.itemView.retryButton) {
                    visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
                    setOnClickListener {
                        it.visibility = View.GONE
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
                    if (loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && (recordPagingAdapter?.itemCount ?: 0) < 1) {
                        recyclerView?.visibility = View.GONE
                        val searchText = editor.text.toString()
                        with(hintText) {
                            visibility = View.VISIBLE
                            text = if (searchText.isBlank()) getString(R.string.init_search_hint) else getString(R.string.empty_search_hint, searchText)
                        }
                    } else {
                        recyclerView?.visibility = View.VISIBLE
                        with(hintText) {
                            visibility = if (loadState.refresh is LoadState.Loading) View.VISIBLE else View.GONE
                            text = getString(R.string.init_search_hint)
                        }
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
        recyclerView.adapter = ConcatAdapter(recordPagingAdapter, footerAdapter)
        with(editor) {
            setOnEditorActionListener { editText, actionId, event ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> recordPagingAdapter?.refresh()
                }
                return@setOnEditorActionListener false
            }
        }
        back.setOnClickListener { onBackPressed() }
    }


    private fun SearchViewModel.observe() {
        recordPagingData.observe(this@SearchActivity) {
            lifecycleScope.launch {
                recordPagingAdapter?.submitData(it.filter { !editor.text.isNullOrEmpty() && it.siteName.contains(editor.text.toString()) })
            }
        }
    }

    companion object {
        fun startActivity(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, SearchActivity::class.java))
        }
    }
}
