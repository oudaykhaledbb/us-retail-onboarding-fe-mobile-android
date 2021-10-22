package com.backbase.android.flow.businessrelations

/**
 * Created by Backbase R&D B.V. on 2021-02-08
 *
 * Configuration options for the BusinessRelationsConfiguration screen. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 * @constructor
 */
class BusinessRelationsConfiguration private constructor(
    var isOffline: Boolean,
    var createCaseActionName: String?,
    var submitRelationTypeActionName: String,
    var updateOwnerActionName: String,
    var updateCurrentUserOwnerActionName: String,
    var updateCurrentUserControlPersonActionName: String,
    var deleteOwnerActionName: String,
    var updateControlPersonActionName: String,
    var deleteControlPersonActionName: String,
    var requestBusinessPersonsActionName: String,
    var submitControlPersonActionName: String,
    var requestBusinessRolesActionName: String,
    var completeOwnersStepActionName: String,
    var completeControlPersonStepActionName: String,
    var completeSummaryStepActionName: String,
    var userInfoProvider: UserInfoProvider,
    var enableCurrentUserEditing: Boolean

) {

    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {

        @set: JvmSynthetic
        var isOffline: Boolean = false

        @set: JvmSynthetic
        var createCaseActionName: String? = null

        @set: JvmSynthetic
        lateinit var submitRelationTypeActionName: String

        @set: JvmSynthetic
        lateinit var updateOwnerActionName: String

        @set: JvmSynthetic
        lateinit var updateCurrentUserOwnerActionName: String

        @set: JvmSynthetic
        lateinit var updateCurrentUserControlPersonActionName: String

        @set: JvmSynthetic
        lateinit var deleteOwnerActionName: String

        @set: JvmSynthetic
        lateinit var updateControlPersonActionName: String

        @set: JvmSynthetic
        lateinit var deleteControlPersonActionName: String

        @set: JvmSynthetic
        lateinit var requestBusinessPersonsActionName: String

        @set: JvmSynthetic
        lateinit var submitControlPersonActionName: String

        @set: JvmSynthetic
        lateinit var requestBusinessRolesActionName: String

        @set: JvmSynthetic
        lateinit var completeSummaryStepActionName: String

        @set: JvmSynthetic
        lateinit var completeControlPersonStepActionName: String

        @set: JvmSynthetic
        lateinit var completeOwnersStepActionName: String

        @set: JvmSynthetic
        lateinit var userInfoProvider: UserInfoProvider

        @set: JvmSynthetic
        var enableCurrentUserEditing: Boolean = false

        fun build() =
            BusinessRelationsConfiguration(
                isOffline,
                createCaseActionName,
                submitRelationTypeActionName,
                updateOwnerActionName,
                updateCurrentUserOwnerActionName,
                updateCurrentUserControlPersonActionName,
                deleteOwnerActionName,
                updateControlPersonActionName,
                deleteControlPersonActionName,
                requestBusinessPersonsActionName,
                submitControlPersonActionName,
                requestBusinessRolesActionName,
                completeOwnersStepActionName,
                completeControlPersonStepActionName,
                completeSummaryStepActionName,
                userInfoProvider,
                enableCurrentUserEditing
            )
    }
}

@JvmSynthetic // Hide from Java callers who should use Builder
fun BusinessRelationsConfiguration(initializer: BusinessRelationsConfiguration.Builder.() -> Unit): BusinessRelationsConfiguration =
    BusinessRelationsConfiguration.Builder()
        .apply(initializer).build()