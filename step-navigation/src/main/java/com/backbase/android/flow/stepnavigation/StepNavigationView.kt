package com.backbase.android.flow.stepnavigation


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_step_navigation.view.*

class StepNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var secondaryProgressAlpha: Int = 30
) : FrameLayout(context, attrs, defStyleAttr) {

    private var onCloseJourney: (() -> Unit)? = null
    private var onBackPressed: (() -> Unit)? = null
    private var totalNumberOfSteps: Int = 0
    private var progressText = ""
    private var stepCount = 1

    init {
        val root = View.inflate(getContext(), R.layout.view_step_navigation, this)
        root.imgClose.setOnClickListener { onCloseJourney?.invoke() }
    }

    fun setOnCloseJourney(onCloseJourney: () -> Unit): StepNavigationView {
        this.onCloseJourney = onCloseJourney
        return this
    }

    fun setOnBackPressed(onBackPressed: () -> Unit): StepNavigationView {
        this.onBackPressed = onBackPressed
        return this
    }

    fun setTotalNumberOfSteps(totalNumberOfSteps: Int): StepNavigationView {
        this.totalNumberOfSteps = totalNumberOfSteps
        return this
    }

    fun setTitle(title: String): StepNavigationView {
        lblTitle.text = title
        return this
    }

    private fun updateProgress(): StepNavigationView {
        lblProgress.text = "$stepCount/$totalNumberOfSteps: $progressText"
        progressBar.progress = (stepCount * 100) / totalNumberOfSteps
        return this
    }

    fun setProgress(progress: Int): StepNavigationView {
        this.stepCount = progress
        return updateProgress()
    }

    fun setProgressText(progressText: String): StepNavigationView {
        this.progressText = progressText
        return updateProgress()
    }

}