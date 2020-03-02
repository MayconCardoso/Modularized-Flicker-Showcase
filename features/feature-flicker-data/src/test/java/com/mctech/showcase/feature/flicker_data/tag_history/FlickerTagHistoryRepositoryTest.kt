package com.mctech.showcase.feature.flicker_data.tag_history

import com.mctech.showcase.feature.flicker_domain.service.FlickerTagHistoryService
import com.mctech.testing.data_factory.suspendedTestScenario
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
@ExperimentalCoroutinesApi
class FlickerTagHistoryRepositoryTest {
    private val tag = "cat"
    private val dataSource = mock<FlickerTagHistoryDataSource>()

    private lateinit var service: FlickerTagHistoryService

    @Before
    fun `before each test`() {
        service = FlickerTagHistoryRepository(dataSource)
    }

    @Test
    fun `should delegate save call`() = suspendedTestScenario(
        action = {
            service.saveTag(tag)
        },
        assertions = {
            verify(dataSource).saveTag(tag)
        }
    )

    @Test
    fun `should delegate load call`() = suspendedTestScenario(
        action = {
            service.loadAllTags()
        },
        assertions = {
            verify(dataSource).loadAllTags()
        }
    )
}