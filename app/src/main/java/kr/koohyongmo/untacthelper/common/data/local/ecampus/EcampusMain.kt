package kr.koohyongmo.untacthelper.common.data.local.ecampus

/**
 * Created by KooHyongMo on 2020/11/13
 */

data class EcampusMain(
    val classes: ArrayList<Class> = arrayListOf()
)

data class Class(
    val title: String, // 과목명
    val professor: String, // 교수명
    val link: String,
    var week: ArrayList<Week> = ArrayList()
)
data class Week(
    var lectures: ArrayList<Lecture> = ArrayList()// 주차별 수업들
)
data class Lecture(
    val title: String,
    val type: LectureType,
    val dueStart: String,
    val dueEnd: String,
    val link: String
)

enum class LectureType {
    TYPE_VIDEO, TYPE_ZOOM, TYPE_ASSIGNMENT, TYPE_FILE
}