package com.backbase.android.flow.common.fragment

import android.view.WindowManager
import androidx.fragment.app.DialogFragment

open class SecureDialogFragment() : DialogFragment() {
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