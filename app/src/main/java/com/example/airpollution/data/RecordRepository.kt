package com.example.airpollution.data

class RecordRepository {

    private val cachedRecordData = HashMap<Int, RecordData>()

    private val apiManager
        get() = ApiManager.create()

    suspend fun getRecordData(limit: Int, offset: Int): Result<RecordData> =
        safeApiCall {
            cachedRecordData[offset]?.let { Result.Success(it) } ?: apiManager.getRecords(limit = limit, offset = offset).getResult().also {
                if (it is Result.Success) {
                    cachedRecordData.getOrPut(offset) { it.data }
                }
            }
        }

    companion object {

        private var INSTANCE: RecordRepository? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: synchronized(RecordRepository::class.java) {
            INSTANCE ?: RecordRepository().also { INSTANCE = it }
        }
    }
}