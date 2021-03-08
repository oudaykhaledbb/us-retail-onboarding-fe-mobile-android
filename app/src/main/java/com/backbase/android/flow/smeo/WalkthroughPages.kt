package com.backbase.android.flow.smeo

import com.backbase.android.flow.smeo.walkthrough.ui.WalkthroughPageBuilder
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredText

fun welcomePage() = WalkthroughPageBuilder()
    .setHeaderImage(DeferredDrawable.Resource(R.drawable.img_welcome))
    .addHeader(DeferredText.Resource(R.string.walkthrough_label_welcome_header))
    .addContent(DeferredText.Resource(R.string.walkthrough_label_welcome_content))
    .addHeaderWithIcon(
        DeferredDrawable.Resource(R.drawable.ic_person),
        DeferredText.Resource(R.string.walkthrough_label_welcome_sole_proprietorship)
    )
    .addSupport(DeferredText.Resource(R.string.walkthrough_label_welcome_proprietorship_description))
    .build()


fun informationPage() = WalkthroughPageBuilder()
        .setHeaderImage(DeferredDrawable.Resource(R.drawable.img_info))
        .addHeader(DeferredText.Resource(R.string.walkthrough_info_header))
        .addSection(
                DeferredDrawable.Resource(R.drawable.ic_document),
                DeferredText.Resource(R.string.walkthrough_label_info_few_documents),
                DeferredText.Resource(R.string.walkthrough_label_info_business_liscence)
        )
        .addSection(
                DeferredDrawable.Resource(R.drawable.ic_business),
                DeferredText.Resource(R.string.walkthrough_info_details_about_your_business),
                DeferredText.Resource(R.string.walkthrough_info_legal_name)
        )
        .addCaption(DeferredText.Resource(R.string.walkthrough_info_dba_ccertificate))
        .build()

fun requirementsPage() = WalkthroughPageBuilder()
        .setHeaderImage(DeferredDrawable.Resource(R.drawable.img_requirements))
        .addHeader(DeferredText.Resource(R.string.walkthrough_requirements_header))
        .addContent(DeferredText.Resource(R.string.walkthrough_label_requirements_content))
        .addLabelWithIcon(
                DeferredDrawable.Resource(R.drawable.ic_tick),
                DeferredText.Resource(R.string.walkthrough_label_your_business_located_in_us)
        )
        .addLabelWithIcon(
                DeferredDrawable.Resource(R.drawable.ic_tick),
                DeferredText.Resource(R.string.walkthrough_label_you_are_us_citizen)
        )
        .addLabelWithIcon(
                DeferredDrawable.Resource(R.drawable.ic_tick),
                DeferredText.Resource(R.string.walkthrough_label_you_can_provide_ssn)
        )
        .build()

fun businessNotAllowedPage() = WalkthroughPageBuilder()
        .setHeaderImage(DeferredDrawable.Resource(R.drawable.img_businessnotallowed))
        .addHeader(DeferredText.Resource(R.string.walkthrough_business_not_allowed_header))
        .addContent(DeferredText.Resource(R.string.walkthrough_label_business_not_allowed_content))
        .addLabelWithIcon(
                DeferredDrawable.Resource(R.drawable.ic_marijuana),
                DeferredText.Resource(R.string.walkthrough_label_your_business_located_in_us)
        )
        .addLabelWithIcon(
                DeferredDrawable.Resource(R.drawable.ic_gambling),
                DeferredText.Resource(R.string.walkthrough_label_sale_and_distribution_of_marijuana)
        )
        .addLabelWithIcon(
                DeferredDrawable.Resource(R.drawable.ic_money),
                DeferredText.Resource(R.string.walkthrough_label_money_service_business)
        )
        .addLabelWithIcon(
                DeferredDrawable.Resource(R.drawable.ic_card),
                DeferredText.Resource(R.string.walkthrough_label_privately_owned_atms)
        )
        .build()

fun termsAndConditionsPages() = WalkthroughPageBuilder()
        .setHeaderImage(DeferredDrawable.Resource(R.drawable.img_tnc))
        .addHeader(DeferredText.Resource(R.string.walkthrough_terms_and_conditions_header))
        .addContent(DeferredText.Resource(R.string.walkthrough_label_terms_and_conditions_content))
        .build()
