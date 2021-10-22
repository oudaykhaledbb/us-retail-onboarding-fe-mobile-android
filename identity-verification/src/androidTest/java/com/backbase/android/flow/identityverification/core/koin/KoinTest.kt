package com.backbase.android.flow.identityverification.core.koin

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

/**
 * Created by Backbase R&D B.V. on 2020-01-03.
 *
 * A base test class for testing things that require Koin to work (like Journeys and screens).
 */
abstract class KoinTest {

    /**
     * Shortcut to a context from the target application. Useful for instantiating configurations.
     */
    protected val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    /**
     * Start Koin before each test. Optionally implement [onKoinStarted] to load Koin modules once Koin has started.
     *
     * Stops previous instances of Koin before starting the new one. [stopKoin] is called here and intentionally *not*
     * called in an @After function to avoid race conditions with Journey fragments that unload Koin modules in their
     * onDestroy functions.
     */
    @Before fun startKoin() {
        stopKoin()
        startKoin {
            androidContext(targetContext.applicationContext)
            onKoinStarted()
        }
    }

    /**
     * Hook for providing additional dependencies before tests.
     */
    open fun onKoinStarted() = Unit
}
