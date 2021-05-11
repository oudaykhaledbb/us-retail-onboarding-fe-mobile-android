@file:JvmName("UiThreadActions")

package com.backbase.android.flow.ssn.core

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers
import java.util.concurrent.TimeUnit

/**
 * Wait for the UI thread until it is idle.
 */
fun waitForUiThread() = InstrumentationRegistry.getInstrumentation().waitForIdleSync()

/**
 * Wait for the UI thread until it is idle after at least [time] [unit]s. This function should be used sparingly; try
 * [waitForUiThread] without arguments first.
 *
 * This may be necessary if, for some reason, the view logic you want to test is not executing until
 * after the main thread goes idle. It's hacky, but a bit less so than calling [Thread.sleep].
 */
fun waitForUiThread(time: Int, unit: TimeUnit): ViewInteraction =
    Espresso.onView(ViewMatchers.isRoot()).perform(loopMainThread(time, unit))

private fun loopMainThread(time: Int, unit: TimeUnit) = object : ViewAction {
    override fun getConstraints() = Matchers.any(View::class.java)

    override fun getDescription() = "loop main thread for ${toMilliseconds(time, unit)} milliseconds"

    override fun perform(uiController: UiController, view: View) =
        uiController.loopMainThreadForAtLeast(toMilliseconds(time, unit))

    private fun toMilliseconds(time: Int, unit: TimeUnit) = TimeUnit.MILLISECONDS.convert(time.toLong(), unit)
}
