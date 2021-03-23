package com.backbase.android.flow.smeo.aboutyou.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCase
import java.lang.Exception

class AboutYouViewModel(private val useCase: AboutYouUseCase)
    : ViewModel() {

    internal val apiSubmitAboutYou = ApiPublisher<Any?>(this.viewModelScope)

    fun submitAboutYou(firstName: String, lastName: String, dateOfBirth: String, email: String){
        apiSubmitAboutYou.submit("updateOwner()"){
            try{
                useCase.initSmeOnBoarding()
                return@submit useCase.submitAboutYou(firstName, lastName, dateOfBirth, email)
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }
    }

}