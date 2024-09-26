package com.example.tasktechstalwartsfoodapp.network

import com.example.tasktechstalwartsfoodapp.model.MealList
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryLists
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiInterface {

    @GET("filter.php")
    suspend fun getMealsList(@Query("c") categoryName:String): MealsByCategoryLists

    @GET("lookup.php?")
    suspend fun getMealDetails(@Query("i") id:String): MealList
}