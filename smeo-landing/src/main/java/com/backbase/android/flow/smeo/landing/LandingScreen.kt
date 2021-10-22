package com.backbase.android.flow.smeo.landing

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.screen_landing.*
import org.koin.android.ext.android.inject

private const val CASE_ID = "CASE_ID"
private const val EMAIL = "EMAIL"

class LandingScreen : DialogFragment(R.layout.screen_landing) {

    private var caseId: String? = null
    private var email: String? = null

    private val configuration: LandingConfiguration by inject()

    fun applyBundle(caseId: String?, email: String?): LandingScreen {
        arguments = Bundle().apply {
            putString(CASE_ID, caseId)
            putString(EMAIL, email)
        }
        return this
    }

    override fun getTheme() = R.style.Theme_Backbase_Fullscreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        caseId = requireArguments().getString(CASE_ID)
        email = requireArguments().getString(EMAIL)
        setStyle(
            STYLE_NO_FRAME,
            R.style.FullScreenDialog
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnContinue.setOnClickListener { openApplicationCenter() }
        email?.let {
            lblDescription.text = getString(R.string.smeo_landing_landing_description)
                .replace("[Email]", "<b>$it</b>")
                .replace("\n", "<br/>")
                .toSpanned()
        }
    }

    private fun openApplicationCenter() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("${configuration.applicationCenterUrl}?id=$caseId")
        startActivity(intent)
    }

    private fun String.toSpanned(): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(this)
        }
    }

}