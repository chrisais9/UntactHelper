package kr.koohyongmo.untacthelper.calendar.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import com.applandeo.materialcalendarview.EventDay
import kotlinx.android.synthetic.main.fragment_calendar.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.calendar.Utils.CalendarUtils
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import java.util.*
import kotlin.collections.ArrayList

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
        monthlyCalendar()
    }

    private fun monthlyCalendar() {
        addEvents(2020, 10, 20, "task", Color.parseColor("#2390dc"), Color.WHITE)
    }

    private fun addEvents(year:Int, month:Int, day:Int, text:String, backColor:Int, textColor:Int) {
        var events = ArrayList<EventDay>()
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val drawableText = CalendarUtils.getDrawableText(context, text, null, backColor, textColor, 11)
        events.add(EventDay(calendar, drawableText))
        calendarView.setEvents(events)
    }

}