package com.backbase.android.flow.identityverification

import com.jumio.core.enums.JumioDataCenter


/**
 * Created by Backbase R&D B.V. on 2010-09-17.
 *
 * Configuration options for the ID&V screens. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class IdentityVerificationConfiguration private constructor(
    val apiToken: String,
    val apiSecretKey: String,
    val dataCenter: JumioDataCenter,
    val initiationActionName: String = "",
    val verificationActionName: String = "",
) {

    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {

        @set:JvmSynthetic
        lateinit var apiToken: String

        @set:JvmSynthetic
        lateinit var apiSecretKey: String

        @set:JvmSynthetic
        lateinit var dataCenter: DocScannerDataCenter

        @set:JvmSynthetic
        lateinit var initiationActionName: String

        @set:JvmSynthetic
        lateinit var verificationActionName: String

        fun build() =
            IdentityVerificationConfiguration(
                apiToken,
                apiSecretKey,
                when(dataCenter){
                    DocScannerDataCenter.EU -> JumioDataCenter.EU
                    DocScannerDataCenter.US -> JumioDataCenter.US
                    DocScannerDataCenter.SG -> JumioDataCenter.SG
                },
                initiationActionName,
                verificationActionName
            )
    }

}

/**
 * DSL function to create a [IdentityVerificationConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun IdentityVerificationConfiguration(initializer: IdentityVerificationConfiguration.Builder.() -> Unit): IdentityVerificationConfiguration =
    IdentityVerificationConfiguration.Builder().apply(initializer).build()

enum class DocScannerDataCenter {
    EU,
    US,
    SG
}