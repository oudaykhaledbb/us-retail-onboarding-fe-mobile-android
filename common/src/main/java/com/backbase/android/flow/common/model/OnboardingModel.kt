package com.backbase.android.flow.common.model

data class OnboardingModel(
    var firstName: String? = null,
    var lastName: String? = null,
    var dateOfBirth: String? = null,
    var email: String? = null,
    var emailVerified: Boolean = false,
    var onboardingType: String? = null,
    var termsAndConditionsInfo:TermsAndConditionsInfo? = null,
    var address: Address? = null,
    var ssn: String? = null,
    var phoneNumber: String? = null,
    var phoneNumberVerified: Boolean = false,
    var fullName: String? = null,
    var antiMoneyLaunderingInfo: AntiMoneyLaunderingInfo? = null
) {}

data class TermsAndConditionsInfo(
    var accepted: Boolean = false,
    var acceptedAt: String? = null
)

data class AntiMoneyLaunderingInfo(
    var matchStatus: String? = null,
    var url: String? = null
)