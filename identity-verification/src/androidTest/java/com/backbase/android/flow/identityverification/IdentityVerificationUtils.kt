package com.backbase.android.flow.identityverification

import android.os.Looper
import com.backbase.android.flow.identityverification.DocScannerDataCenter
import com.backbase.android.flow.identityverification.IdentityVerificationConfiguration
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val backbaseModule = module {
    factory {
        //BBIdentityAuthClient constructor creates a Handler so calling thread needs to prepare looper.
        if (Looper.myLooper() == null) Looper.prepare()
    }
}

private val identityVerificationConfigurationModule = module {
    factory {
        IdentityVerificationConfiguration {
            apiToken = ""
            apiSecretKey = ""
            dataCenter = DocScannerDataCenter.EU
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
fun provideIdentityVerificationConfiguration() = loadKoinModules(identityVerificationConfigurationModule)