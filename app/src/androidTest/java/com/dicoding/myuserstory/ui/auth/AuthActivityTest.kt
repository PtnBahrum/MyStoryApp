package com.dicoding.myuserstory.ui.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import com.dicoding.myuserstory.R
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AuthActivityTest{

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(AuthActivity::class.java)

    @Test
    fun testLoginSuccess() {
        onView(withId(R.id.ed_email_login)).perform(typeText("kaliber1933@gmail.com"))
        onView(withId(R.id.ed_password_login)).perform(typeText("kaliber013"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()))
        onView(withId(R.id.exit)).perform(click())
        onView(withId(R.id.auth_layout)).check(matches(isDisplayed()))
    }

}