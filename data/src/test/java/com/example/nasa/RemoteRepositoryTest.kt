package com.example.nasa

import com.example.nasa.data.network.*
import com.example.nasa.data.network.Collection
import com.example.nasa.data.network.api.NasaImagesApi
import com.example.nasa.data.network.model.NasaImageDto
import com.example.nasa.data.repository.RemoteImagesRepositoryImpl
import com.example.nasa.data.util.log
import com.example.nasa.data.util.mapToDomain
import com.example.nasa.domain.model.NasaImage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RemoteRepositoryTest {

    private val api = mockk<NasaImagesApi> {
        coEvery { getNasaImages(any(), any(), any(), any()) } coAnswers {
            val page = arg<Int>(0)
            if (page == 0) {
                STUB_NASA_IMAGE
            } else {
                error("Some error")
            }
        }
    }

    private val repository = RemoteImagesRepositoryImpl(api)

    @Test
    fun testSuccessLoading() = runTest {
        val result = repository.fetchNasaImages(page = 0, "", 0, 0)
        Assert.assertTrue(result.isSuccess)
        result.getOrThrow()
            .zip(STUB_NASA_IMAGE.collection.items)
            .forEach { (nasaImage, item) ->
                Assert.assertEquals(item.data.getOrNull(0)?.nasaId, nasaImage.id)
                Assert.assertEquals(item.links.getOrNull(0)?.href, nasaImage.imageUrl)
            }
    }

    @Test
    fun testFailedLoading() = runTest {
        val result = repository.fetchNasaImages(page = 1, "", 0, 0)
        Assert.assertTrue(result.isFailure)
    }

    companion object {
        private val STUB_NASA_IMAGE = NasaImageDto(Collection(List(1) {
            Item(List(1) {
                Data("$it", "$it", "$it")
            }, List(1) {
                Link("$it")
            })
        }, Metadata(1)))
    }
}
