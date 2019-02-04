package org.wit.blocky

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView.setOnDateChangedListener { widget, date, selected ->
            calendarView.addDecorator(
                EventDecorator(R.color.colorPrimary, listOf(calendarView.selectedDate))
            )
        }
    }

}
