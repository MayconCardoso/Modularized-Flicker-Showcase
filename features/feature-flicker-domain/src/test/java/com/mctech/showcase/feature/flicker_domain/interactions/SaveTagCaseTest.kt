package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.library.logger.Logger
import com.mctech.showcase.feature.flicker_domain.service.FlickerTagHistoryService
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
class SaveTagCaseTest{
    private val tag     = "cat"
    private val service = mock<FlickerTagHistoryService>()
    private val logger  = mock<Logger>()
    private val error   = RuntimeException()
    private lateinit var useCase: SaveTagCase

    @Before
    fun `before each test`() {
        useCase = SaveTagCase(service, logger)
    }

    @Test
    fun `should delegate save calling`() = suspendedTestScenario(
        action = {
            useCase.execute(tag)
        },
        assertions = {
            verify(service).saveTag(tag)
        }
    )

    @Test
    fun `should log error`() = suspendedTestScenario(
        scenario = {
            whenever(service.saveTag(any())).thenThrow(error)
        },
        action = {
            useCase.execute(tag)
        },
        assertions = {
            verify(service).saveTag(tag)
            verify(logger).e(e = error)
        }
    )
}