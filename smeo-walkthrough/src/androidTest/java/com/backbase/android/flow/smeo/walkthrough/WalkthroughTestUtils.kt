package com.backbase.android.flow.smeo.walkthrough

import android.os.Looper
import com.backbase.android.flow.smeo.walkthrough.ui.WalkthroughPageBuilder
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredText
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


private val backbaseModule = module {
    factory {
        //BBIdentityAuthClient constructor creates a Handler so calling thread needs to prepare looper.
        if (Looper.myLooper() == null) Looper.prepare()
    }
}

private val walkthroughConfigurationModule = module {

    factory {
        walkthroughConfiguration {
            pages = arrayListOf(firstPage(), secondPage())
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
fun provideWalkthroughConfiguration() = loadKoinModules(walkthroughConfigurationModule)

fun firstPage() = WalkthroughPageBuilder()
    .setHeaderImage(DeferredDrawable.Resource(R.drawable.backbase_ic_atm))
    .addHeader(DeferredText.Constant("Header"))
    .addContent(DeferredText.Constant("Content"))
    .addCaption(DeferredText.Constant("Caption"))
    .addSupport(DeferredText.Constant("Support"))
    .addHeaderWithIcon(DeferredDrawable.Resource(R.drawable.backbase_ic_atm), DeferredText.Constant("HeaderWithIcon"))
    .build()

fun secondPage() = WalkthroughPageBuilder()
    .setHeaderImage(DeferredDrawable.Resource(R.drawable.backbase_ic_atm))
    .addHeader(DeferredText.Constant("HeaderTitle2"))
    .build()