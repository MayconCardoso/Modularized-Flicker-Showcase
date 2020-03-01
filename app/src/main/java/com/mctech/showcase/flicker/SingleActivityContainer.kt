package com.mctech.showcase.flicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import com.mctech.showcase.feature.flicker_presentation.PhotosNavigation
import org.koin.android.ext.android.inject

class SingleActivityContainer : AppCompatActivity() {

    private val navigator : PhotosNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        setupActionBarWithNavController(getNavigationController())
    }

    override fun onStart() {
        super.onStart()
        navigator.bind(getNavigationController())
    }

    override fun onStop() {
        navigator.unbind()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavigationController().navigateUp()
    }

    private fun getNavigationController() =
        Navigation.findNavController(this, R.id.nav_host_fragment)
}
