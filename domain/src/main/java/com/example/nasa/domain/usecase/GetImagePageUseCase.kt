package com.example.nasa.domain.usecase

import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.repository.LocalRepository
import com.example.nasa.domain.repository.RemoteRepository
import com.example.nasa.domain.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.runningReduce

class GetImagePageUseCase(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int
    ): Flow<List<Resource<NasaImage>>> = networkBoundResource(
        query = {

        },
        fetch = {

        }
    )
}