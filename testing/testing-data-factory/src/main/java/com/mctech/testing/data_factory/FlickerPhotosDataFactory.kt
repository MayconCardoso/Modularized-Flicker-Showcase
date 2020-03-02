package com.mctech.testing.data_factory

import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto

object FlickerPhotosDataFactory {
    fun listOf(count: Int = 0): List<FlickerPhoto> {
        val list = mutableListOf<FlickerPhoto>()
        for (x in 0 until count) {
            list.add(single())
        }
        return list
    }

    fun single(
        id: Long = 0,
        tag: String = "",
        title: String = "",
        thumbnailUrl: String = "",
        sourceUrl: String = ""
    ) = FlickerPhoto(
        id,
        tag,
        title,
        thumbnailUrl,
        sourceUrl
    )

}