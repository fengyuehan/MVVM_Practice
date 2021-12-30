package com.example.lib_base.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class BaseRepository {
    protected fun <T> request(block:suspend FlowCollector<T>.() -> Unit):Flow<T> {
        return flow(block).flowOn(Dispatchers.IO)
    }
}