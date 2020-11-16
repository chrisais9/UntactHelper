package kr.koohyongmo.untacthelper.timetable.ui.fragment

import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment

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
    }
}