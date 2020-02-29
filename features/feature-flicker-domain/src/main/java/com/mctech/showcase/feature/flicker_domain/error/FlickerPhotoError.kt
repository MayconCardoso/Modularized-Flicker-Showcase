package com.mctech.showcase.feature.flicker_domain.error

/**
 * @author MAYCON CARDOSO on 2020-02-29.
 */
sealed class FlickerPhotoError(message : String) : RuntimeException(message){
    object UnknownException : FlickerPhotoError("It was not possible to load your photos at the moment.")
}