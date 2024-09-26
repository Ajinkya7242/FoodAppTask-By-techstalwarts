package com.example.tasktechstalwartsfoodapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart
import com.example.tasktechstalwartsfoodapp.repository.MealListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.tasktechstalwartsfoodapp.model.Result
import com.example.tasktechstalwartsfoodapp.repository.CartOperationsRepository
import com.example.tasktechstalwartsfoodapp.repository.FavoritesOperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val repo:MealListRepository,private val cartRepo:CartOperationsRepository, private val favRepo:FavoritesOperationRepository):ViewModel() {

    private val _popularItemsLiveData = MutableLiveData<Result<List<MealsByCategory>>>()
    val popularItemsLiveData: LiveData<Result<List<MealsByCategory>>> get() = _popularItemsLiveData

    init {
        viewModelScope.launch {
            if(_popularItemsLiveData.value==null){
                fetchPopularItems()
            }
        }
    }

    suspend fun fetchPopularItems() {
        repo.getPopularItems().observeForever { result ->
            _popularItemsLiveData.postValue(result)
        }
    }

    fun removeAllFavMeal()=viewModelScope.launch { favRepo.deleteAllFavMeals() }
    fun removeAllCartMeal()=viewModelScope.launch { cartRepo.deleteAllCartMeals() }


    fun addMealToCart(meal:MealsByCategoryCart) = viewModelScope.launch { cartRepo.addMealToCart(meal) }

}