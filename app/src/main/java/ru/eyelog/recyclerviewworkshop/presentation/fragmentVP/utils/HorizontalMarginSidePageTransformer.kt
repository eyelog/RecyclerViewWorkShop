package ru.eyelog.recyclerviewworkshop.presentation.fragmentVP.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class HorizontalMarginSidePageTransformer(
    private val itemsCount: Int
) : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val viewPager = requireViewPager(page)
        val offset = position * -2
        page.translationY = offset

        when (viewPager.currentItem) {
            itemsCount - 1 -> {
                with(viewPager) {
                    setPadding(128, 0, 0, 0)
                }
            }
            0 -> {
                with(viewPager) {
                    setPadding(0, 0, 128, 0)
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