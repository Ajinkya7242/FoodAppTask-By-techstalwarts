package com.example.tasktechstalwartsfoodapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryLists
import com.example.tasktechstalwartsfoodapp.network.MealApiInterface
import javax.inject.Inject
import com.example.tasktechstalwartsfoodapp.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealListRepository @Inject constructor(private val api: MealApiInterface){

    suspend fun getPopularItems(categoryName: String = "Vegetarian"): LiveData<Result<List<MealsByCategory>>> {
        val popularItemsLiveData = MutableLiveData<Result<List<MealsByCategory>>>()

        try {
            val response = api.getMealsList(categoryName)
            popularItemsLiveData.postValue(Result.Success(response.meals))
        } catch (e: Exception) {
            popularItemsLiveData.postValue(Result.Error(e))
        }

        return popularItemsLiveData
    }


}