package org.wit.blocky

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DrawerTest {

    @Rule
    @JvmField
    public val rule = ActivityTestRule(LoginActivity::class.java)

    private val homeMonitor =
        getInstrumentation().addMonitor(MainActivity::class.java!!.name, null, false)

    private val username = "d@d.com"
    private val correctPassword = "hello1"

    private fun login() {
        onView((withId(R.id.userEmail)))
            .perform(ViewActions.typeText(username))
        onView(withId(R.id.userPassword))
            .perform(ViewActions.typeText(correctPassword))
        closeSoftKeyboard()
        onView(withId(R.id.logIn))
            .perform(ViewActions.click())
        Thread.sleep(10000)
    }

    @Test
    fun testEntryItem() {
        login()
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.navigationView))
            .perform(NavigationViewActions.navigateTo(R.id.destination_entry))
        Thread.sleep(4000)
        onView(withId(R.id.destination_entry)).check(matches(isDisplayed()))
    }

    @Test
    fun testOverviewItem() {
        login()
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.navigationView))
            .perform(NavigationViewActions.navigateTo(R.id.destination_overview))
        Thread.sleep(4000)
        onView(withId(R.id.destination_overview)).check(matches(isDisplayed()))
    }

    @Test
    fun testUsersItem() {
        login()
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.navigationView))
            .perform(NavigationViewActions.navigateTo(R.id.destination_users))
        Thread.sleep(4000)
        onView(withId(R.id.destination_users)).check(matches(isDisplayed()))
    }

    @Test
    fun testProfileItem() {
        login()
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.navigationView))
            .perform(NavigationViewActions.navigateTo(R.id.destination_profile))
        Thread.sleep(4000)
        onView(withId(R.id.destination_profile)).check(matches(isDisplayed()))
    }
}