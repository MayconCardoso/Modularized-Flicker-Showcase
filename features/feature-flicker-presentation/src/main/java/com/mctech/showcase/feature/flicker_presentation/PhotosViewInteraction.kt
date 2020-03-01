package com.mctech.showcase.feature.flicker_presentation

import com.mctech.library.architecture.UserInteraction

sealed class PhotosViewInteraction : UserInteraction {
    object RefreshData : PhotosViewInteraction()
    object LoadFirstPage : PhotosViewInteraction()
    object LoadNextPage : PhotosViewInteraction()

    object LoadTagHistory : PhotosViewInteraction()
    data class SearchTag(val tag: String) : PhotosViewInteraction()
}