package com.mctech.showcase.feature.flicker_presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mctech.library.architecture.BaseViewModel
import com.mctech.library.architecture.ComponentState
import com.mctech.library.architecture.UserInteraction
import com.mctech.library.architecture.components.PaginatedComponent
import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto
import com.mctech.showcase.feature.flicker_domain.interactions.CleanFlickerPhotosCacheCase
import com.mctech.showcase.feature.flicker_domain.interactions.LoadFlickerPhotoCase
import com.mctech.showcase.feature.flicker_domain.interactions.LoadNextPageOfFlickerPhotosCase
import com.mctech.showcase.feature.flicker_domain.interactions.Result
import kotlinx.coroutines.launch

/**
 * @author MAYCON CARDOSO on 2020-02-29.
 */
class PhotosViewModel(
    private val loadFlickerPhotoCase                : LoadFlickerPhotoCase,
    private val loadNextPageOfFlickerPhotosCase     : LoadNextPageOfFlickerPhotosCase,
    private val cleanFlickerPhotosCacheCase         : CleanFlickerPhotosCacheCase
) : BaseViewModel(){
    // The active tag on screen
    private var currentTag : String? = "beach"

    // To control the pagination.
    private var photosPagination = PaginatedComponent<FlickerPhoto>()

    // The state of photo UI component
    private val _photosComponent = MutableLiveData<ComponentState<PhotosState>>(ComponentState.Initializing)
    val photosComponent: LiveData<ComponentState<PhotosState>> = _photosComponent

    // The state of search UI component
    private val _tagComponent = MutableLiveData<ComponentState<String>>(ComponentState.Success(currentTag!!))
    val tagComponent: LiveData<ComponentState<String>> = _tagComponent


    override suspend fun handleUserInteraction(interaction: UserInteraction) {
        when(interaction){
            is PhotosViewInteraction.SearchTag      -> changeTagInteraction(interaction.tag)
            is PhotosViewInteraction.RefreshData    -> refreshDataInteraction()
            is PhotosViewInteraction.LoadFirstPage  -> loadFirstPageOfPhotosInteraction()
            is PhotosViewInteraction.LoadNextPage   -> loadNextPageOfPhotosInteraction()
        }
    }

    private suspend fun changeTagInteraction(tag: String) {
        // Save new tag
        _tagComponent.value = ComponentState.Success(tag)
        currentTag = tag

        // Clean UI list
        _photosComponent.value = ComponentState.Success(PhotosState(mutableListOf(), true))

        // Load first page of new tag
        loadFirstPageOfPhotosInteraction()
    }

    private suspend fun refreshDataInteraction() {
        currentTag?.let { tag ->

            // Clean the cache of this tag.
            cleanFlickerPhotosCacheCase.execute(tag)

            // Call the first page again.
            loadFirstPageOfPhotosInteraction()
        }
    }

    private suspend fun loadFirstPageOfPhotosInteraction() = internalPhotosFetcher(true, mutableListOf()) { tag ->
        loadFlickerPhotoCase.execute(tag)
    }


    private fun loadNextPageOfPhotosInteraction() {
        synchronized(photosPagination.isLoadingNextPage) {
            if (photosPagination.isLoadingNextPage) return

            photosPagination.isLoadingNextPage = true

            viewModelScope.launch{
                internalPhotosFetcher(false, photosPagination.currentList) { tag ->
                    loadNextPageOfFlickerPhotosCase.execute(tag)
                }
            }
        }
    }

    private suspend fun internalPhotosFetcher(
        moveListToTop: Boolean,
        currentList: List<FlickerPhoto>,
        loaderAgent: suspend (tag : String) -> Result<List<FlickerPhoto>>
    ) {
        currentTag?.let { tag ->
            // Show loading on component.
            _photosComponent.value = photosPagination.loadingState()

            // Fetch photos by calling the use case.
            when (val photosResult = loaderAgent.invoke(tag)) {

                // Success when fetching photos
                is Result.Success -> {
                    // Merge the lists in order to add the next page on the previous one.
                    photosPagination.currentList = mutableListOf<FlickerPhoto>().apply {
                        addAll(currentList)
                        addAll(photosResult.result)
                    }

                    // Update screen
                    _photosComponent.value = ComponentState.Success(
                        PhotosState(
                            photosPagination.currentList,
                            moveListToTop
                        )
                    )
                }

                // Failure when fetching photos
                is Result.Failure -> photosResult.throwable.apply {
                    logger.e(message.orEmpty(), this)
                    _photosComponent.value = ComponentState.Error(this)
                }
            }

            // Release the loading control block.
            synchronized(photosPagination.isLoadingNextPage) {
                photosPagination.isLoadingNextPage = false
            }
        }
    }
}