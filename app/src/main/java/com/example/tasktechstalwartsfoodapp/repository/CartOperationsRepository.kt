package com.example.tasktechstalwartsfoodapp.repository

import com.example.tasktechstalwartsfoodapp.database.AppDatabase
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CartOperationsRepository @Inject constructor(private val appDatabase: AppDatabase){

    suspend fun getCartMeals(): Flow<List<MealsByCategoryCart>> = appDatabase.appDao().getAllCart().flowOn(
        Dispatchers.IO).conflate()

    suspend fun deleteCartMeal(meal: MealsByCategoryCart)=appDatabase.appDao().deleteCartMeal(meal)

    suspend fun deleteAllCartMeals()=appDatabase.appDao().deleteAllCartMeals()

    suspend fun addMealToCart(meal: MealsByCategoryCart)=appDatabase.appDao().insertCartMeal(meal)
}