package com.mctech.showcase.feature.flicker_domain.entity

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
data class FlickerPhoto(
    val id : Long,
    val tag : String,
    val title : String,
    val thumbnailUrl : String,
    val sourceUrl : String
)