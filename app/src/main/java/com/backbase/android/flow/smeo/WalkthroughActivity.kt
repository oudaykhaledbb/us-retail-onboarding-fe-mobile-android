package com.backbase.android.flow.smeo

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.backbase.android.flow.smeo.common.AppActivity
import org.koin.dsl.module

class WalkthroughActivity : AppActivity(
        R.layout.activity_walkthrough
) {
    override fun instantiateActivityModule() = module {
        val navController = findNavController()
        factory {
            walkthroughRouter(this@WalkthroughActivity, navController) {
                setTheme(R.style.AppTheme)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val lsActiveFragments: List<Fragment> =
                supportFragmentManager.fragments
        for (fragmentActive in lsActiveFragments) {
            if (fragmentActive is NavHostFragment) {
                val lsActiveSubFragments: List<Fragment> =
                        fragmentActive.getChildFragmentManager().fragments
                for (fragmentActiveSub in lsActiveSubFragments) {
                    fragmentActiveSub.onActivityResult(requestCode, resultCode, data)
                }
            }
        }
    }
}
