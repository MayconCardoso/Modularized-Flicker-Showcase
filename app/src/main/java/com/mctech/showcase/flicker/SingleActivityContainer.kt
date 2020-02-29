package com.mctech.showcase.flicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

class SingleActivityContainer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavigationController().navigateUp()
    }

    private fun getNavigationController() =
        Navigation.findNavController(this, R.id.nav_host_fragment)
}
