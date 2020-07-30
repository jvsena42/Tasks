package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.PersonRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mSharedPerferences = SecurityPreferences(application)

    private val mCreate = MutableLiveData<ValidationListener>()
    val create: LiveData<ValidationListener> = mCreate

    fun create(name: String, email: String, password: String) {
        mPersonRepository.create(name, email, password, object : APIListener <HeaderModel>{
            override fun onSuccess(model: HeaderModel) {

                mSharedPerferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                mSharedPerferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                mSharedPerferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)

                mCreate.value = ValidationListener()
            }

            override fun onFailure(str: String) {
                mCreate.value = ValidationListener(str)
            }

        })
    }

}