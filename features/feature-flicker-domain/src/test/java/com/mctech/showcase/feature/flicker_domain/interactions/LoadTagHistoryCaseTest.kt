package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.showcase.feature.flicker_domain.assertResultFailure
import com.mctech.showcase.feature.flicker_domain.assertResultSuccess
import com.mctech.showcase.feature.flicker_domain.service.FlickerTagHistoryService
import com.mctech.testing.data_factory.FlickerTagHistoryDataFactory
import com.mctech.testing.data_factory.suspendedTestScenario
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
class LoadTagHistoryCaseTest{

    private val service         = mock<FlickerTagHistoryService>()
    private val error           = RuntimeException()
    private val expectedValue   = FlickerTagHistoryDataFactory.listOf(5)
    private lateinit var useCase: LoadTagHistoryCase

    @Before
    fun `before each test`() {
        useCase = LoadTagHistoryCase(service)
    }

    @Test
    fun `should delegate calling`() = suspendedTestScenario(
        scenario = {
            whenever(service.loadAllTags()).thenReturn(expectedValue)
        },
        action = {
            useCase.execute()
        },
        assertions = {
            verify(service).loadAllTags()
        }
    )

    @Test
    fun `should return tag history`() = suspendedTestScenario(
        scenario = {
            whenever(service.loadAllTags()).thenReturn(expectedValue)
        },
        action = {
            useCase.execute()
        },
        assertions = { result ->
            result.assertResultSuccess(expectedValue)
        }
    )

    @Test
    fun `should return error`() = suspendedTestScenario(
        scenario = {
            whenever(service.loadAllTags()).thenThrow(error)
        },
        action = {
            useCase.execute()
        },
        assertions = { result ->
            result.assertResultFailure(error)
        }
    )
}