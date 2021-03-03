package com.backbase.android.flow.smeo.walkthrough

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredText
import kotlinx.android.synthetic.main.screen_walkthrough.*
import org.koin.android.ext.android.inject

class WalkthroughScreen : Fragment(R.layout.screen_walkthrough), ViewPager.OnPageChangeListener {

    private val configuration: WalkthroughConfiguration by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = WalkthroughPagerAdapter(parentFragmentManager, configuration.pages)

        tabIndicator.setupWithViewPager(viewPager, true)
        viewPager.addOnPageChangeListener(this)
        viewPager.adapter = adapter

        onPageChanged(0)

    }

    fun onPageChanged(position: Int) {
        if (position < configuration.pages.size - 1) {
            lblNext.setOnClickListener {
                viewPager.setCurrentItem(position + 1, true)
            }
        } else {
            lblNext.setOnClickListener {
//                    WalkthroughContract.onNext?.invoke(requireActivity())
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        onPageChanged(position)
    }

    private fun buildWelcomePage() = WalkthroughPageBuilder()
        .setHeaderImage(DeferredDrawable.Resource(R.drawable.img_welcome))
        .addHeader(DeferredText.Constant("We’re so glad you’re here!"))
        .addContent(DeferredText.Constant("You are few minutes away to open a business account tailored to meet your business needs!"))
        .addHeaderWithIcon(
            DeferredDrawable.Resource(R.drawable.ic_person),
            DeferredText.Constant("Sole proprietorship")
        )
        .addSupport(DeferredText.Constant("A sole proprietor is someone who owns an unincorporated business by himself and has full control over it."))
        .build()

    private fun buildInformationPage() = WalkthroughPageBuilder()
        .setHeaderImage(DeferredDrawable.Resource(R.drawable.img_info))
        .addHeader(DeferredText.Constant("What you’ll need to get started:"))
        .addSection(
            DeferredDrawable.Resource(R.drawable.ic_document),
            DeferredText.Constant("A Few documents"),
            DeferredText.Constant("Business License, DBA certificate*, A valid ID.")
        )
        .addSection(
            DeferredDrawable.Resource(R.drawable.ic_business),
            DeferredText.Constant("Details about your business"),
            DeferredText.Constant("Legal name, EIN*, date of establishment, Country of legal formation.")
        )
        .addCaption(DeferredText.Constant("* A DBA certificate, also known as a trade name, fictitious name, or assumed name, allows you to conduct business under a name other than your legal business name (if applicable)."))
        .build()

}