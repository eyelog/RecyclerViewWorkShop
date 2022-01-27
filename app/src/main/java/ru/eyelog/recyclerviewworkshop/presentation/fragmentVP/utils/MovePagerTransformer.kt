package ru.eyelog.recyclerviewworkshop.presentation.fragmentVP.utils

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class MovePagerTransformer(
    private val itemsCount: Int
) : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val viewPager = requireViewPager(page)
        var offset = page.dip2px(1) * position
//        var offset =  position
        Log.i("Logcat", "position $position")
        Log.i("Logcat", "offset eternal $offset")

        when (viewPager.currentItem) {
            0 -> {
                offset -= page.dip2px(32) // left
            }
            itemsCount - 1 -> {
                offset += page.dip2px(32) // right
            }
        }
        Log.i("Logcat", "offset final $offset")
        Log.i("Logcat", "    ")

//        page.x = offset
//        page.translationX = offset

        if (isTheFloatInteger(position)) {
            page.translationX = offset
        }
    }

    private fun requireViewPager(page: View): ViewPager2 {
        val pageParent = page.parent
        val recyclerParent = pageParent.parent
        if (pageParent is RecyclerView && recyclerParent is ViewPager2) {
            return recyclerParent
        }
        throw IllegalStateException(
            "Expected the page view to be managed by a ViewPager2 instance."
        )
    }

    private fun isTheFloatInteger(num: Float): Boolean {
        val a = num.toInt().toFloat()
        return num == a
    }
}