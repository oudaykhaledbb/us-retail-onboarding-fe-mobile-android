package com.backbase.android.flow.smeo.business.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.flow.smeo.business.R
import org.koin.android.ext.android.inject

class BusinessInfoScreen : Fragment(R.layout.screen_business_info) {

    private val viewModel: BusinessInfoViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}