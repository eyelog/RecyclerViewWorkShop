package ru.eyelog.recyclerviewworkshop.presentation.fragmentVP.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)

// returns dip(dp) dimension value in pixels
fun Context.dip2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.dip2px(value: Float): Int = (value * resources.displayMetrics.density).toInt()

// converts px value into dip or sp
fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density

inline fun View.dip2px(value: Int): Int = context.dip2px(value)

inline fun View.dip2px(value: Float): Int = context.dip2px(value)

inline fun View.px2dip(px: Int): Float = context.px2dip(px)