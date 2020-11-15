package kr.koohyongmo.untacthelper.timetable.ui.fragment

import android.widget.Toast
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_timetable.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.data.local.kcard.Class
import kr.koohyongmo.untacthelper.common.data.local.kcard.KcardClass
import kr.koohyongmo.untacthelper.common.data.local.kcard.Period
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
        val kCardClass = KcardClass(emptyList())

        //kcard에서 받아온 시간표를 timetable에 추가
        for(c in kCardClass.classes) {
            val schedules = ArrayList<Schedule>()
            for(p in c.time) {
                schedules.add(
                    Schedule().apply {
                        classTitle = c.title
                        classPlace = "${c.professor} (${c.room})"
                        day = p.day
                        startTime = p.startTime
                        endTime = p.endTime
                    }
                )
            }
            timetable.add(schedules)
        }
    }

    /**
     * 미구현기능
     */
//    //강의 추가
//    private fun addLecture() {
//        val title = "강의" // 강의명
//        val place = "강의실" // 강의실
//        val professor = "교수명" // 교수명
//        // 강의 시간정보
//        val period = listOf(
//            Period(0, Time(13, 0), Time(17,0)),
//            Period(1, Time(13, 0), Time(16, 0))
//        )
//
//        val schedules = ArrayList<Schedule>()
//        for(p in period) {
//            schedules.add(
//                Schedule().apply {
//                    classTitle = title
//                    classPlace = place
//                    professorName = professor
//                    day = p.day
//                    startTime = p.startTime
//                    endTime = p.endTime
//                }
//            )
//        }
//        timetable.add(schedules)
//    }
}