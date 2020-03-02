package com.mctech.showcase.feature.flicker_presentation

import androidx.lifecycle.viewModelScope
import com.mctech.library.architecture.ComponentState
import com.mctech.showcase.feature.flicker_domain.interactions.*
import com.mctech.testing.architecture.BaseViewModelTest
import com.mctech.testing.architecture.extentions.*
import com.mctech.testing.data_factory.FlickerPhotosDataFactory
import com.mctech.testing.data_factory.FlickerTagHistoryDataFactory
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PhotosViewModelTest : BaseViewModelTest(){
    private lateinit var viewModel: PhotosViewModel

    private val tag                             = "cat"
    private val expectedPhotosValue             = FlickerPhotosDataFactory.listOf(3)
    private val expectedTagsValue               = FlickerTagHistoryDataFactory.listOf(3)
    private val expectedException               = RuntimeException("Erro ao carregar os banners")

    private val loadFlickerPhotoCase            = mock<LoadFlickerPhotoCase>()
    private val loadNextPageOfFlickerPhotosCase = mock<LoadNextPageOfFlickerPhotosCase>()
    private val cleanFlickerPhotosCacheCase     = mock<CleanFlickerPhotosCacheCase>()
    private val saveTagCase                     = mock<SaveTagCase>()
    private val loadTagHistoryCase              = mock<LoadTagHistoryCase>()

    @Before
    fun `before each test`() {
        viewModel = PhotosViewModel(
            loadFlickerPhotoCase,
            loadNextPageOfFlickerPhotosCase,
            cleanFlickerPhotosCacheCase,
            saveTagCase,
            loadTagHistoryCase
        )
    }

    @After
    fun `after each test`() {
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `should initialize photos component`() {
        viewModel.photosComponent.test(
            assertion = {
                it.assertCount(1)
                it.assertFirst().isEqualTo(ComponentState.Initializing)
            }
        )
    }

    @Test
    fun `should initialize tag history component`() {
        viewModel.tagHistoryComponent.test(
            assertion = {
                it.assertCount(1)
                it.assertFirst().isEqualTo(ComponentState.Initializing)
            }
        )
    }

    @Test
    fun `should initialize current tag component`() {
        viewModel.tagComponent.test(
            assertion = {
                it.assertCount(1)
                it.assertFirst().isEqualTo(ComponentState.Initializing)
            }
        )
    }

    @Test
    fun `should display tag history success`() {
        viewModel.tagHistoryComponent.test(
            scenario = {
                whenever(loadTagHistoryCase.execute()).thenReturn(
                    Result.Success(
                        expectedTagsValue
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.LoadTagHistory)
            },
            assertion = {
                val successValue = it[2] as ComponentState.Success<List<String>>

                it.assertCount(3)
                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Loading.FromEmpty)
                it.assertAtPosition(2).isExactlyInstanceOf(ComponentState.Success::class.java)

                Assertions.assertThat(successValue.result).isEqualTo(expectedTagsValue)

                verify(loadTagHistoryCase, times(1)).execute()
            }
        )
    }

    @Test
    fun `should redirect user when tag history has loaded`() {
        viewModel.tagHistoryComponent.test(
            scenario = {
                whenever(loadTagHistoryCase.execute()).thenReturn(
                    Result.Success(
                        expectedTagsValue
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.LoadTagHistory)
            },
            assertion = {
                val successValue = (it[2] as ComponentState.Success<List<String>>).result

                // Check that the last tag has been updated.
                verify(saveTagCase, times(1)).execute(successValue.first())

                // Check the reset of photos component.
                viewModel.photosComponent.test { tagComponent ->
                    tagComponent.assertFirst().isEqualTo(ComponentState.Initializing)
                }

                // Check the current tag component
                viewModel.tagComponent.test { tagComponent ->
                    tagComponent.assertFirst().isExactlyInstanceOf(ComponentState.Success::class.java)

                    val lastTagSuccess = tagComponent[0] as ComponentState.Success<String>
                    Assertions.assertThat(lastTagSuccess.result).isEqualTo(successValue.first())
                }
            }
        )
    }

    @Test
    fun `should display tag history error`() {
        viewModel.tagHistoryComponent.test(
            scenario = {
                whenever(loadTagHistoryCase.execute()).thenReturn(
                    Result.Failure(
                        expectedException
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.LoadTagHistory)
            },
            assertion = {
                val errorValue = it[2] as ComponentState.Error

                it.assertCount(3)
                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Loading.FromEmpty)
                it.assertAtPosition(2).isExactlyInstanceOf(ComponentState.Error::class.java)

                Assertions.assertThat(errorValue.reason).isEqualTo(expectedException)

                verify(loadTagHistoryCase, times(1)).execute()
            }
        )
    }

    @Test
    fun `should change current tag`() {
        viewModel.tagComponent.test(
            scenario = {
                whenever(loadTagHistoryCase.execute()).thenReturn(
                    Result.Success(
                        expectedTagsValue
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.SearchTag(tag))
            },
            assertion = {
                val successValue = (it[1] as ComponentState.Success<String>).result

                // Check that the last tag has been updated.
                verify(saveTagCase, times(1)).execute(successValue)

                // Check the reset of photos component.
                viewModel.photosComponent.test { tagComponent ->
                    tagComponent.assertFirst().isEqualTo(ComponentState.Initializing)
                }

                // Check update tag history
                verify(loadTagHistoryCase, times(1)).execute()
            }
        )
    }

    @Test
    fun `should refresh photos data`() {
        viewModel.photosComponent.test(
            scenario = {
                whenever(loadFlickerPhotoCase.execute(any())).thenReturn(
                    Result.Success(
                        expectedPhotosValue
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.SearchTag(tag))
                viewModel.interact(PhotosViewInteraction.RefreshData)
            },
            assertion = {
                val successValue = it[3] as ComponentState.Success<PhotosState>

                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(2).isEqualTo(ComponentState.Loading.FromEmpty)
                it.assertAtPosition(3).isExactlyInstanceOf(ComponentState.Success::class.java)

                Assertions.assertThat(successValue.result.photos).isEqualTo(expectedPhotosValue)
                Assertions.assertThat(successValue.result.moveToTop).isEqualTo(true)

                verify(cleanFlickerPhotosCacheCase, times(1)).execute(tag)
                verify(loadFlickerPhotoCase, times(1)).execute(tag)
            }
        )
    }

    @Test
    fun `should load first page of photos`() {
        viewModel.photosComponent.test(
            scenario = {
                whenever(loadFlickerPhotoCase.execute(any())).thenReturn(
                    Result.Success(
                        expectedPhotosValue
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.SearchTag(tag))
                viewModel.interact(PhotosViewInteraction.LoadFirstPage)
            },
            assertion = {
                val successValue = it[3] as ComponentState.Success<PhotosState>

                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Initializing) // Restart component
                it.assertAtPosition(2).isEqualTo(ComponentState.Loading.FromEmpty)
                it.assertAtPosition(3).isExactlyInstanceOf(ComponentState.Success::class.java)

                Assertions.assertThat(successValue.result.photos).isEqualTo(expectedPhotosValue)
                Assertions.assertThat(successValue.result.moveToTop).isEqualTo(true)

                verify(loadFlickerPhotoCase, times(1)).execute(tag)
            }
        )
    }

    @Test
    fun `should load next page of photos`() {
        viewModel.photosComponent.test(
            scenario = {
                whenever(loadNextPageOfFlickerPhotosCase.execute(any())).thenReturn(
                    Result.Success(
                        expectedPhotosValue
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.SearchTag(tag))
                viewModel.interact(PhotosViewInteraction.LoadNextPage)
            },
            assertion = {
                val successValue = it[3] as ComponentState.Success<PhotosState>

                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Initializing) // Restart component
                it.assertAtPosition(2).isEqualTo(ComponentState.Loading.FromEmpty)
                it.assertAtPosition(3).isExactlyInstanceOf(ComponentState.Success::class.java)

                Assertions.assertThat(successValue.result.photos).isEqualTo(expectedPhotosValue)
                Assertions.assertThat(successValue.result.moveToTop).isEqualTo(false)

                verify(loadNextPageOfFlickerPhotosCase, times(1)).execute(tag)
            }
        )
    }

    @Test
    fun `should show correct state when loading next page`() {
        viewModel.photosComponent.test(
            scenario = {
                whenever(loadFlickerPhotoCase.execute(any())).thenReturn(
                    Result.Success(
                        expectedPhotosValue
                    )
                )
                whenever(loadNextPageOfFlickerPhotosCase.execute(any())).thenReturn(
                    Result.Success(
                        expectedPhotosValue
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.SearchTag(tag))
                viewModel.interact(PhotosViewInteraction.LoadFirstPage)
                viewModel.interact(PhotosViewInteraction.LoadNextPage)
            },
            assertion = {
                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Initializing) // Restart component
                it.assertAtPosition(2).isEqualTo(ComponentState.Loading.FromEmpty)
                it.assertAtPosition(3).isExactlyInstanceOf(ComponentState.Success::class.java)
                it.assertAtPosition(4).isEqualTo(ComponentState.Loading.FromData)
                it.assertAtPosition(5).isExactlyInstanceOf(ComponentState.Success::class.java)
            }
        )
    }

    @Test
    fun `should show error while loading photos`() {
        viewModel.photosComponent.test(
            scenario = {
                whenever(loadFlickerPhotoCase.execute(any())).thenReturn(
                    Result.Failure(
                        expectedException
                    )
                )
            },
            action = {
                viewModel.interact(PhotosViewInteraction.SearchTag(tag))
                viewModel.interact(PhotosViewInteraction.LoadFirstPage)
            },
            assertion = {
                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Initializing) // Restart component
                it.assertAtPosition(2).isEqualTo(ComponentState.Loading.FromEmpty)
                it.assertAtPosition(3).isExactlyInstanceOf(ComponentState.Error::class.java)
            }
        )
    }
}