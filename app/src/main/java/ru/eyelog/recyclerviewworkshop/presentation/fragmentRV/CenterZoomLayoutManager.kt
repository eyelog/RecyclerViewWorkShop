package ru.eyelog.recyclerviewworkshop.presentation.fragmentRV

import android.content.Context
import android.graphics.Color
import android.graphics.ColorSpace
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import ru.eyelog.recyclerviewworkshop.R
import kotlin.math.abs

class CenterZoomLayoutManager : LinearLayoutManager {
    private val mShrinkAmount = 0.30f
    private val mShrinkDistance = 1.0f

    constructor(context: Context?) : super(context) {}

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        val scrolled = super.scrollVerticallyBy(dy, recycler, state)
        updateItemsFit()
        return scrolled
    }

    fun updateItemsFit() {
        val midpoint = height / 2f
        val d0 = 0f
        val d1 = mShrinkDistance * midpoint
        val s0 = 1f
        val s1 = 1f - mShrinkAmount

        for (i in 0 until childCount) {
            val child: View? = getChildAt(i)
            child?.let {
                val childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2f
                val d = d1.coerceAtMost(abs(midpoint - childMidpoint))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                child.scaleX = scale
                child.scaleY = scale

                val opacity = checkOpacity(abs(childMidpoint - midpoint) / midpoint)
                val stringOpacity = "%02x".format((opacity * 255).toInt())
                val backColor = "#${stringOpacity}ffffff"
                child.findViewById<View>(R.id.socView).setBackgroundColor(
                    Color.parseColor(backColor)
                )
            }
        }
    }

    private fun checkOpacity(fl: Float): Float {
        return if (fl <= 1f){
            fl
        } else {
            1f
        }
    }
}