package com.pinhsiang.fitracker.util

import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.util.Util.getString

enum class CurrentFragmentType(val value: String) {
    WORKOUT(getString(R.string.workout)),
    WORKOUT_MOTION(getString(R.string.workout_motion)),
    WORKOUT_RECORD(getString(R.string.workout_record)),
    WORKOUT_ANALYSIS(getString(R.string.workout_analysis)),
    NUTRITION(getString(R.string.nutrition)),
    NUTRITION_RECORD(getString(R.string.nutrition_record)),
    NUTRITION_ANALYSIS(getString(R.string.nutrition_analysis)),
    INBODY(getString(R.string.inbody)),
    INBODY_RECORD(getString(R.string.inbody_record)),
    INBODY_ANALYSIS(getString(R.string.inbody_analysis)),
    TIMER(getString(R.string.timer)),
    TDEE(getString(R.string.tdee)),
    ONE_RM(getString(R.string.one_rm)),
    RECOMMENDED(getString(R.string.recommended))
}