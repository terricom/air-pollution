package com.example.airpollution.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

class RecordPagingSource(val repository: RecordRepository): PagingSource<Int, Record>() {

    private val loadLimit = 10
    private var loadCount = 0

    override fun getRefreshKey(state: PagingState<Int, Record>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Record> {
        if (params.key == null) {
            loadCount = 0
        }
        val loadResult = repository.getRecordData(loadLimit, loadCount)
        val list = mutableListOf<Record>().apply {
            if (loadResult is Result.Success) {
                addAll(loadResult.data.records)
                loadCount += loadResult.data.records.size
            } else if (loadResult is Result.Error) {
                return LoadResult.Error(loadResult.exception)
            }
        }
        val nextPage = (loadResult as? Result.Success)?.data?.takeIf { loadCount < loadResult.data.total }?.total
        return LoadResult.Page(
            data = list,
            prevKey = null,
            nextKey = nextPage
        )
    }
}
