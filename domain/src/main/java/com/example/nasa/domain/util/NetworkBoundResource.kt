package com.example.nasa.domain.util


import com.example.nasa.domain.model.Resource
import kotlinx.coroutines.flow.*


inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> Result<RequestType>,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        fetch()
            .fold(
                onSuccess = { result ->
                    saveFetchResult(result)
                    query().map { Resource.Success(it) }
                },
                onFailure = { throwable ->
                    query().map { Resource.Error(throwable = throwable, data = it) }
                }
            )
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}