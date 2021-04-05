package com.backbase.android.flow.smeo

import android.content.Context
import androidx.navigation.fragment.findNavController
import com.backbase.android.flow.smeo.common.AppActivity
import com.backbase.android.flow.stepnavigation.HeaderDataProvider
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.dsl.module

class MainActivity : AppActivity(R.layout.activity_main) {

    override fun onResume() {
        super.onResume()
        header.setTotalNumberOfSteps(9)
        findNavController().addOnDestinationChangedListener(HeaderDataProvider(mapFragments, this).setStepNavigationView(header))
    }

    override fun instantiateActivityModule() = module {
        val navController = findNavController()

        factory {
            aboutYouRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            otpRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            businessRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            addressRouter(supportFragmentManager.fragments[0]
                .childFragmentManager.fragments[0]
                .childFragmentManager.fragments[0]
                .findNavController())
        }

        factory { return@factory header }

        factory {
            val context: Context by inject()
            return@factory HeaderDataProvider(mapFragments, context).setStepNavigationView(header)
        }

    }

}