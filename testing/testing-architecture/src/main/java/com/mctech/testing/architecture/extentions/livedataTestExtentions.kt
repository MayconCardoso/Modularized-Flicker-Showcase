package com.mctech.testing.architecture.extentions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun <T> LiveData<T>.collectValuesForTesting(assertion: (List<T>) -> Unit) {
    test { assertion(it) }
}

fun <T> LiveData<T>.test(
    scenario: suspend () -> Unit = {},
    action: () -> Unit = {},
    assertion: suspend (List<T>) -> Unit
) {
    val emittedValues = mutableListOf<T>()
    val observer = Observer<T> {
        emittedValues.add(it)
    }

    try {
        runBlocking {
            scenario()
            observeForever(observer)
            action()
            withContext(Dispatchers.Default) {
                assertion(emittedValues)
            }
        }
    } finally {
        removeObserver(observer)
    }
}

fun <T> LiveData<T>.assertNoValue() {
    collectValuesForTesting { it.assertEmpty() }
}