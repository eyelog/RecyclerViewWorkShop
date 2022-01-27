package ru.eyelog.recyclerviewworkshop.presentation.fragmentVP.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OffsetItemDecorator : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        rect: Rect,
        view: View,
        parent: RecyclerView,
        s: RecyclerView.State
    ) {
//        parent.adapter?.let { adapter ->
//            parent.clipToPadding = false
//            parent.clipChildren = false
//            when (parent.getChildAdapterPosition(view)) {
//                adapter.itemCount - 1 -> {
//                    parent.setPadding(256, 0, 32, 0)
//                }
//                else -> {
//                    parent.setPadding(32, 0, 256, 0)
//                }
//            }
//        }

//        parent.adapter?.let { adapter ->
//            parent.clipToPadding = false
//            parent.clipChildren = false
//            parent.setPadding(32, 0, 256, 0)
//        }

//        parent.adapter?.let { adapter ->
//            with(rect) {
//                top = 0
//                left = 32
//                right = 32
//                bottom = 0
//            }
//        }

        parent.adapter?.let { adapter ->
            parent.clipToPadding = false
            parent.clipChildren = false
            when (parent.getChildAdapterPosition(view)) {
                adapter.itemCount - 1 -> {
                    with(rect) {
                        top = 0
                        left = 32
                        right = 32
                        bottom = 0
                    }
                }
                else -> {
                    with(rect) {
                        top = 0
                        left = 32
                        right = 256
                        bottom = 0
                    }
                }
            }
        }
    }
}