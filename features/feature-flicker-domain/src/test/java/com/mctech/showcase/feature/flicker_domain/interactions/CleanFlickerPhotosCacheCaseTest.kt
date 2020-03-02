package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.library.logger.Logger
import com.mctech.showcase.feature.flicker_domain.service.FlickerService
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
class CleanFlickerPhotosCacheCaseTest{
    private val tag     = "cat"
    private val service = mock<FlickerService>()
    private val logger  = mock<Logger>()
    private val error   = RuntimeException()
    private lateinit var useCase: CleanFlickerPhotosCacheCase

    @Before
    fun `before each test`() {
        useCase = CleanFlickerPhotosCacheCase(service, logger)
    }

    @Test
    fun `should delegate clean cache calling`() = suspendedTestScenario(
        action = {
            useCase.execute(tag)
        },
        assertions = {
            verify(service).cleanCache(tag)
        }
    )

    @Test
    fun `should log error`() = suspendedTestScenario(
        scenario = {
            whenever(service.cleanCache(any())).thenThrow(error)
        },
        action = {
            useCase.execute(tag)
        },
        assertions = {
            verify(service).cleanCache(tag)
            verify(logger).e(e = error)
        }
    )
}