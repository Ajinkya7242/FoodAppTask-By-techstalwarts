package com.example.tasktechstalwartsfoodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart
import com.example.tasktechstalwartsfoodapp.repository.CartOperationsRepository
import com.example.tasktechstalwartsfoodapp.repository.FavoritesOperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartVIewModel @Inject constructor(private val repo: CartOperationsRepository,private val favRepo: FavoritesOperationRepository):ViewModel(){

    val _mealList = MutableStateFlow<List<MealsByCategoryCart>>(emptyList())
    val mealList = _mealList.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCartMeals().distinctUntilChanged().collect{listOfNotes->
                if(listOfNotes.isNotEmpty()){
                    _mealList.value=listOfNotes
                }
            }
        }
    }


    fun removeCartMeal(meal:MealsByCategoryCart)=viewModelScope.launch { repo.deleteCartMeal(meal) }
    fun addFavMeal(meal:MealsByCategory)=viewModelScope.launch { favRepo.addMealToFav(meal) }

}