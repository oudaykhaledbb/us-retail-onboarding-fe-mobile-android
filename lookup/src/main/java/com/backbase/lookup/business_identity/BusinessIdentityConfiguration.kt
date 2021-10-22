package com.backbase.lookup.business_identity

import com.backbase.lookup.business_structure.module.BusinessDetailsResponseModel


/**
 * Created by Backbase R&D B.V. on 2021-03-03.
 *
 * Configuration options for the Business Identity Journey. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class BusinessIdentityConfiguration private constructor(
    val isOffline: Boolean,
    val submitBusinessIdentityActionName: String,
    val businessDetails: BusinessDetailsResponseModel?
) {

    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {

        @set:JvmSynthetic
        var isOffline: Boolean = false

        @set:JvmSynthetic
        lateinit var submitBusinessIdentityActionName: String

        @set:JvmSynthetic
        var businessDetails: BusinessDetailsResponseModel? = null

        fun build() =
            BusinessIdentityConfiguration(isOffline, submitBusinessIdentityActionName, businessDetails)
    }

}

/**
 * DSL function to create a [AboutYouConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun businessIdentityConfiguration(initializer: BusinessIdentityConfiguration.Builder.() -> Unit): BusinessIdentityConfiguration =
    BusinessIdentityConfiguration.Builder().apply(initializer).build()
