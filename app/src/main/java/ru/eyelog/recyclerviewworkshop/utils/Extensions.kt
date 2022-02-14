package ru.eyelog.recyclerviewworkshop.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)

fun Number.toDip(): Float = this.toFloat() / Resources.getSystem().displayMetrics.density

fun Number.dip2px(): Int = (this.toInt() * Resources.getSystem().displayMetrics.density).toInt()