package com.backbase.lookup.business_structure

/**
 * Created by Backbase R&D B.V. on 2021-06-15.
 *
 * Configuration options for the Business Structure Journey. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class BusinessStructureConfiguration private constructor(
    val isOffline: Boolean,
    val createCaseActionName: String?,
    val requestBusinessStructureActionName: String,
    val submitBusinessStructureActionName: String,
    val requestCompanyLookupActionName: String,
    val submitCompanyDetailsActionName: String
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
        var createCaseActionName: String? = null

        @set:JvmSynthetic
        lateinit var requestBusinessStructureActionName: String

        @set:JvmSynthetic
        lateinit var submitBusinessStructureActionName: String

        @set:JvmSynthetic
        lateinit var requestCompanyLookupActionName: String

        @set:JvmSynthetic
        lateinit var submitCompanyDetailsActionName: String

        fun build() =
            BusinessStructureConfiguration(
                isOffline,
                createCaseActionName,
                requestBusinessStructureActionName,
                submitBusinessStructureActionName,
                requestCompanyLookupActionName,
                submitCompanyDetailsActionName
            )
    }

}

/**
 * DSL function to create a [AboutYouConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun businessStructureConfiguration(initializer: BusinessStructureConfiguration.Builder.() -> Unit): BusinessStructureConfiguration =
    BusinessStructureConfiguration.Builder().apply(initializer).build()
