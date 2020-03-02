package com.mctech.showcase.feature.flicker_domain.entity

import com.mctech.testing.data_factory.FlickerPhotosDataFactory
import com.mctech.testing.data_factory.suspendedTestScenario
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
@ExperimentalCoroutinesApi
class FlickerPhotoTest{
    private val expectedValue = FlickerPhotosDataFactory.single(
        id = 1,
        tag = "cat",
        title = "title",
        thumbnailUrl = "thumb",
        sourceUrl = "source"
    )

    @Test
    fun `should delegate clean cache calling`() =
        suspendedTestScenario(
            action = {
                expectedValue
            },
            assertions = { photo ->
                assertThat(photo.id).isEqualTo(1)
                assertThat(photo.tag).isEqualTo("cat")
                assertThat(photo.title).isEqualTo("title")
                assertThat(photo.thumbnailUrl).isEqualTo("thumb")
                assertThat(photo.sourceUrl).isEqualTo("source")
            }
        )
}