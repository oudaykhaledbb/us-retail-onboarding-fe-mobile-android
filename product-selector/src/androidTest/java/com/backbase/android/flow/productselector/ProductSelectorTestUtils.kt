package com.backbase.android.flow.productselector

import android.os.Looper
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.net.URI

private val backbaseModule = module {
        factory {
            //BBIdentityAuthClient constructor creates a Handler so calling thread needs to prepare looper.
            if (Looper.myLooper() == null) Looper.prepare()
        }
    }

    private val productSelectorConfigurationModule = module {
        factory {
            return@factory ProductSelectorConfiguration {
                imageBaseUrl = ""
                requestProductsAction = "get-product-list"
                submitProductAction = "select-products"
                createCaseAction = ""
                selectionType = SelectionType.SINGLE
                hideHelperLink = false
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
    fun provideProductSelectorConfigurationModule() {
        loadKoinModules(productSelectorConfigurationModule)
    }

