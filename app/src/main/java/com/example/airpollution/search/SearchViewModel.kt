package com.example.airpollution.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.airpollution.data.RecordPagingSource
import com.example.airpollution.data.RecordRepository

class SearchViewModel: ViewModel() {
    private val repository = RecordRepository.getInstance()

    val recordPagingData = Pager(PagingConfig(1)) {
        RecordPagingSource(repository)
    }.flow.asLiveData()
}
