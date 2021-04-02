package com.backbase.android.flow.common.model

import com.backbase.android.flow.models.InteractionStep
import java.util.*

object StepConfiguration{
    var interactionId:String? = null
    var model: OnboardingModel?  = null
    var stepName: String? = null
    var previousStepName: String? = null
    var steps: HashMap<String, InteractionStep>? = null
}