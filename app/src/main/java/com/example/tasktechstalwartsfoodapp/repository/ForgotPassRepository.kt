package com.example.tasktechstalwartsfoodapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class ForgotPassRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun forgotPass(email: String): LiveData<Result<FirebaseUser?>> {
        val resultLiveData = MutableLiveData<Result<FirebaseUser?>>()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.value = Result.success(firebaseAuth.currentUser)
                } else {
                    resultLiveData.value = Result.failure(task.exception ?: Exception("Registration failed"))
                }
            }
        return resultLiveData
    }
}