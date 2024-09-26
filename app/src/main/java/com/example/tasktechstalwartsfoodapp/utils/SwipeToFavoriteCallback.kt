package com.example.tasktechstalwartsfoodapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktechstalwartsfoodapp.R

abstract class SwipeToFavoriteCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val favoriteIcon = ContextCompat.getDrawable(context, R.drawable.baseline_add_shopping_cart_24)!! // Heart icon
    private val background = ColorDrawable(ContextCompat.getColor(context, R.color.green_success)) // Green background

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false // We don't support item movement here
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // To be implemented in the RecyclerView setup
    }

    override fun onChildDraw(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val iconMargin = (itemView.height - favoriteIcon.intrinsicHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconBottom = iconTop + favoriteIcon.intrinsicHeight

        if (dX > 0) {
            // Draw green background and heart icon when swiping right
            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
            favoriteIcon.setBounds(
                itemView.left + iconMargin,
                iconTop,
                itemView.left + iconMargin + favoriteIcon.intrinsicWidth,
                iconBottom
            )
            background.draw(canvas)
            favoriteIcon.draw(canvas)
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
