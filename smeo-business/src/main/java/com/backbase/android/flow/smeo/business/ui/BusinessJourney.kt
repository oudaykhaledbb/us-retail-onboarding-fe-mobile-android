package com.backbase.android.flow.smeo.business.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.backbase.android.flow.smeo.business.R
import com.backbase.android.flow.stepnavigation.HeaderDataProvider
import org.koin.android.ext.android.inject

class BusinessJourney : Fragment(R.layout.journey_business){

    private val headerDataProvider: HeaderDataProvider by inject()

    override fun onResume() {
        super.onResume()
        childFragmentManager.fragments.last().findNavController().addOnDestinationChangedListener(headerDataProvider)
    }

}