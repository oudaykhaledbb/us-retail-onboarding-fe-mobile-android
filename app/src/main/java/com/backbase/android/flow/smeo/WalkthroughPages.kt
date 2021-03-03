package com.backbase.android.flow.smeo

import com.backbase.android.flow.smeo.walkthrough.WalkthroughPageBuilder
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
