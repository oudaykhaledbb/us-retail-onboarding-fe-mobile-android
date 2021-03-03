package com.backbase.android.flow.smeo.walkthrough

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.screen_single_walkthrough.*

private const val BUNDLE_PAGE_CONTENT = "BUNDLE_PAGE_CONTENT"

class WalkthroughSingleScreen : Fragment(R.layout.screen_single_walkthrough) {

    private lateinit var pageContent: PageContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageContent = requireArguments().getSerializable(BUNDLE_PAGE_CONTENT) as PageContent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgWalkthrough.setImageDrawable(pageContent.headerImage.resolve(requireContext()))
        pageContent.walkthroughItems.forEach {
            dynamicContainer.addView(it.resolve(requireContext()))
        }
    }

    companion object {

        fun newInstance(pageContent: PageContent) = WalkthroughSingleScreen().apply {
            arguments = Bundle().apply {
                putSerializable(BUNDLE_PAGE_CONTENT, pageContent)
            }
        }

    }

}