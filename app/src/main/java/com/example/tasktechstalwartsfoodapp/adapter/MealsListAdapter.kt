package com.example.tasktechstalwartsfoodapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tasktechstalwartsfoodapp.R
import com.example.tasktechstalwartsfoodapp.UI.food_details.FoodDetailsActivity
import com.example.tasktechstalwartsfoodapp.databinding.LayoutFoodItemBinding
import com.example.tasktechstalwartsfoodapp.model.MealsByCategory

class MealsListAdapter(val context: Context,
                       var list:MutableList<MealsByCategory>,
                       private val listener: OnItemLongClickListener): RecyclerView.Adapter<MealsListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding:LayoutFoodItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutFoodItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=list[position]
        Glide.with(context).load(item.strMealThumb).error(R.drawable.logo).into(holder.binding.ivFoodImg)
        holder.binding.tvTitle.setText(item.strMeal)

        holder.itemView.setOnClickListener {
            val intent= Intent(context, FoodDetailsActivity::class.java)
            intent.putExtra("id",item)
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }

        holder.itemView.setOnLongClickListener {
            listener.onItemLongClick(item, position) // Notify the listener
            true // Return true to indicate the long click was handled
        }
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }



    interface OnItemLongClickListener {
        fun onItemLongClick(item: MealsByCategory, position: Int)
    }

}