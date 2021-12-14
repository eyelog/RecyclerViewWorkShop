package ru.eyelog.recyclerviewworkshop.presentation.pragmentVP.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class MovePagerTransformer(
    private val itemsCount: Int
) : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val viewPager = requireViewPager(page)
        var offset = 32.toPx * position
        when (viewPager.currentItem) {
            itemsCount - 1 -> offset += 32.toPx
            else -> offset -= 32.toPx
        }
        page.translationX = offset
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