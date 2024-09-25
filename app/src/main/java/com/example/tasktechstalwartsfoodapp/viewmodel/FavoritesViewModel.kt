package com.example.tasktechstalwartsfoodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.repository.FavoritesOperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repo:FavoritesOperationRepository):
    ViewModel() {

    val _mealList = MutableStateFlow<List<MealsByCategory>>(emptyList())
    val mealList = _mealList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFevMeals().distinctUntilChanged().collect{listOfNotes->
                if(listOfNotes.isNotEmpty()){
                    _mealList.value=listOfNotes
                }
            }
        }
    }


    fun removeFavMeal(dog:MealsByCategory)=viewModelScope.launch { repo.deleteFavMeal(dog) }

}