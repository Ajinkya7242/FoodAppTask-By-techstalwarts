package com.example.tasktechstalwartsfoodapp.repository

import com.example.tasktechstalwartsfoodapp.database.AppDatabase
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FavoritesOperationRepository @Inject constructor(private val appDatabase: AppDatabase)  {

    suspend fun getFevMeals(): Flow<List<MealsByCategory>> = appDatabase.appDao().getAllFavorites().flowOn(
        Dispatchers.IO).conflate()

    suspend fun deleteFavMeal(meal: MealsByCategory)=appDatabase.appDao().deleteFavoriteMeal(meal)

    suspend fun deleteAllFavMeals()=appDatabase.appDao().deleteAllFavMeals()


    suspend fun addMealToFav(meal: MealsByCategory)=appDatabase.appDao().insertFavMeal(meal)

}