package com.example.tasktechstalwartsfoodapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "cart_tbl")
data class MealsByCategoryCart(
    @PrimaryKey
    @ColumnInfo(name="idMeal")
    val idMeal: String,
    @ColumnInfo(name="strMeal")
    val strMeal: String,
    @ColumnInfo(name="strMealThumb")
    val strMealThumb: String
): Parcelable