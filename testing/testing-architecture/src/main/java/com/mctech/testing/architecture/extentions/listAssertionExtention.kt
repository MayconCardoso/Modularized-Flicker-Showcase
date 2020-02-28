package com.mctech.testing.architecture.extentions

import org.assertj.core.api.Assertions

fun <T> List<T>.assertEmpty() = assertCount(0)
fun <T> List<T>.assertCount(count : Int) = Assertions.assertThat(size).isEqualTo(count)
fun <T> List<T>.assertAtPosition(position : Int) = Assertions.assertThat(get(position))
fun <T> List<T>.assertFirst() = assertAtPosition(0)
fun <T> List<T>.assertLast() = assertAtPosition(size - 1)
