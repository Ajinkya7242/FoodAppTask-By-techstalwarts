package com.example.tasktechstalwartsfoodapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktechstalwartsfoodapp.R

abstract class SwipeToHandleCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.baseline_delete_forever_24)!!
    private val favoriteIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24)!!
    private val deleteBackground: ColorDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary)) // Red background for delete
    private val favoriteBackground: ColorDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.green_success)) // Green background for favorite

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false // No movement support
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            onDeleteSwiped(position)
        } else if (direction == ItemTouchHelper.RIGHT) {
            onFavoriteSwiped(position)
        }
    }

    // Abstract methods to be implemented in the Fragment/Activity
    abstract fun onDeleteSwiped(position: Int)
    abstract fun onFavoriteSwiped(position: Int)

    override fun onChildDraw(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

        // Handle left swipe (Delete)
        if (dX < 0) {
            val iconTop = itemView.top + iconMargin
            val iconBottom = iconTop + deleteIcon.intrinsicHeight
            deleteBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, iconTop, itemView.right - iconMargin, iconBottom)
            deleteBackground.draw(canvas)
            deleteIcon.draw(canvas)
        }

        // Handle right swipe (Favorite)
        if (dX > 0) {
            val iconTop = itemView.top + iconMargin
            val iconBottom = iconTop + favoriteIcon.intrinsicHeight
            favoriteBackground.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
            favoriteIcon.setBounds(itemView.left + iconMargin, iconTop, itemView.left + iconMargin + favoriteIcon.intrinsicWidth, iconBottom)
            favoriteBackground.draw(canvas)
            favoriteIcon.draw(canvas)
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
