package com.backbase.lookup.address

import com.backbase.deferredresources.DeferredText
import com.backbase.lookup.address.LookupAddressConfiguration.Builder
import com.backbase.lookup.address.models.AddressModel

/**
 * Created by Backbase R&D B.V. on 2010-05-29.
 *
 * Configuration options for the Lookup Address screen. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class LookupAddressConfiguration private constructor(
    var submitActionName: String,
    var fetchActionName: String = "",
    var prefillAddress: (()->AddressModel?)? =  null,
    var description: DeferredText? = null
){
    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {
        @set:JvmSynthetic
        lateinit var submitActionName: String

        @set:JvmSynthetic
        var fetchActionName: String = ""

        @set:JvmSynthetic
        var prefillAddress: (()->AddressModel?)? = null

        @set:JvmSynthetic
        var description: DeferredText? = null

        fun build() =
            LookupAddressConfiguration(submitActionName, fetchActionName, prefillAddress, description)
    }

}

/**
 * DSL function to create a [LookupAddressConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun LookupAddressConfiguration(initializer: Builder.() -> Unit): LookupAddressConfiguration =
    Builder().apply(initializer).build()

