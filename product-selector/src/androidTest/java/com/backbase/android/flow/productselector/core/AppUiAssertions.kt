package com.backbase.android.flow.productselector.core

import androidx.test.espresso.NoActivityResumedException
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

/**
 * Verify that [action] causes an [E] to be thrown. Optionally provide a [message] to verify the exception's message.
 */
inline fun <reified E : Exception> expect(message: String? = null, action: () -> Unit) {
    try {
        action()
        fail("Expected exception: <${E::class}>")
    } catch (exception: Exception) {
        assertTrue("Unexpected exception, expected <${E::class}> but was <${exception.javaClass}>", exception is E)
        if (message != null)
            assertTrue(
                "Unexpected exception message, expected to contain <$message> but was <${exception.message}>",
                exception.message?.contains(message) == true
            )
    }
}

/**
 * Verify that [action] causes the app under test to be killed. Optionally provide a [message] to verify the reason for
 * the app's death.
 */
inline fun expectAppKilled(message: String? = null, action: () -> Unit) =
    expect<NoActivityResumedException>(message, action)
