package com.example.tasktechstalwartsfoodapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "meals_tbl")
data class MealsByCategory(
    @PrimaryKey
    @ColumnInfo(name="idMeal")
    val idMeal: String,
    @ColumnInfo(name="strMeal")
    val strMeal: String,
    @ColumnInfo(name="strMealThumb")
    val strMealThumb: String
):Parcelable