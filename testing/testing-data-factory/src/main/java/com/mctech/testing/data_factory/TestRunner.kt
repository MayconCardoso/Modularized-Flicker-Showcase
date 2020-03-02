package com.mctech.testing.data_factory

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
fun <T> testScenario(
        scenario: () -> Unit = {},
        action: () -> T,
        assertions: (result: T) -> Unit
) = runBlockingTest {
    scenario.invoke()
    assertions.invoke( action.invoke() )
}

@ExperimentalCoroutinesApi
fun <T> suspendedTestScenario(
        scenario: suspend () -> Unit = {},
        action: suspend () -> T,
        assertions: suspend (result: T) -> Unit
) = runBlockingTest {
    scenario.invoke()
    assertions.invoke( action.invoke() )
}