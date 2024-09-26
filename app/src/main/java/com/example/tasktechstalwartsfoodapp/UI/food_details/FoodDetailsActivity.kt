package com.example.tasktechstalwartsfoodapp.UI.food_details

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.databinding.ActivityFoodDetailsBinding
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart
import com.example.tasktechstalwartsfoodapp.model.Result
import com.example.tasktechstalwartsfoodapp.viewmodel.MealDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FoodDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityFoodDetailsBinding
    private val viewModel: MealDetailsViewModel by viewModels()
    lateinit var mealsByCategory: MealsByCategory
    var favMealsList:ArrayList<MealsByCategory> =ArrayList<MealsByCategory>()
    var cartMealsList:ArrayList<MealsByCategoryCart> =ArrayList<MealsByCategoryCart>()
    var isFavoriteMeal: Boolean = false
    var isCart: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        mealsByCategory = intent.getParcelableExtra<MealsByCategory>("id")?: MealsByCategory("52772","","")
        viewModel.getMealDetails(mealsByCategory.idMeal ?: "52772")
        observeMealDetails()
        observerDataFromRoomDb()

        binding.ivHeart.setOnClickListener {
            if (isFavoriteMeal) {

                Toast.makeText(this@FoodDetailsActivity,"Already Added to the favorites",Toast.LENGTH_SHORT).show()

            } else {
                val tintColor = ContextCompat.getColor(this, R.color.colorPrimary)
                binding.ivHeart.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
                viewModel.addFavMeal(mealsByCategory)
                Toast.makeText(this, "Meal Successfully Added to Favorites", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddToCart.setOnClickListener {
            binding.btnAddToCart.isEnabled=false
            val mealForCart = MealsByCategoryCart(
                idMeal = mealsByCategory.idMeal,
                strMeal = mealsByCategory.strMeal,
                strMealThumb = mealsByCategory.strMealThumb
            )
            viewModel.addCartMeal(mealForCart)
        }



    }

    private fun observerDataFromRoomDb() {
        lifecycleScope.launch {
            viewModel.mealList.collect { mealList ->
                Timber.d(mealList.toString())
                if (mealList.isNotEmpty()) {
                    favMealsList.clear()
                    favMealsList.addAll(mealList)

                    isFavoriteMeal = favMealsList.any { it.idMeal == mealsByCategory.idMeal }
                    if (isFavoriteMeal) {
                        val tintColor = ContextCompat.getColor(this@FoodDetailsActivity, R.color.colorPrimary)
                        binding.ivHeart.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
                    } else {
                        val tintColor = ContextCompat.getColor(this@FoodDetailsActivity, R.color.grey)
                        binding.ivHeart.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
                    }
                }
            }

        }

        lifecycleScope.launch {
            viewModel.mealListCart.collect { mealList ->
                Timber.d(mealList.toString())
                Log.d("Check Vlaue",mealList.toString())

                if (mealList.isNotEmpty()) {
                    cartMealsList.clear()
                    cartMealsList.addAll(mealList)
                    isCart = cartMealsList.any {
                        it.idMeal == mealsByCategory.idMeal
                    }

                    if (isCart) {
                        binding.btnAddToCart.isEnabled=false
                    } else {
                        binding.btnAddToCart.isEnabled=true
                    }

                }
            }
        }


    }

    private fun observeMealDetails() {
        binding.cvProgressbar.isVisible = true

        viewModel.mealDetailsLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.cvProgressbar.isVisible = false
                    val meals = result.data
                    binding.tvTitle.text = meals?.strMeal
                    binding.tvMealDetails.text = meals?.strInstructions
                    binding.tvArea.text = meals?.strArea
                    Glide.with(this).load(meals?.strMealThumb).into(binding.ivLogo)
                    binding.cvmealImage.isVisible=true
                }

                is Result.Error -> {
                    binding.cvProgressbar.isVisible = false
                    Toast.makeText(this, "Error: ${result.exception.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {}
            }

        }
    }


}