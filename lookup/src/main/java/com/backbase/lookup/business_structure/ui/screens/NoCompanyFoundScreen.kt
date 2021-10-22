package com.backbase.lookup.business_structure.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.backbase.lookup.R
import kotlinx.android.synthetic.main.screen_no_company_found.*

class NoCompanyFoundScreen : DialogFragment() {

    internal var onSkipListener: (()->Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.screen_no_company_found, container, false)

    override fun getTheme() =
        R.style.Theme_Backbase_Fullscreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_FRAME,
            R.style.FullScreenDialog
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnTryAgain.setOnClickListener { dismissAllowingStateLoss() }
        btnSkip.setOnClickListener {
            onSkipListener?.invoke()
            dismissAllowingStateLoss()
        }
        imgClose.setOnClickListener { dismissAllowingStateLoss() }
    }

    companion object{
        fun getInstance(onSkipListener: (()->Unit)) = NoCompanyFoundScreen().apply {
            this.onSkipListener = onSkipListener
        }
    }

}