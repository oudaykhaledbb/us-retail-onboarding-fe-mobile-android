package com.backbase.android.flow.smeo

import android.content.Context
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.smeo.common.AppActivity
import com.backbase.android.flow.stepnavigation.HeaderDataProvider
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

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
            productSelection(navController) {
                setTheme(R.style.AppTheme)
            }
        }


        factory {
            businessRelations(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            lookupRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            addressRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            uploadFilesRouter(navController)
        }

        factory {
            ssnRouter(this@MainActivity)
        }

        factory { return@factory header }

        factory {
            val context: Context by inject()
            return@factory HeaderDataProvider(mapFragments, context).setStepNavigationView(header)
        }

    }

}