package com.backbase.android.flow.productselector

import com.backbase.android.flow.productselector.ProductSelectorConfiguration.Builder

/**
 * Created by Backbase R&D B.V. on 2020-11-17
 *
 * Configuration options for the ProductSelector screen. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 * @constructor
 */
class ProductSelectorConfiguration private constructor(
        var imageBaseUrl: String,
        var createCaseAction: String,
        var requestProductsAction: String,
        var submitProductsAction: String,
        var selectionType: SelectionType? = SelectionType.SINGLE,
        var hideHelperLink: Boolean = false
) {

    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {

        @set:JvmSynthetic
        lateinit var imageBaseUrl: String

        @set:JvmSynthetic
        lateinit var createCaseAction: String

        @set:JvmSynthetic
        lateinit var requestProductsAction: String

        @set:JvmSynthetic
        lateinit var submitProductAction: String

        @set:JvmSynthetic
        lateinit var selectionType: SelectionType

        @set:JvmSynthetic
        var hideHelperLink: Boolean = false

        fun build() =
                ProductSelectorConfiguration(
                        imageBaseUrl,
                        createCaseAction,
                        requestProductsAction,
                        submitProductAction,
                        selectionType,
                        hideHelperLink
                )
    }

}

/**
 * DSL function to create a [ProductSelectorConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun ProductSelectorConfiguration(initializer: ProductSelectorConfiguration.Builder.() -> Unit): ProductSelectorConfiguration =
    ProductSelectorConfiguration.Builder()
        .apply(initializer).build()

enum class SelectionType { MULTI, SINGLE}