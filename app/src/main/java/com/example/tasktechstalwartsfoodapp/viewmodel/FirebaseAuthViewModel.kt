package com.example.tasktechstalwartsfoodapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasktechstalwartsfoodapp.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FirebaseAuthViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository):ViewModel() {

    private val _loginResult = MutableLiveData<Result<FirebaseUser?>>()
    val loginResult: LiveData<Result<FirebaseUser?>> get() = _loginResult
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    private val _registerResult = MutableLiveData<Result<FirebaseUser?>>()
    val registerResult: LiveData<Result<FirebaseUser?>> get() = _registerResult

    fun registerUser(email: String, password: String) {
        _loading.value=true
        authRepository.registerUser(email, password).observeForever {
            _registerResult.value = it
            _loading.value=false
        }
    }

    fun loginUser(email: String, password: String) {
        _loading.value=true
        authRepository.loginUser(email, password).observeForever {
            _loginResult.value = it
            _loading.value=false
        }
    }

    fun logout() {
        authRepository.logout()
    }
}