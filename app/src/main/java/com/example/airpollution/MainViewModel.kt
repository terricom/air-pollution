package com.example.airpollution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.airpollution.data.RecordPagingSource
import com.example.airpollution.data.RecordRepository

class MainViewModel: ViewModel() {

    private val repository = RecordRepository.getInstance()
    val filterValue = 10

    val headerPagingData = Pager(PagingConfig(1)) {
        RecordPagingSource(repository)
    }.flow.asLiveData()

    val recordPagingData = Pager(PagingConfig(1)) {
        RecordPagingSource(repository)
    }.flow.asLiveData()
}
