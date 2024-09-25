package com.example.tasktechstalwartsfoodapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.UI.food_details.FoodDetailsActivity
import com.example.tasktechstalwartsfoodapp.adapter.MealsListAdapter.OnItemLongClickListener
import com.example.tasktechstalwartsfoodapp.databinding.LayoutCartItemBinding
import com.example.tasktechstalwartsfoodapp.databinding.LayoutFoodItemBinding
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory
import com.example.tasktechstalwartsfoodapp.model.MealsByCategoryCart

class MealsCartAdapter (val context: Context, var list:MutableList<MealsByCategoryCart>,private val listener: OnItemLongClickListener): RecyclerView.Adapter<MealsCartAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: LayoutCartItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutCartItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=list[position]
        Glide.with(context).load(item.strMealThumb).error(R.drawable.logo).into(holder.binding.ivFoodImg)
        holder.binding.tvTitle.setText(item.strMeal)

        holder.itemView.setOnLongClickListener {
            listener.onItemLongClick(item, position) // Notify the listener
            true // Return true to indicate the long click was handled
        }
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(item: MealsByCategoryCart, position: Int)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

}