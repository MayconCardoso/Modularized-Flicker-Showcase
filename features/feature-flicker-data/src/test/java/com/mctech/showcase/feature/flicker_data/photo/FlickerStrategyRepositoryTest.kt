package com.mctech.showcase.feature.flicker_data.photo

import com.mctech.showcase.feature.flicker_data.photo.remote.FlickerRemoteDataSource
import com.mctech.showcase.feature.flicker_domain.service.FlickerService
import com.mctech.testing.data_factory.FlickerPhotosDataFactory
import com.mctech.testing.data_factory.suspendedTestScenario
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
@ExperimentalCoroutinesApi
class FlickerStrategyRepositoryTest{
    private val tag                     = "cat"
    private val cacheExpectedValue      = FlickerPhotosDataFactory.listOf(5)
    private val dataBaseExpectedValue   = FlickerPhotosDataFactory.listOf(5)
    private val remoteExpectedValue     = FlickerPhotosDataFactory.listOf(5)

    private val cacheDataSource         = mock<FlickerStorableService>()
    private val databaseDataSource      = mock<FlickerStorableService>()
    private val remoteDataSource        = mock<FlickerRemoteDataSource>()

    private lateinit var service: FlickerService

    @Before
    fun `before each test`() {
        service = FlickerStrategyRepository(
            cacheDataSource,
            databaseDataSource,
            remoteDataSource
        )
    }

    @Test
    fun `should delegate clean cache call`() = suspendedTestScenario(
        action = {
            service.cleanCache(tag)
        },
        assertions = {
            verify(cacheDataSource).deleteAllByTag(tag)
            verify(databaseDataSource).deleteAllByTag(tag)
            verifyZeroInteractions(remoteDataSource)
        }
    )

    @Test
    fun `should return from cache first page`() = suspendedTestScenario(
        scenario = {
          whenever(cacheDataSource.restore(any(), any())).thenReturn(cacheExpectedValue)
        },
        action = {
            service.loadFirstPageOfPhotos(tag)
        },
        assertions = { result ->
            verify(cacheDataSource).restore(tag, 1)
            verifyZeroInteractions(databaseDataSource)
            verifyZeroInteractions(remoteDataSource)
            assertThat(result).isEqualTo(cacheExpectedValue)
        }
    )

    @Test
    fun `should return from cache other page`() = suspendedTestScenario(
        scenario = {
            whenever(cacheDataSource.restore(any(), any())).thenReturn(cacheExpectedValue)
        },
        action = {
            service.loadFirstPageOfPhotos(tag)
            service.loadNextPageOfPhotos(tag)
        },
        assertions = { result ->
            verify(cacheDataSource).restore(tag, 2)
            verifyZeroInteractions(databaseDataSource)
            verifyZeroInteractions(remoteDataSource)

            assertThat(result).isEqualTo(cacheExpectedValue)
        }
    )

    @Test
    fun `should return from database first page`() = suspendedTestScenario(
        scenario = {
          whenever(cacheDataSource.restore(any(), any())).thenReturn(null)
          whenever(databaseDataSource.restore(any(), any())).thenReturn(dataBaseExpectedValue)
        },
        action = {
            service.loadFirstPageOfPhotos(tag)
        },
        assertions = { result ->
            verify(databaseDataSource).restore(tag, 1)
            verify(cacheDataSource).save(tag, 1, dataBaseExpectedValue)

            verifyZeroInteractions(remoteDataSource)
            assertThat(result).isEqualTo(dataBaseExpectedValue)
        }
    )

    @Test
    fun `should return from database other page`() = suspendedTestScenario(
        scenario = {
            whenever(cacheDataSource.restore(any(), any())).thenReturn(null)
            whenever(databaseDataSource.restore(any(), any())).thenReturn(dataBaseExpectedValue)
        },
        action = {
            service.loadFirstPageOfPhotos(tag)
            service.loadNextPageOfPhotos(tag)
        },
        assertions = { result ->
            verify(databaseDataSource).restore(tag, 2)
            verify(cacheDataSource).save(tag, 2, dataBaseExpectedValue)

            verifyZeroInteractions(remoteDataSource)
            assertThat(result).isEqualTo(dataBaseExpectedValue)
        }
    )

    @Test
    fun `should return from server first page`() = suspendedTestScenario(
        scenario = {
          whenever(cacheDataSource.restore(any(), any())).thenReturn(null)
          whenever(databaseDataSource.restore(any(), any())).thenReturn(null)
          whenever(remoteDataSource.loadPhotosByPage(any(), any())).thenReturn(remoteExpectedValue)
        },
        action = {
            service.loadFirstPageOfPhotos(tag)
        },
        assertions = { result ->
            verify(remoteDataSource).loadPhotosByPage(tag, 1)
            verify(cacheDataSource).save(tag, 1, remoteExpectedValue)
            verify(databaseDataSource).save(tag, 1, remoteExpectedValue)

            assertThat(result).isEqualTo(remoteExpectedValue)
        }
    )

    @Test
    fun `should return from server other page`() = suspendedTestScenario(
        scenario = {
            whenever(cacheDataSource.restore(any(), any())).thenReturn(null)
            whenever(databaseDataSource.restore(any(), any())).thenReturn(null)
            whenever(remoteDataSource.loadPhotosByPage(any(), any())).thenReturn(remoteExpectedValue)
        },
        action = {
            service.loadFirstPageOfPhotos(tag)
            service.loadNextPageOfPhotos(tag)
        },
        assertions = { result ->
            verify(remoteDataSource).loadPhotosByPage(tag, 2)
            verify(cacheDataSource).save(tag, 2, remoteExpectedValue)
            verify(databaseDataSource).save(tag, 2, remoteExpectedValue)

            assertThat(result).isEqualTo(remoteExpectedValue)
        }
    )

}