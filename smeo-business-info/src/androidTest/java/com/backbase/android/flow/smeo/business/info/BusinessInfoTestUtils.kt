package com.backbase.android.flow.smeo.business.info

import android.os.Looper
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


private val backbaseModule = module {
    factory {
        //BBIdentityAuthClient constructor creates a Handler so calling thread needs to prepare looper.
        if (Looper.myLooper() == null) Looper.prepare()
    }
}

private val businessInfoConfigurationModule = module {
    factory {
        businessInfoConfiguration {
            isOffline = true
            submitBusinessDetailsAction = ""
            verifyCaseAction = ""
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
fun provideBusinessInfoConfiguration() = loadKoinModules(businessInfoConfigurationModule)
