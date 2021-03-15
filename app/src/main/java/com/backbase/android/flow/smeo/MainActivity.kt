package com.backbase.android.flow.smeo

import com.backbase.android.flow.smeo.common.AppActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.dsl.module

class MainActivity : AppActivity(R.layout.activity_main) {

    override fun onResume() {
        super.onResume()
        header.setTotalNumberOfSteps(1)
    }

    override fun instantiateActivityModule() = module {
        val navController = findNavController()
        factory {
            aboutYouRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory { return@factory header }
    }

}