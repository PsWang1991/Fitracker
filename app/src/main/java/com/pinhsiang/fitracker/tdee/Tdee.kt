package com.pinhsiang.fitracker.tdee

class Tdee(
    val gender: Int,
    val age: Float,
    val height: Float,
    val weight: Float
) {

    private val genderBias = when (gender) {
        MALE -> BIAS_MALE
        else -> BIAS_FEMALE
    }

    // bmr = basal metabolic rate
    private val bmr =
        height.times(COEFFICIENT_HEIGHT) + weight.times(COEFFICIENT_WEIGHT) - age.times(COEFFICIENT_AGE) + genderBias

    fun getBmr(): Int {
        return bmr.toInt()
    }

    /**
     *  Sed = Sedentary
     */
    fun getSed(): Int {
        val tdeeSed = when (gender) {
            MALE -> SEDENTARY_MALE
            else -> SEDENTARY_FEMALE
        }
        return bmr.times(tdeeSed).toInt()
    }

    /**
     *  L = Light Exercise
     */
    fun getL(): Int {
        val tdeeL = when (gender) {
            MALE -> LIGHT_EXERCISE_MALE
            else -> LIGHT_EXERCISE_FEMALE
        }
        return bmr.times(tdeeL).toInt()
    }

    /**
     *  M = Moderate Exercise
     */
    fun getM(): Int {
        val tdeeM = when (gender) {
            MALE -> MODERATE_EXERCISE_MALE
            else -> MODERATE_EXERCISE_FEMALE
        }
        return bmr.times(tdeeM).toInt()
    }

    /**
     *  H = Heavy Exercise
     */
    fun getH(): Int {
        val tdeeH = when (gender) {
            MALE -> HEAVY_EXERCISE_MALE
            else -> HEAVY_EXERCISE_FEMALE
        }
        return bmr.times(tdeeH).toInt()
    }

    fun getAthlete(): Int {
        val tdeeAthlete = when (gender) {
            MALE -> ATHLETE_MALE
            else -> ATHLETE_FEMALE
        }
        return bmr.times(tdeeAthlete).toInt()
    }

    companion object {

        /**
         *  Gender
         */
        const val MALE = 0
        const val FEMALE = 1

        /**
         *  Coefficients
         */
        private const val BIAS_MALE = 5f
        private const val BIAS_FEMALE = -161f
        private const val COEFFICIENT_AGE = 4.92f
        private const val COEFFICIENT_WEIGHT = 9.99f
        private const val COEFFICIENT_HEIGHT = 6.25f

        private const val SEDENTARY_MALE = 1.2f
        private const val SEDENTARY_FEMALE = 1.1f
        private const val LIGHT_EXERCISE_MALE = 1.375f
        private const val LIGHT_EXERCISE_FEMALE = 1.275f
        private const val MODERATE_EXERCISE_MALE = 1.55f
        private const val MODERATE_EXERCISE_FEMALE = 1.35f
        private const val HEAVY_EXERCISE_MALE = 1.725f
        private const val HEAVY_EXERCISE_FEMALE = 1.525f
        private const val ATHLETE_MALE = 1.9f
        private const val ATHLETE_FEMALE = 1.8f
    }
}