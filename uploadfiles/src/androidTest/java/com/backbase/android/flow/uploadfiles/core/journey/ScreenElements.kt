package com.backbase.android.flow.uploadfiles.core.journey

/**
 * Calls the specified function [block] with the given [screenElements] as its receiver.
 *
 * Similar to [with], but with no return value.
 */
inline fun <reified T> on(screenElements: T, block: T.() -> Unit) = with(screenElements) {
    block.invoke(this)
    Unit
}
