package ru.eyelog.recyclerviewworkshop.presentation.pragmentVP.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class HorizontalMarginSidePageTransformer(
    private val itemsCount: Int
) : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val viewPager = requireViewPager(page)
        val offset = position * -2
        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.translationX = -offset
            } else {
                page.translationX = offset
            }
        } else {
            page.translationY = offset
        }

        when (viewPager.currentItem) {
            itemsCount - 1 -> {
                with(viewPager) {
                    setPadding(256, 0, 0, 0)
                }
            }
            else -> {
                with(viewPager) {
                    setPadding(0, 0, 256, 0)
                }
            }
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
}