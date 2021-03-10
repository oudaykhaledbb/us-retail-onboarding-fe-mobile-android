package com.backbase.android.flow.smeo

import com.backbase.android.flow.smeo.walkthrough.walkthroughConfiguration
import org.koin.dsl.module

/**
 * Koin module defining the app-level Authentication Journey configurations.
 */
val applicationModule = module {

    factory {
        walkthroughConfiguration {
            pages = arrayListOf(welcomePage(), requirementsPage(), informationPage(), businessNotAllowedPage(), termsAndConditionsPages())
        }
    }
}