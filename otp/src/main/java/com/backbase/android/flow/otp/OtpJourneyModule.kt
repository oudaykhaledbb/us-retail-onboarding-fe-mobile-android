package com.backbase.android.flow.otp

import com.backbase.android.flow.otp.ui.OtpViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2010-06-30.
 */
internal const val SCOPE_ID = "journey_otp"

val otpJourneyModule = module {
    viewModel { OtpViewModel(get()) }
    scope(named(SCOPE_ID)) { }
}