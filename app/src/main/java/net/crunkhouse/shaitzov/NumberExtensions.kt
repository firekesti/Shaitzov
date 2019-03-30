package net.crunkhouse.shaitzov

import android.content.Context
import android.util.TypedValue

infix fun Number.dp(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                     this.toFloat(),
                                     context.resources.displayMetrics).toInt()
}
