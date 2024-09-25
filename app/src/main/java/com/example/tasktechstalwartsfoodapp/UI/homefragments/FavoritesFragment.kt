package com.example.tasktechstalwartsfoodapp.UI.homefragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.adapter.MealsListAdapter
import com.example.tasktechstalwartsfoodapp.databinding.FragmentFavoritesBinding
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.utils.SwipeToDeleteCallback
import com.example.tasktechstalwartsfoodapp.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FavoritesFragment : Fragment() ,MealsListAdapter.OnItemLongClickListener{

    lateinit var binding:FragmentFavoritesBinding
    private val viewModel:FavoritesViewModel by viewModels()
    var favMealsList:ArrayList<MealsByCategory> =ArrayList<MealsByCategory>()
    lateinit var adapter:MealsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentFavoritesBinding.inflate(layoutInflater,container,false)

        adapter=MealsListAdapter(requireContext(),favMealsList, this)


        binding.rvFavMeals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavMeals.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position >= 0 && position < favMealsList.size) {
                    val meal = favMealsList[position]
                    adapter.removeAt(position)
                    viewModel.removeFavMeal(meal)
                } else {
                    adapter.notifyItemChanged(position)
                }
            }
        }


        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvFavMeals)

        lifecycleScope.launch {
            viewModel.mealList.collect { mealList ->
                Timber.d(mealList.toString())
                if (mealList.isNotEmpty()) {
                    favMealsList.clear()
                    favMealsList.addAll(mealList)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        return binding.root
    }

    override fun onItemLongClick(item: MealsByCategory, position: Int) {

    }




}