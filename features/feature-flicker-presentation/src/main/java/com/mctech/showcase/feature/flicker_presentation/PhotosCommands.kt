package com.mctech.showcase.feature.flicker_presentation

import br.com.unicred.feature.arq.ViewCommand

sealed class PhotosCommands : ViewCommand{
    object NavigateToPhotos : PhotosCommands()
}