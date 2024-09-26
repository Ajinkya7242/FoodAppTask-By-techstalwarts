package com.example.tasktechstalwartsfoodapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasktechstalwartsfoodapp.UI.authentication.LoginOrRegActivity
import com.example.tasktechstalwartsfoodapp.model.Meal
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.Result
import com.example.tasktechstalwartsfoodapp.network.MealApiInterface
import javax.inject.Inject

class FoodDetailsRepository @Inject constructor(private val api: MealApiInterface) {

    suspend fun getMealDetails(id: String): Result<Meal?> {
        return try {
            val response = api.getMealDetails(id)
            if (response.meals.isNotEmpty()) {
                Result.Success(response.meals[0])
            } else {
                Result.Error(Exception("No meal details found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
