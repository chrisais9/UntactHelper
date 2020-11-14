package kr.koohyongmo.untacthelper.common.data.local.kcard

import com.github.tlaabs.timetableview.Time

/**
 * Created by KimMinJeong on 2020/11/14
 */

// Kcard - 나의 수업 목록에서 조회되는 내용
data class KcardClass(
    val Classes: List<Class>
)

data class Class(
    val title: String, // 과목명
    val professor: String, // 교수명
    val room: String? = null, // 강의실
    val time: List<Period> // 강의시간
)

data class Period(
    val day: Int, // 강의요일 (월=0, 일=6)
    val startTime: Time, //강의시작시간
    val endTime: Time //강의 종료시간
)