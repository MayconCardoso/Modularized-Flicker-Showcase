package com.mctech.library.architecture.components

import com.mctech.library.architecture.ComponentState


class PaginatedComponent<T> {
    var currentList = mutableListOf<T>()
    var isLoadingNextPage = false

    fun loadingState() : ComponentState.Loading {
        return if(currentList.isEmpty())
            ComponentState.Loading.FromEmpty
        else
            ComponentState.Loading.FromData
    }
}