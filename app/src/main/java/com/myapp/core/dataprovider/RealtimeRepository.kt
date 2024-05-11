package com.myapp.core.dataprovider

import com.myapp.core.common.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {
    suspend fun insert(
        item:RealtimeModelResponse.RealtimeItems
    ) : ResultState<Any>

    suspend fun getItems() : Flow<ResultState<List<RealtimeModelResponse>>>

    fun delete(key:String) :Flow<ResultState<Any>>

    fun update(
        item:RealtimeModelResponse
    ) : Flow<ResultState<Any>>
}