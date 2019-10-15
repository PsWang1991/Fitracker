package com.pinhsiang.fitracker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TdeeCalculatorInstrumentedTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule
        var activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Test
    fun testTdee() {

        onView(withId(R.id.drawer_layout))
//            .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
            .perform(DrawerActions.open())
        Thread.sleep(SLEEP_TIME)

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_tdee))
        Thread.sleep(SLEEP_TIME)

        onView(withId(R.id.radio_male)).perform(click())
        Thread.sleep(SLEEP_TIME)

        onView(withId(R.id.text_tdee_age)).perform(typeText(INPUT_AGE), closeSoftKeyboard())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.text_tdee_age)).check(matches(withText(INPUT_AGE)))

        onView(withId(R.id.text_tdee_weight)).perform(typeText(INPUT_WEIGHT), closeSoftKeyboard())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.text_tdee_weight)).check(matches(withText(INPUT_WEIGHT)))

        onView(withId(R.id.text_tdee_height)).perform(typeText(INPUT_HEIGHT), closeSoftKeyboard())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.text_tdee_height)).check(matches(withText(INPUT_HEIGHT)))

        onView(withId(R.id.layout_btn_calculate_tdee)).perform(click())
        Thread.sleep(SLEEP_TIME)

        onView(withId(R.id.text_result_bmr)).check(matches(withText("1,427")))
        onView(withId(R.id.text_result_sedentary)).check(matches(withText("1,713")))
        onView(withId(R.id.text_result_light_exercise)).check(matches(withText("1,963")))
        onView(withId(R.id.text_result_moderate_exercise)).check(matches(withText("2,213")))
        onView(withId(R.id.text_result_heavy_exercise)).check(matches(withText("2,463")))
        onView(withId(R.id.text_result_athlete)).check(matches(withText("2,713")))
    }

    companion object {
        const val SLEEP_TIME = 1000L
        const val INPUT_AGE = "28"
        const val INPUT_WEIGHT = "53"
        const val INPUT_HEIGHT = "165"
    }
}