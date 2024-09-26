package com.example.tasktechstalwartsfoodapp.UI.homefragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.findColumnIndexBySuffix
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.UI.authentication.LoginOrRegActivity
import com.example.tasktechstalwartsfoodapp.adapter.MealsListAdapter
import com.example.tasktechstalwartsfoodapp.databinding.FragmentHomeBinding
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart
import com.example.tasktechstalwartsfoodapp.viewmodel.HomeFragmentViewModel
import com.example.tasktechstalwartsfoodapp.model.Result
import com.example.tasktechstalwartsfoodapp.utils.SwipeToDeleteCallback
import com.example.tasktechstalwartsfoodapp.utils.SwipeToFavoriteCallback
import com.example.tasktechstalwartsfoodapp.viewmodel.FirebaseAuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(),MealsListAdapter.OnItemLongClickListener {


    lateinit var binding:FragmentHomeBinding
    private val viewModel:HomeFragmentViewModel by activityViewModels()
    lateinit var mealsList:ArrayList<MealsByCategory>
    lateinit var adapter: MealsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mealsList.isEmpty()) {
            observeMealsList()
        } else {
            adapter.notifyDataSetChanged()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container, false)
        Log.d("HomeFragment", "onCreateView called")

        mealsList=ArrayList()
        Log.d("HomeFragment", mealsList.toString())

        setupSwipeRefreshLayout()


        binding.rvMealsList.layoutManager=LinearLayoutManager(requireContext())
        adapter= MealsListAdapter(requireContext(),mealsList,this)
        binding.rvMealsList.adapter=adapter
        val swipeHandler = object : SwipeToFavoriteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position >= 0 && position < mealsList.size) {
                    val meal = mealsList[position]
                    binding.rvMealsList.adapter?.notifyItemChanged(position)
                    showAlertDialog(meal, position)
                }
            }
        }


        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvMealsList)

        binding.ivLogout.setOnClickListener {
            showAlertDialogLogout()
        }

        return binding.root
    }

    private fun observeMealsList() {
        binding.cvProgressbar.isVisible=true
        if (mealsList.isEmpty()) {
            viewModel.popularItemsLiveData.observe(viewLifecycleOwner) { result ->
                binding.cvProgressbar.isVisible = false
                when (result) {
                    is Result.Success -> {
                        mealsList.addAll(result.data)
                        adapter.notifyDataSetChanged()
                    }
                    is Result.Error -> {
                        Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            binding.cvProgressbar.isVisible = false
            adapter.notifyDataSetChanged() // If data is cached, update the adapter
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        binding.cvProgressbar.isVisible = true
        mealsList.clear()
        lifecycleScope.launch {
                viewModel.fetchPopularItems()
        }
        binding.swipeRefreshLayout.isRefreshing = false
        adapter.notifyDataSetChanged()
    }

    private fun showAlertDialogLogout() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Action Required")
        builder.setMessage("Do you want to logout?")
        builder.setPositiveButton("Logout") { dialog, _ ->
            FirebaseAuth.getInstance().signOut()
            viewModel.removeAllFavMeal()
            viewModel.removeAllCartMeal()
            dialog.dismiss()
            startActivity(Intent(requireContext(),LoginOrRegActivity::class.java))
            requireActivity().finish()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }


    private fun showAlertDialog(item: MealsByCategory, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Action Required")
        builder.setMessage("Would you like add ${item.strMeal} in Cart?")
        builder.setPositiveButton("Add") { dialog, _ ->
            viewModel.addMealToCart(MealsByCategoryCart(
                idMeal = item.idMeal,
                strMeal = item.strMeal,
                strMealThumb = item.strMealThumb
            ))
            Toast.makeText(requireContext(),"${item.strMeal} added to cart",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onItemLongClick(item: MealsByCategory, position: Int) {
        showAlertDialog(item, position)
    }

}