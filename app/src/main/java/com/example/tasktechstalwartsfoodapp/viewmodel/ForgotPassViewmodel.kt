package com.example.tasktechstalwartsfoodapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasktechstalwartsfoodapp.repository.ForgotPassRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewmodel @Inject constructor(private val authRepository: ForgotPassRepository):ViewModel(){

    private val _forgotPassResult = MutableLiveData<Result<FirebaseUser?>>()
    val forgotPassResult: LiveData<Result<FirebaseUser?>> get() = _forgotPassResult

    fun forgotPassword(email: String) {
        authRepository.forgotPass(email).observeForever {
            _forgotPassResult.value = it
        }
    }

}