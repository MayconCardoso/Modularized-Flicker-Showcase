package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.library.logger.Logger
import com.mctech.showcase.feature.flicker_domain.assertResultFailure
import com.mctech.showcase.feature.flicker_domain.assertResultSuccess
import com.mctech.showcase.feature.flicker_domain.error.FlickerPhotoError
import com.mctech.showcase.feature.flicker_domain.error.NetworkException
import com.mctech.showcase.feature.flicker_domain.service.FlickerService
import com.mctech.testing.data_factory.FlickerPhotosDataFactory
import com.mctech.testing.data_factory.suspendedTestScenario
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
@ExperimentalCoroutinesApi
class LoadNextPageOfFlickerPhotosCaseTest{
    private val tag             = "cat"
    private val service         = mock<FlickerService>()
    private val logger          = mock<Logger>()

    private val expectedValue   = FlickerPhotosDataFactory.listOf(5)

    private lateinit var useCase: LoadNextPageOfFlickerPhotosCase

    @Before
    fun `before each test`() {
        useCase = LoadNextPageOfFlickerPhotosCase(service,logger)
    }

    @Test
    fun `should delegate calling`() = suspendedTestScenario(
        scenario = {
            whenever(service.loadNextPageOfPhotos(any())).thenReturn(expectedValue)
        },
        action = {
            useCase.execute(tag)
        },
        assertions = {
            verify(service).loadNextPageOfPhotos(tag)
        }
    )

    @Test
    fun `should return photos`() = suspendedTestScenario(
        scenario = {
            whenever(service.loadNextPageOfPhotos(any())).thenReturn(expectedValue)
        },
        action = {
            useCase.execute(tag)
        },
        assertions = { result ->
            result.assertResultSuccess(expectedValue)
        }
    )

    @Test
    fun `should return known exception`() = failureAssertion(
        exception = FlickerPhotoError.UnknownException,
        expectedException = FlickerPhotoError.UnknownException
    )

    @Test
    fun `should return unknown exception`() = failureAssertion(
        exception = RuntimeException(),
        expectedException = FlickerPhotoError.UnknownException
    )

    @Test
    fun `should return network exception`() = failureAssertion(
        exception = NetworkException,
        expectedException = NetworkException
    )

    private fun failureAssertion(exception: Throwable, expectedException: Exception) = suspendedTestScenario(
        scenario = {
            whenever(service.loadNextPageOfPhotos(any())).thenThrow(exception)
        },
        action = {
            useCase.execute(tag)
        },
        assertions = { result ->
            result.assertResultFailure(expectedException)
            verify(logger).e(e = exception)
        }
    )
}