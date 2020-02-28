package com.mctech.library.architecture

import androidx.navigation.NavController

interface Navigation {
    fun bind(navController: NavController)
    fun unbind()
}