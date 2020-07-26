package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.PersonRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mSharedPerferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    val login : LiveData<ValidationListener> = mLogin

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email,password, object : APIListener{
            override fun onSuccess(model: HeaderModel) {

                mSharedPerferences.store(TaskConstants.SHARED.TOKEN_KEY,model.token)
                mSharedPerferences.store(TaskConstants.SHARED.PERSON_KEY,model.personKey)
                mSharedPerferences.store(TaskConstants.SHARED.PERSON_NAME,model.name)

                mLogin.value = ValidationListener()
            }

            override fun onFailure(str: String) {
                mLogin.value = ValidationListener(str)
            }

        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
    }

}