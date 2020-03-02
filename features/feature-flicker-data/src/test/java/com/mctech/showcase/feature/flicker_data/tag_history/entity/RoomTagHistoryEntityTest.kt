package com.mctech.showcase.feature.flicker_data.tag_history.entity

import com.mctech.testing.data_factory.suspendedTestScenario
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions
import org.junit.Test

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
@ExperimentalCoroutinesApi
class RoomTagHistoryEntityTest{
    private val expectedValue = RoomTagHistoryEntity(
        tag = "cat",
        date = 1000L
    )

    @Test
    fun `should validate entity`() = suspendedTestScenario(
        action = {
            expectedValue
        },
        assertions = { photo ->
            Assertions.assertThat(photo.tag).isEqualTo("cat")
            Assertions.assertThat(photo.date).isEqualTo(1000L)
        }
    )
}