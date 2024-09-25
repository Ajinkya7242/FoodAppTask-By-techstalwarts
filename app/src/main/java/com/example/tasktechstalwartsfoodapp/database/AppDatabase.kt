package com.example.tasktechstalwartsfoodapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart

@Database(entities = [MealsByCategory::class,MealsByCategoryCart::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun appDao():AppDatabaseDAO

}