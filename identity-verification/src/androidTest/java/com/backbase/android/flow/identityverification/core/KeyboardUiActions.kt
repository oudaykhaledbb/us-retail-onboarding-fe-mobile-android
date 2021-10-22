package com.backbase.android.flow.identityverification.core

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.Espresso.pressBack
import androidx.test.platform.app.InstrumentationRegistry

/**
 * Returns a boolean value, based on the visibility of keyboard
 */
fun isKeyboardShown(): Boolean {
    val inputMethodManager =
        InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputMethodManager.isAcceptingText
}

/**
 * This function performs press back navigation and works on the screens where keyboard is open and pressing back can just close keyboard instead of navigating back
 */
fun checkForKeyboardOpenAndPressBack() {
    if (isKeyboardShown()){
        pressBack()
        waitForUiThread()
    }

    pressBack()
    waitForUiThread()
}
