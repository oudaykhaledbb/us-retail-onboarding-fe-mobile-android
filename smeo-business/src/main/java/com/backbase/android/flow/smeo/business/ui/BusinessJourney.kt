package com.backbase.android.flow.smeo.business.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.backbase.android.flow.address.AddressRouter
import com.backbase.android.flow.smeo.business.R
import com.backbase.android.flow.stepnavigation.HeaderDataProvider
import org.koin.android.ext.android.inject
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class BusinessJourney : Fragment(R.layout.journey_business){

    private val headerDataProvider: HeaderDataProvider by inject()
    private val activityModule: Module by lazy { instantiateActivityModule() }

    private fun instantiateActivityModule() = module {
        factory {
            addressRouter()
        }
    }

    override fun onResume() {
        super.onResume()
        childFragmentManager.fragments.last().findNavController().addOnDestinationChangedListener(headerDataProvider)
    }

    override fun onDestroy() {
        unloadKoinModules(activityModule)
        super.onDestroy()
    }

    private fun addressRouter(
        completion: () -> Unit = {}
    ) = object : AddressRouter {

        override fun onAddressFinished(data: Any?) {
            findNavController().navigate(R.id.action_to_businessIdentityScreen)
            completion()
        }
    }

}