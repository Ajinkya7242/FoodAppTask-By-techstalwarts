package com.example.tasktechstalwartsfoodapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun registerUser(email: String, password: String): LiveData<Result<FirebaseUser?>> {
        val resultLiveData = MutableLiveData<Result<FirebaseUser?>>()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.value = Result.success(firebaseAuth.currentUser)
                } else {
                    resultLiveData.value = Result.failure(task.exception ?: Exception("Registration failed"))
                }
            }
        return resultLiveData
    }

    fun loginUser(email: String, password: String): LiveData<Result<FirebaseUser?>> {
        val resultLiveData = MutableLiveData<Result<FirebaseUser?>>()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.value = Result.success(firebaseAuth.currentUser)
                } else {
                    resultLiveData.value = Result.failure(task.exception ?: Exception("Login failed"))
                }
            }
        return resultLiveData
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}