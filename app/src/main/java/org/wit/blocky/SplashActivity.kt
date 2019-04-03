package org.wit.blocky

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var delayHandler: Handler
    private val SPLASH_DELAY: Long = 2000

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize the Handler
        delayHandler = Handler()

        // Navigate with delay
        delayHandler.postDelayed(mRunnable, SPLASH_DELAY)
    }

    public override fun onDestroy() {
        if (delayHandler != null) {
            delayHandler.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}