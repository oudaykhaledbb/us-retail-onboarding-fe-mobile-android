package com.backbase.android.flow.smeo.business.core.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment

/**
 * A fragment that returns a simple [View] with [backgroundColor] from [onCreateView].
 */
open class SimpleViewFragment(
    @ColorInt private var backgroundColor: Int = Color.WHITE
) : Fragment() {

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedInstanceState?.let {
            backgroundColor = it.getInt(BACKGROUND_COLOR)
        }
        return View(inflater.context).apply {
            setBackgroundColor(backgroundColor)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BACKGROUND_COLOR, backgroundColor)
    }

    companion object {
        private const val BACKGROUND_COLOR = "SimpleViewFragment: BACKGROUND_COLOR"
    }
}
