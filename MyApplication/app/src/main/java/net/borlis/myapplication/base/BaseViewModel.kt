package net.borlis.myapplication.base

import androidx.lifecycle.ViewModel
import net.borlis.myapplication.sideeffects.SideEffect
import net.borlis.myapplication.sideeffects.SideEffectConsumer

abstract class BaseViewModel<UiModel> : ViewModel() {
    val consumers = mutableSetOf<SideEffectConsumer>()

    fun registerConsumer(consumer: SideEffectConsumer) {
        consumers += consumer
    }

    fun unregisterConsumer(consumer: SideEffectConsumer) {
        consumers -= consumer
    }

    protected fun SideEffect?.post() {
        if (this == null) return
        postSideEffect(this)
    }

    private fun postSideEffect(sideEffect: SideEffect) {
        consumers.forEach {
            it.onSideEffectReceived(sideEffect)
        }
    }
}
