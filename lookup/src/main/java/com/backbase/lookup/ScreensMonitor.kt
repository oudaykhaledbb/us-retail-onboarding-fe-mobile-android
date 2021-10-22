package com.backbase.lookup

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

class ScreensMonitor {

    private var onScreenChangeListener: OnScreenChangeListener? = null

    fun navigate(navController: NavController, @IdRes resId: Int, tag: String, bundle: Bundle? = null){
        navController.navigate(resId, bundle)
        onScreenChangeListener?.onScreenChanged(tag)
    }

    fun setOnScreenChangeListener(onScreenChangeListener: OnScreenChangeListener){
        this.onScreenChangeListener = onScreenChangeListener
    }

}

interface OnScreenChangeListener{
    fun onScreenChanged(tag: String)
}