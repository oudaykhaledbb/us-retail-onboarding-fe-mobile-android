package com.backbase.android.flow.identityverification

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.identityverification.R

object CountrySelectionDialogFragmentElements {
    val rvCountriesViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.rvCountries))
    val txtSearchViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtSearch))
    val imgBackViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.imgBack))
}