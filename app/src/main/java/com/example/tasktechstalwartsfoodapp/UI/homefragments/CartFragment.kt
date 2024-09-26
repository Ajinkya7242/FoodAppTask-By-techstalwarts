package com.example.tasktechstalwartsfoodapp.UI.homefragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.adapter.MealsCartAdapter
import com.example.tasktechstalwartsfoodapp.adapter.MealsListAdapter
import com.example.tasktechstalwartsfoodapp.databinding.FragmentCartBinding
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart
import com.example.tasktechstalwartsfoodapp.utils.SwipeToDeleteCallback
import com.example.tasktechstalwartsfoodapp.utils.SwipeToFavoriteCallback
import com.example.tasktechstalwartsfoodapp.utils.SwipeToHandleCallback
import com.example.tasktechstalwartsfoodapp.viewmodel.CartVIewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CartFragment : Fragment(), MealsCartAdapter.OnItemLongClickListener {

    lateinit var binding:FragmentCartBinding
    private val viewModel: CartVIewModel by viewModels()
    var cartMealsList:ArrayList<MealsByCategoryCart> =ArrayList<MealsByCategoryCart>()
    lateinit var adapter: MealsCartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCartBinding.inflate(layoutInflater,container,false)

        adapter=MealsCartAdapter(requireContext(),cartMealsList,this)


        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter

        val swipeHandler = object : SwipeToHandleCallback(requireContext()) {
            override fun onDeleteSwiped(position: Int) {
                if (position >= 0 && position < cartMealsList.size) {
                    val meal = cartMealsList[position]
                    adapter.removeAt(position)
                    viewModel.removeCartMeal(meal)
                } else {
                    adapter.notifyItemChanged(position)
                }
            }

            override fun onFavoriteSwiped(position: Int) {
                if (position >= 0 && position < cartMealsList.size) {
                    val meal = cartMealsList[position]
                    binding.rvCart.adapter?.notifyItemChanged(position)
                    showAlertDialog(meal, position)
                }
            }
        }


        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvCart)



        lifecycleScope.launch {
            viewModel.mealList.collect { mealList ->
                Timber.d(mealList.toString())
                if (mealList.isNotEmpty()) {
                    cartMealsList.clear()
                    cartMealsList.addAll(mealList)
                    adapter.notifyDataSetChanged()
                }
            }
        }


        return binding.root
    }

    private fun showAlertDialog(item: MealsByCategoryCart, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Action Required")
        builder.setMessage("Would you like add ${item.strMeal} in Favorites?")
        builder.setPositiveButton("Add") { dialog, _ ->
            viewModel.addFavMeal(MealsByCategory(
                idMeal = item.idMeal,
                strMeal = item.strMeal,
                strMealThumb = item.strMealThumb
            ))
            Toast.makeText(requireContext(),"${item.strMeal} added to Favorites", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onItemLongClick(item: MealsByCategoryCart, position: Int) {
        showAlertDialog(item, position)

    }


}