package org.wit.blocky

import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @Rule
    @JvmField
    public val rule = ActivityTestRule(LoginActivity::class.java)

    private val mainMonitor =
        getInstrumentation().addMonitor(MainActivity::class.java!!.name, null, false)
    private val loginMonitor =
        getInstrumentation().addMonitor(LoginActivity::class.java!!.name, null, false)

    private val username = "l@l.com"
    private val correctPassword = "hello1"
    private val wrongPassword = "test"

    @Test
    fun testLoginSuccessful() {
        Log.e("@Test", "Performing login success test")

        Espresso.onView((withId(R.id.userEmail)))
            .perform(ViewActions.typeText(username))

        Espresso.onView(withId(R.id.userPassword))
            .perform(ViewActions.typeText(correctPassword))

        Espresso.closeSoftKeyboard()

        Espresso.onView(withId(R.id.logIn))
            .perform(ViewActions.click())

        val mainActivity = getInstrumentation().waitForMonitorWithTimeout(mainMonitor, 10000)
        assertNotNull(mainActivity)
    }

    @Test
    fun testLoginFailure() {
        Log.e("@Test", "Performing login failure test")

        Espresso.closeSoftKeyboard()

        Espresso.onView((withId(R.id.userEmail)))
            .perform(ViewActions.typeText(username))

        Espresso.onView(withId(R.id.userPassword))
            .perform(ViewActions.typeText(wrongPassword))

        Espresso.closeSoftKeyboard()

        Espresso.onView(withId(R.id.logIn))
            .perform(ViewActions.click())

        Thread.sleep(3000)

        val loginActivity = getInstrumentation().waitForMonitorWithTimeout(loginMonitor, 5000)
        assertNotNull(loginActivity)
    }
}