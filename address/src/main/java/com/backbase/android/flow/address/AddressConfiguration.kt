package com.backbase.android.flow.address

import com.backbase.android.flow.address.AddressConfiguration.Builder
import com.backbase.android.flow.address.models.AddressModel
import com.backbase.deferredresources.DeferredText

/**
 * Created by Backbase R&D B.V. on 2010-05-29.
 *
 * Configuration options for the Address screen. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class AddressConfiguration private constructor(
    var submitActionName: String,
    var fetchActionName: String = "",
    var prefillAddress: AddressModel? =  null,
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
        var prefillAddress: AddressModel? = null

        @set:JvmSynthetic
        var description: DeferredText? = null

        fun build() =
            AddressConfiguration(submitActionName, fetchActionName, prefillAddress, description)
    }

}

/**
 * DSL function to create a [AddressConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun AddressConfiguration(initializer: Builder.() -> Unit): AddressConfiguration =
    Builder().apply(initializer).build()


