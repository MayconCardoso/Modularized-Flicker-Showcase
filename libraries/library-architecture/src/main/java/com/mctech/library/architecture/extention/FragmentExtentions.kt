package com.mctech.library.architecture.extention

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import br.com.unicred.feature.arq.ViewCommand
import com.mctech.library.architecture.BaseViewModel
import com.mctech.library.architecture.ComponentState
import com.mctech.library.architecture.SingleLiveEvent
import kotlinx.coroutines.launch

fun <T> Fragment.bindData(observable : LiveData<T>, block : (result : T) -> Unit) {
    lifecycleScope.launch {
        observable.observe(this@bindData, Observer {
            block(it)
        })
    }
}

fun <T> Fragment.bindState(observable : LiveData<ComponentState<T>>, block : (result : ComponentState<T>) -> Unit) {
    lifecycleScope.launch {
        observable.observe(this@bindState, Observer {
            block(it)
        })
    }
}


fun Fragment.bindCommand(viewModel: BaseViewModel, block: (result: ViewCommand) -> Unit) {
    lifecycleScope.launch { commandObserver(
        lifecycle   = this@bindCommand,
        viewModel   = viewModel,
        block       = block
    )}
}

private fun commandObserver(lifecycle: LifecycleOwner, viewModel: BaseViewModel, block: (result: ViewCommand) -> Unit) {
    ((viewModel.commandObservable) as SingleLiveEvent<ViewCommand>).observe(
        lifecycle::class.java.simpleName,
        lifecycle,
        Observer {
            block(it)
        }
    )
}