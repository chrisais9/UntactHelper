package kr.koohyongmo.untacthelper.timetable.ui.fragment

import android.view.View
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import kotlinx.android.synthetic.main.fragment_timetable.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.data.local.kcard.Class
import kr.koohyongmo.untacthelper.common.data.local.kcard.KcardClass
import kr.koohyongmo.untacthelper.common.data.local.kcard.Period
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import kr.koohyongmo.untacthelper.timetable.dialog.DialogAddFragment
import java.util.*
import kotlin.collections.ArrayList

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

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = calendar.get(Calendar.MONTH).toInt()
        if(month > 7) {
            tv_header.text = year + "년 2학기"
        } else {
            tv_header.text = year + "년 1학기"
        }

        //dummy data
        // 강의 1
        val period1 = listOf(
            Period(0, Time(9, 0), Time(10, 15)),
            Period(2, Time(9, 0), Time(10, 15))
        )
        val class1 = Class("응용통계학", "윤상민", "미래관2층32호실", period1)

        // 강의2
        val period2 = listOf(
            Period(0, Time(10, 30), Time(11, 45)),
            Period(2, Time(10, 30), Time(11, 45))
        )
        val class2 = Class("이산수학", "김형균", "미래관4층45호실", period2)

        // 강의3
        val period3 = listOf(
            Period(1, Time(9, 0), Time(10, 15)),
            Period(3, Time(9, 0), Time(10, 15))
        )
        val class3 = Class("컴퓨터구조", "임은진", "미래관6층11호", period3)

        // 강의4
        val period4 = listOf(
            Period(1, Time(10, 30), Time(11, 45)),
            Period(3, Time(10, 30), Time(11, 45))
        )
        val class4 = Class("화일처리", "김혁만", "미래관2층31호실", period4)

        // 강의5
        val period5 = listOf(
            Period(1, Time(16, 30), Time(17, 45)),
            Period(3, Time(16,30), Time(17, 45))
        )
        val class5 = Class("모바일프로그래밍", "이창우", "미래관4층45호실", period5)

        // 강의6
        val period6 = listOf(
            Period(4, Time(15, 0), Time(17, 45))
        )
        val class6 = Class("미술의이해", "허윤정", "예술관지하1층10호실", period6)

        // kcard class data
        val kcardClass = KcardClass(listOf(
            class1, class2,  class3, class4, class5, class6
        ))

        //kcard에서 받아온 시간표를 timetable에 추가
        for(c in kcardClass.classes) {
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

        val addLectureBtn = btn_addLecture
        addLectureBtn.setOnClickListener(View.OnClickListener {
            val dialog = DialogAddFragment.DialogAddFragmentBuilder()
                .create()
            dialog.show(activity!!.supportFragmentManager, dialog.tag)
            addLecture()
        })
    }

    //강의 추가
    private fun addLecture() {
        val title = "알바" // 강의명
        val place = "맥도날드" // 강의실
        val professor = "" // 교수명
        // 강의 시간정보
        val period = listOf(
            Period(3, Time(13, 0), Time(14,0))
        )

        val schedules = ArrayList<Schedule>()
        for(p in period) {
            schedules.add(
                Schedule().apply {
                    classTitle = title
                    classPlace = place
                    professorName = professor
                    day = p.day
                    startTime = p.startTime
                    endTime = p.endTime
                }
            )
        }
        timetable.add(schedules)
    }
}