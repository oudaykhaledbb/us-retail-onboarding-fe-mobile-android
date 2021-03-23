package com.backbase.android.flow.smeo.aboutyou.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCase
import kotlinx.coroutines.delay

class AboutYouViewModel(private val useCase: AboutYouUseCase)
    : ViewModel() {

    internal val apiSubmitAboutYou = ApiPublisher<Any?>(this.viewModelScope)

    fun submitAboutYou(firstName: String, lastName: String, dateOfBirth: String, email: String){
        apiSubmitAboutYou.submit("submitAboutYou()"){
            try{
                useCase.initSmeOnBoarding()
                delay(30)
                return@submit useCase.submitAboutYou(firstName, lastName, dateOfBirth, email)
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }
    }

}