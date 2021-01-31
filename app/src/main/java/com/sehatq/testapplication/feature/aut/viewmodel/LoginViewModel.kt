package com.sehatq.testapplication.feature.aut.viewmodel

import com.sehatq.testapplication.core.event.SingleLiveEvent
import com.sehatq.testapplication.core.extention.isNull
import com.sehatq.testapplication.core.platform.BaseViewModel
import com.sehatq.testapplication.core.util.ErrorPassword
import com.sehatq.testapplication.core.util.Validator

class LoginViewModel : BaseViewModel() {

    var errorPassword: ErrorPassword? = null

    val successLogin = SingleLiveEvent<Unit>()

    val isUserNameEmpty = SingleLiveEvent<Boolean>()
    val isPasswordError = SingleLiveEvent<ErrorPassword>()


    fun validateLogin(username:String, password: String) {
        isUserNameEmpty.postValue(false)
        val passwordValidate = Validator().validatePassword(password) { error ->
            errorPassword = error
        }
        when {
            username.isEmpty() -> isUserNameEmpty.postValue(true)
            !passwordValidate -> {
                if (!errorPassword.isNull()) isPasswordError.postValue(errorPassword)
            }
            else -> successLogin.call()
        }
    }

}