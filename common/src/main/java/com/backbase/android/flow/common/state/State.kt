package com.backbase.android.flow.common.state

sealed class State<out T>(val result: T? = null) {
    object Idle : State<Nothing>()
    object Incomplete : State<Nothing>()
    object Working : State<Nothing>()
    class Failed(private val data: Exception) : State<java.lang.Exception>(data)
    class Success<T>(private val data: T? = null) : State<T>(data)
}