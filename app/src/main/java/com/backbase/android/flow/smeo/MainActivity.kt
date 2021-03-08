package com.backbase.android.flow.smeo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.backbase.android.flow.onboarding.app.common.AppActivity
import org.koin.dsl.module


class MainActivity : AppActivity(
        R.layout.activity_main
) {
    override fun instantiateActivityModule() = module {
        val navController = findNavController()
        factory {
            walkthroughRouter(navController) {
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
