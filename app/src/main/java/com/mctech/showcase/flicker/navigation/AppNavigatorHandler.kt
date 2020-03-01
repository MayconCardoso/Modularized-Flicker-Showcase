package com.mctech.showcase.flicker.navigation

import androidx.navigation.NavController
import com.mctech.showcase.feature.flicker_presentation.PhotosNavigation
import com.mctech.showcase.flicker.R

class AppNavigatorHandler : PhotosNavigation {

    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        navController = null
    }

    override fun navigateToPhotosList() {
        navController?.navigate(
            R.id.action_searchFragment_to_photosFragment
        )
    }
}