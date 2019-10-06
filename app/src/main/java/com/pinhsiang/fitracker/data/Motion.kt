package com.pinhsiang.fitracker.data

import android.graphics.drawable.Drawable
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.util.Util.getDrawable


data class Motion(

    val image: Drawable? = getDrawable(R.drawable.motion_bench_press),
    val motion: String
)