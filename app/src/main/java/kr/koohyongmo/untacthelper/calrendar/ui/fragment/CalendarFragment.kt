package kr.koohyongmo.untacthelper.calrendar.ui.fragment

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.fragment_calendar.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.calrendar.ui.fragment.Decorator.SaturdayDecorator
import kr.koohyongmo.untacthelper.calrendar.ui.fragment.Decorator.SundayDecorator
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import java.util.*

/**
 * Created by KimMinJeong on 11/16/20
 */
class CalendarFragment: BaseFragment() {
    override val layoutResourceID: Int
        get() = R.layout.fragment_calendar
    override val layoutToolbarID: Int
        get() = R.id.toolbar_calendar

    override fun initLayoutAttributes() {
        initCalendar()
    }

    private fun initCalendar() {
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2017, 0, 1))
            .setMaximumDate(CalendarDay.from(2030, 11, 31))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit();

        calendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator()
        )
    }
}