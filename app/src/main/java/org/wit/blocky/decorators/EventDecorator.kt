package org.wit.blocky.decorators

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import org.wit.blocky.R

class EventDecorator(private val context: Context, private val color: Int, dates: Collection<CalendarDay>) : DayViewDecorator {
    private val dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5f, R.color.colorPrimary))
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.material_grey_300)!!)
    }
}