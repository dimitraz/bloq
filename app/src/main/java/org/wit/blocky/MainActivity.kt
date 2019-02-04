package org.wit.blocky

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.wit.blocky.main.MainApp

class MainActivity : AppCompatActivity() {
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app = application as MainApp

        calendarView.setOnDateChangedListener { _, _, _ ->
            calendarView.addDecorator(
                EventDecorator(R.color.colorPrimary, listOf(calendarView.selectedDate))
            )
        }
    }

}
