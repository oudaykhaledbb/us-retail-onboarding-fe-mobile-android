package com.backbase.android.flow.identityverification

import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.identityverification.core.journey.launchScreen
import com.backbase.android.flow.identityverification.core.journey.on
import com.backbase.android.flow.identityverification.core.koin.KoinTest
import com.backbase.android.flow.identityverification.core.view.checkMatches
import com.backbase.android.flow.identityverification.ui.scan.CountrySelectionDialogFragment
import com.backbase.android.flow.identityverification.ui.scan.countryName
import com.backbase.android.flow.v2.models.InteractionResponse
import com.jumio.nv.custom.NetverifyCountry
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import java.lang.Thread.sleep

class CountrySelectionDialogFragmentTest: KoinTest() {
    private val fragmentModule: Module by lazy {
        module {
            factory {
                object : IdentityVerificationRouter {
                    override fun onIdentityVerified(interactionResponse: InteractionResponse<*>?) {

                    }

                    override fun onIdentityFailed(data: Any?) {

                    }
                }
            }
        }
    }

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        loadKoinModules(IdentityVerificationJourneyModule)
        provideIdentityVerificationConfiguration()
        loadKoinModules(fragmentModule)
    }

    @Test
    fun testAllViewsAreDisplayed() {
        val fragmentContainer = launchScreen<CountrySelectionDialogFragment>()
        val countries = HashMap<String, NetverifyCountry>()
        fragmentContainer.onFragment { fragment->
            fragment
                .setCountriesList( ArrayList(countries.values.sortedBy { it.countryName() }))
                .setTitle("Choose your country")
                .setOnRadioButtonSelectedListener { netVerifyCountry ->
                    
            }
        }
        on(CountrySelectionDialogFragmentElements) {
            sleep(10000)
            rvCountriesViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtSearchViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            imgBackViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }
}