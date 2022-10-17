package com.irv205.gamechallenge

sealed class MainViewState {
    object ViewCoin : MainViewState()
    object ViewDice : MainViewState()
}