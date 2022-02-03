package ru.eyelog.recyclerviewworkshop.presentation.fragmentRV.localutils

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HideLastDecorator() : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val count = parent.childCount
        for (i in 0 until count) {
            parent.getChildAt(i).visibility = if (count == i - 1) View.INVISIBLE else View.VISIBLE
        }
    }
}