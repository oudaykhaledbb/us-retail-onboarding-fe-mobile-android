package com.backbase.android.flow.stepnavigation

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.backbase.deferredresources.DeferredText

class HeaderDataProvider(private val mapFragments: Map<String, HeaderLabels>, private val context: Context): NavController.OnDestinationChangedListener{

    private var stepNavigationView: StepNavigationView? = null

    fun setStepNavigationView(stepNavigationView: StepNavigationView): HeaderDataProvider{
        this.stepNavigationView = stepNavigationView
        stepNavigationView?.setTotalNumberOfSteps(mapFragments.size)
        return this
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        val headerLabels = mapFragments[destination.label]
        stepNavigationView?.let { stepNavigationView ->
            if (headerLabels == null) {
                stepNavigationView.setProgressText(destination.label.toString())
                stepNavigationView.setTitle(destination.label.toString())
                stepNavigationView.setProgress(0)
            } else {
                stepNavigationView.setProgressText(headerLabels.subtitle.resolve(context).toString())
                stepNavigationView.setTitle(headerLabels.title.resolve(context).toString())
                stepNavigationView.setProgress(headerLabels.index)
            }
        }
    }

}

class HeaderLabels(val index: Int, val title: DeferredText, var subtitle: DeferredText)