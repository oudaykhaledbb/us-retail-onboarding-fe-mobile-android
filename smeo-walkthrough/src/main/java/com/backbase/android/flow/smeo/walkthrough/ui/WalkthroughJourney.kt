package com.backbase.android.flow.smeo.walkthrough.ui

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.backbase.android.flow.smeo.walkthrough.R
import com.backbase.android.flow.smeo.walkthrough.WalkthroughConfiguration
import com.backbase.android.flow.smeo.walkthrough.WalkthroughRouter
import kotlinx.android.synthetic.main.journey_walkthrough.*
import org.koin.android.ext.android.inject


class WalkthroughJourney : Fragment(R.layout.journey_walkthrough), ViewPager.OnPageChangeListener {

    private val configuration: WalkthroughConfiguration by inject()
    private val router: WalkthroughRouter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = WalkthroughPagerAdapter(parentFragmentManager, configuration.pages)

        tabIndicator.setupWithViewPager(viewPager, true)
        viewPager.addOnPageChangeListener(this)
        viewPager.adapter = adapter

        onPageChanged(0)

        cbTermsAndConditions.setOnCheckedChangeListener { _, isChecked ->
            lblNext.isEnabled = isChecked
        }

        lblNext.isEnabled = true

        prepareTermsAndConditionsTextView()

    }

    fun onPageChanged(position: Int) {
        handleNextButtonWhenPageChanged(position)
        handleSkipButtonWhenPageChanged(position)
    }

    private fun handleSkipButtonWhenPageChanged(position: Int) {
        if (position > 0) {
            btnSkip.setText(R.string.smeo_walkthrough_back)
            btnSkip.setOnClickListener {
                viewPager.setCurrentItem(position - 1, true)
            }
        } else {
            btnSkip.setText(R.string.smeo_walkthrough_skip)
            btnSkip.setOnClickListener {
                viewPager.setCurrentItem(configuration.pages.size, true)
            }
        }
    }

    private fun handleNextButtonWhenPageChanged(position: Int) {
        if (position < configuration.pages.size - 1) {
            termsAndConditions.visibility = View.GONE
            lblNext.isEnabled = true
            lblNext.setText(R.string.smeo_walkthrough_next)
            lblNext.setOnClickListener {
                viewPager.setCurrentItem(position + 1, true)
            }
        } else {
            lblNext.setText(R.string.smeo_walkthrough_get_started)
            cbTermsAndConditions.isChecked = false
            lblNext.isEnabled = false
            termsAndConditions.visibility = View.VISIBLE
            lblNext.setOnClickListener {
                router.onWalkthroughFinished()
            }
        }
    }

    private fun prepareTermsAndConditionsTextView() {
        val spannableString = SpannableString(getString(R.string.smeo_walkthrough_termsAndConditions))

        val termsAndConditionsSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                router.openTermsAndConditions()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val privacyPolicySpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                router.openPrivacyPolicy()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(termsAndConditionsSpan, spannableString.indexOf("Terms"), spannableString.indexOf("Terms") + 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(privacyPolicySpan, spannableString.indexOf("Privacy"), spannableString.indexOf("Privacy") + 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtTermsAndConditions.text = spannableString
        txtTermsAndConditions.movementMethod = LinkMovementMethod.getInstance()
        txtTermsAndConditions.highlightColor = Color.TRANSPARENT
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        onPageChanged(position)
    }
}