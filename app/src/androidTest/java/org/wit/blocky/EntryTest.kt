package org.wit.blocky

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EntryTest {

    @Rule
    @JvmField
    public val rule = ActivityTestRule(LoginActivity::class.java)

    private val homeMonitor =
        getInstrumentation().addMonitor(MainActivity::class.java!!.name, null, false)

    private val username = "d@d.com"
    private val correctPassword = "hello1"

    @Test
    fun testCreateEntry() {
        Espresso.onView((withId(R.id.userEmail)))
            .perform(ViewActions.typeText(username))

        Espresso.onView(withId(R.id.userPassword))
            .perform(ViewActions.typeText(correctPassword))

        Espresso.closeSoftKeyboard()

        Espresso.onView(withId(R.id.logIn))
            .perform(ViewActions.click())

        Thread.sleep(10000)

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open()) // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.navigationView))
            .perform(NavigationViewActions.navigateTo(R.id.destination_entry))

        Thread.sleep(5000)

        // Check that you Activity was opened.
        Espresso.onView(withId(R.id.item_save))
            .perform(ViewActions.click())

        Thread.sleep(5000)

        val mainActivity = getInstrumentation().waitForMonitorWithTimeout(homeMonitor, 10000)
        Assert.assertNotNull(mainActivity)
    }
}