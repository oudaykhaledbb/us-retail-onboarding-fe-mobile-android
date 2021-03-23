package com.backbase.android.flow.smeo.aboutyou

import android.os.Looper
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


private val backbaseModule = module {
    factory {
        //BBIdentityAuthClient constructor creates a Handler so calling thread needs to prepare looper.
        if (Looper.myLooper() == null) Looper.prepare()
    }
}

private val aboutYouConfigurationModule = module {

    factory {
        AboutYouConfiguration {
            isOffline = true
            actionInit = "sme-onboarding-init"
            actionAboutYouSubmit = "sme-onboarding-anchor-data"
        }
    }
}

/**
 * Provide Backbase specific dependencies for tests, which are otherwise provided from application level.
 */
fun provideBackbaseDependencies() = loadKoinModules(backbaseModule)

/**
 * Provide Backbase specific dependencies for tests, which are otherwise provided from application level.
 */
fun provideAboutYouConfiguration() = loadKoinModules(aboutYouConfigurationModule)
