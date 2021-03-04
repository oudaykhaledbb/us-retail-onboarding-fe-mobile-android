package com.backbase.android.flow.onboarding.app.common

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

/**
 * Created by Backbase R&D B.V. on 2010-05-20.
 *
 * Base Backbase Flow Onboarding Journey-based application activity.
 */

abstract class AppActivity(
    @LayoutRes private val appContentLayout: Int
) : AppCompatActivity() {

    private val activityModule: Module by lazy { instantiateActivityModule() }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(appContentLayout)
        loadKoinModules(activityModule)
        setWindowFullScreen()
    }
    private fun setWindowFullScreen(){
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            decorView.systemUiVisibility = WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS or WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    final override fun onDestroy() {
        unloadKoinModules(activityModule)
        super.onDestroy()
    }

    protected abstract fun instantiateActivityModule(): Module

    protected fun findNavController() = supportFragmentManager.fragments[0].findNavController()

    private companion object {
        private val TAG = AppActivity::class.java.simpleName
    }
}
