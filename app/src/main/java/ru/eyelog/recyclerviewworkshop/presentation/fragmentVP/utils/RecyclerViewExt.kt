package ru.eyelog.recyclerviewworkshop.presentation.fragmentVP.utils

import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

@RequiresApi(Build.VERSION_CODES.KITKAT) fun RecyclerView.applyWindowInsetsOnItem() {

    var gestureInsets: Insets = Insets.NONE

    val onItemTouchListener = object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val touchX = e.x.toInt()
            return gestureInsets.left > touchX || touchX > (rv.width - gestureInsets.right)
        }
    }

    doOnApplyWindowInsets { _, insets, _ ->
        gestureInsets = insets.systemGestureInsets
        return@doOnApplyWindowInsets insets
    }

    addOnItemTouchListener(onItemTouchListener)
}

@RequiresApi(Build.VERSION_CODES.KITKAT) fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        block(view, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View): Rect {
    return Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
}

@RequiresApi(Build.VERSION_CODES.KITKAT) fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}