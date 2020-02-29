package com.mctech.showcase.feature.flicker_presentation

import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto

data class PhotosState(
    val photos: List<FlickerPhoto>,
    val moveToTop: Boolean
)