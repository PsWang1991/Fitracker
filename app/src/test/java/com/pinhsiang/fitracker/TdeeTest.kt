package com.pinhsiang.fitracker

import com.pinhsiang.fitracker.tdee.Tdee
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TdeeTest {

    @Test
    fun testFemaleTdee() {

        val testTdee = Tdee(gender = Tdee.FEMALE, weight = 53f, height = 165f, age = 28f)

        val resultBmr: Int = testTdee.bmr()
        val resultSedentary: Int = testTdee.sedentary()
        val resultLightExercise: Int = testTdee.lightExercise()
        val resultModerateExercise: Int = testTdee.moderateExercise()
        val resultHeavyExercise: Int = testTdee.heavyExercise()
        val resultAthlete: Int = testTdee.athlete()

        assertThat(resultBmr, `is`(1261))
        assertThat(resultSedentary, `is`(1388))
        assertThat(resultLightExercise, `is`(1608))
        assertThat(resultModerateExercise, `is`(1703))
        assertThat(resultHeavyExercise, `is`(1924))
        assertThat(resultAthlete, `is`(2271))
    }

    @Test
    fun testFemaleTdee2() {

        val testTdee = Tdee(gender = Tdee.FEMALE, weight = 525f, height = 170f, age = 50f)

        val resultBmr: Int = testTdee.bmr()
        val resultSedentary: Int = testTdee.sedentary()
        val resultLightExercise: Int = testTdee.lightExercise()
        val resultModerateExercise: Int = testTdee.moderateExercise()
        val resultHeavyExercise: Int = testTdee.heavyExercise()
        val resultAthlete: Int = testTdee.athlete()

        assertThat(resultBmr, `is`(5900))
        assertThat(resultSedentary, `is`(6490))
        assertThat(resultLightExercise, `is`(7522))
        assertThat(resultModerateExercise, `is`(7965))
        assertThat(resultHeavyExercise, `is`(8997))
        assertThat(resultAthlete, `is`(10620))
    }

    @Test
    fun testMaleTdee() {

        val testTdee = Tdee(gender = Tdee.MALE, weight = 53f, height = 165f, age = 28f)

        val resultBmr: Int = testTdee.bmr()
        val resultSedentary: Int = testTdee.sedentary()
        val resultLightExercise: Int = testTdee.lightExercise()
        val resultModerateExercise: Int = testTdee.moderateExercise()
        val resultHeavyExercise: Int = testTdee.heavyExercise()
        val resultAthlete: Int = testTdee.athlete()

        assertThat(resultBmr, `is`(1427))
        assertThat(resultSedentary, `is`(1713))
        assertThat(resultLightExercise, `is`(1963))
        assertThat(resultModerateExercise, `is`(2213))
        assertThat(resultHeavyExercise, `is`(2463))
        assertThat(resultAthlete, `is`(2713))
        assertNotEquals(resultAthlete, 0)
    }

    @Test
    fun testMaleTdee2() {

        val testTdee = Tdee(gender = Tdee.MALE, weight = 590f, height = 180f, age = 32f)

        val resultBmr: Int = testTdee.bmr()
        val resultSedentary: Int = testTdee.sedentary()
        val resultLightExercise: Int = testTdee.lightExercise()
        val resultModerateExercise: Int = testTdee.moderateExercise()
        val resultHeavyExercise: Int = testTdee.heavyExercise()
        val resultAthlete: Int = testTdee.athlete()

        assertThat(resultBmr, `is`(6866))
        assertThat(resultSedentary, `is`(8239))
        assertThat(resultLightExercise, `is`(9441))
        assertThat(resultModerateExercise, `is`(10643))
        assertThat(resultHeavyExercise, `is`(11844))
        assertThat(resultAthlete, `is`(13046))
        assertNotEquals(resultAthlete, 0)
    }

    @Test
    fun reverseTestMaleTdee() {

        val testTdee = Tdee(gender = Tdee.MALE, weight = 62.3f, height = 173.8f, age = 30.5f)

        val resultBmr: Int = testTdee.bmr()
        val resultSedentary: Int = testTdee.sedentary()
        val resultLightExercise: Int = testTdee.lightExercise()
        val resultModerateExercise: Int = testTdee.moderateExercise()
        val resultHeavyExercise: Int = testTdee.heavyExercise()
        val resultAthlete: Int = testTdee.athlete()

        assertThat(resultBmr, `is`(1563))
        assertNotEquals(resultBmr, 0)
        assertNotEquals(resultSedentary, 1563)
        assertNotEquals(resultLightExercise, 1563)
        assertNotEquals(resultModerateExercise, 1563)
        assertNotEquals(resultHeavyExercise, 1563)
        assertNotEquals(resultAthlete, 1563)
        assertNotEquals(resultAthlete, 0)
    }
}