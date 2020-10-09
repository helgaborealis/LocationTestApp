package net.borlis.myapplication.sideeffects

interface SideEffectConsumer {
    fun onSideEffectReceived(sideEffect: SideEffect)
}