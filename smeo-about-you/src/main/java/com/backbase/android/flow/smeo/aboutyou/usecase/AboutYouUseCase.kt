package com.backbase.android.flow.smeo.aboutyou.usecase

/**
 * Created by Backbase R&D B.V. on 2010-06-01.
 */
interface AboutYouUseCase {

    suspend fun initSmeOnBoarding(): Any?

    suspend fun submitAboutYou(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String
    ): Any?

}