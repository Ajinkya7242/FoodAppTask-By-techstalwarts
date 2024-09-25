package com.example.tasktechstalwartsfoodapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappmvvm_retrofit.model.Meal
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart
import com.example.tasktechstalwartsfoodapp.model.Result
import com.example.tasktechstalwartsfoodapp.repository.CartOperationsRepository
import com.example.tasktechstalwartsfoodapp.repository.FavoritesOperationRepository
import com.example.tasktechstalwartsfoodapp.repository.FoodDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(private val repository: FoodDetailsRepository,private val favOperationRepo: FavoritesOperationRepository,private val cartOperationsRepository: CartOperationsRepository) : ViewModel() {

    private val _mealDetailsLiveData = MutableLiveData<Result<Meal?>>()
    val mealDetailsLiveData: LiveData<Result<Meal?>> get() = _mealDetailsLiveData

    fun getMealDetails(mealId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getMealDetails(mealId)
                _mealDetailsLiveData.postValue(result)  // Correct: Posting Result<Meal?>
            } catch (e: Exception) {
                _mealDetailsLiveData.postValue(Result.Error(e))
            }
        }
    }

    fun addFavMeal(meal: MealsByCategory) = viewModelScope.launch { favOperationRepo.addMealToFav(meal) }

    fun addCartMeal(meal: MealsByCategoryCart) = viewModelScope.launch { cartOperationsRepository.addMealToCart(meal) }

    fun removeFavMeal(dog:MealsByCategory)=viewModelScope.launch { favOperationRepo.deleteFavMeal(dog) }

    val _mealList = MutableStateFlow<List<MealsByCategory>>(emptyList())
    val mealList = _mealList.asStateFlow()

    val _mealListCart = MutableStateFlow<List<MealsByCategoryCart>>(emptyList())
    val mealListCart = _mealListCart.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            favOperationRepo.getFevMeals().distinctUntilChanged().collect{listOfNotes->
                if(listOfNotes.isNotEmpty()){
                    _mealList.value=listOfNotes
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            cartOperationsRepository.getCartMeals().distinctUntilChanged().collect{listOfNotes->
                if(listOfNotes.isNotEmpty()){
                    _mealListCart.value=listOfNotes
                }
            }
        }


    }

}

