package com.mctech.testing.data_factory

object FlickerTagHistoryDataFactory {
    fun listOf(count: Int = 0): List<String> {
        val list = mutableListOf<String>()
        for (x in 0 until count) {
            list.add(single())
        }
        return list
    }

    fun single(tag: String = "") = tag
}