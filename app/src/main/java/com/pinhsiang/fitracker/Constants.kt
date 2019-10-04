package com.pinhsiang.fitracker

// Firebase
const val USER = "user"
const val IN_BODY = "in-body"
const val WORKOUT = "workout"
const val NUTRITION = "nutrition"
const val UID = "uid"

// Overview and Analysis
const val MILLISECOND_PER_DAY = 86400000L
const val DAYS_PER_1M = 32L
const val DAYS_PER_3M = 93L
const val DAYS_PER_6M = 186L
const val DAYS_PER_1Y = 366L
const val PERIOD_1M = MILLISECOND_PER_DAY * DAYS_PER_1M
const val PERIOD_3M = MILLISECOND_PER_DAY * DAYS_PER_3M
const val PERIOD_6M = MILLISECOND_PER_DAY * DAYS_PER_6M
const val PERIOD_1Y = MILLISECOND_PER_DAY * DAYS_PER_1Y
const val TAG_1M = "1M"
const val TAG_3M = "3M"
const val TAG_6M = "6M"
const val TAG_1Y = "1Y"
const val TAG_ALL = "ALL"


// Line chart parameters
const val CHART_AXIS_TEXT_SIZE = 14f
const val CHART_X_AXIS_LABEL_ROTATION = 45f
const val Y_AXIS_GRID_LINE_WIDTH = 2f