package org.wit.blocky.views.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.fragment_overview.*
import org.wit.blocky.R
import org.wit.blocky.decorators.EventDecorator
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.entry.FirebaseStore

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel
    private lateinit var app: MainApp
    private lateinit var fireStore: FirebaseStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = activity!!.application as MainApp
        fireStore = FirebaseStore(context!!)
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)

        calendarView.state().edit().setMaximumDate(CalendarDay.today()).commit()
        calendarView.setOnDateChangedListener { _, date, _ ->
            val bundle = bundleOf(
                "date" to CalendarDate(date.day, date.month, date.year)
            )
            Navigation.findNavController(view!!).navigate(R.id.overview_to_entry, bundle)
        }

        val dates: MutableList<CalendarDay> = mutableListOf()
        fireStore.fetchAllEntries(app.currentUser.authId) {
            for (item in fireStore.allEntries) {
                val date = CalendarDay.from(item.date.year, item.date.month, item.date.day)
                dates.add(date)
            }
            calendarView.addDecorator(
                EventDecorator(context!!, R.color.colorPrimary, dates)
            )
        }
    }
}
