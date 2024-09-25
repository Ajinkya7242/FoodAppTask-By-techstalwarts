package com.example.tasktechstalwartsfoodapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDatabaseDAO {

    @Query("SELECT * FROM meals_tbl")
    fun getAllFavorites(): Flow<List<MealsByCategory>>

    @Delete
    suspend fun deleteFavoriteMeal(meal:MealsByCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavMeal(meal: MealsByCategory)

    @Query("DELETE FROM meals_tbl")
    suspend fun deleteAllFavMeals()



    @Query("SELECT * FROM cart_tbl")
    fun getAllCart(): Flow<List<MealsByCategoryCart>>

    @Delete
    suspend fun deleteCartMeal(meal:MealsByCategoryCart)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartMeal(meal: MealsByCategoryCart)

    @Query("DELETE FROM cart_tbl")
    suspend fun deleteAllCartMeals()
}

