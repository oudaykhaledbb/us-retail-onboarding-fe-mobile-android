package com.backbase.android.flow.common.fragment

import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class SecureFragment(@LayoutRes private val layoutId: Int) : Fragment(layoutId) {
    override fun onResume() {
        addSecureWindowFlags()
        super.onResume()
    }
    private fun addSecureWindowFlags() {
        requireActivity().window.apply {
            setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}