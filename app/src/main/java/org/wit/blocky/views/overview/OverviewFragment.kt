package org.wit.blocky.views.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.overview_fragment.*
import org.wit.blocky.R
import org.wit.blocky.decorators.EventDecorator

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)

        calendarView.state().edit().setMaximumDate(CalendarDay.today()).commit()
        calendarView.setOnDateChangedListener { _, _, _ ->
            calendarView.addDecorator(
                EventDecorator(context!!, R.color.colorPrimary, listOf(calendarView.selectedDate))
            )
        }
    }

}
