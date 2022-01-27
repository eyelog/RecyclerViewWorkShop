package ru.eyelog.recyclerviewworkshop.presentation.fragmentVP.utils

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

private const val DEFAULT_PADDING_MULTIPLIER = 2

open class PagerStartSnapHelper : PagerSnapHelper() {

    private var verticalHelper: OrientationHelper? = null
    private var horizontalHelper: OrientationHelper? = null

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        val outputCoords = intArrayOf(0, 0)

        if (layoutManager.canScrollHorizontally()) {
            outputCoords[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        }
        if (layoutManager.canScrollVertically()) {
            outputCoords[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
        }
        return outputCoords
    }

    open fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        return helper.getDecoratedStart(targetView) - DEFAULT_PADDING_MULTIPLIER * helper.startAfterPadding
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        return verticalHelper ?: OrientationHelper.createVerticalHelper(layoutManager)
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        return horizontalHelper ?: OrientationHelper.createHorizontalHelper(layoutManager)
    }
}