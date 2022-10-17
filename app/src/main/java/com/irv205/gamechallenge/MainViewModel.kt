package com.irv205.gamechallenge

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _containerState = mutableStateOf<MainViewState>(MainViewState.ViewCoin)
    val containerState : State<MainViewState> get() = _containerState

    fun switchViews(view: MainViewState) {
        _containerState.value = view
    }
}