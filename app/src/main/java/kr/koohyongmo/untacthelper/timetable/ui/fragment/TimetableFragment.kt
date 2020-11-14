package kr.koohyongmo.untacthelper.timetable.ui.fragment

import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_timetable.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment

/**
 * Created by KooHyongMo on 11/14/20
 */
class TimetableFragment: BaseFragment() {
    override val layoutResourceID: Int
        get() = R.layout.fragment_timetable
    override val layoutToolbarID: Int
        get() = R.id.toolbar_timetable

    override fun initLayoutAttributes() {
        initTimeTable()
    }

    private fun initTimeTable() {
        val schedules = arrayListOf(
            Schedule().apply {
                classTitle = "Data Structure" // sets subject
                classPlace = "IT-601" // sets place
                professorName = "Won Kim" // sets professor
                day = 0 // MON
                startTime = Time(10, 0) // sets the beginning of class time (hour,minute)
                endTime = Time(13, 30) // sets the end of class time (hour,minute)
            },
            Schedule().apply {
                classTitle = "Data Structure" // sets subject
                classPlace = "IT-601" // sets place
                professorName = "Won Kim" // sets professor
                day = 3 // MON
                startTime = Time(12, 0) // sets the beginning of class time (hour,minute)
                endTime = Time(14, 30) // sets the end of class time (hour,minute)
            }
        )
        timetable.add(schedules)
    }
}