package kr.koohyongmo.untacthelper.common.data.local.ecampus

/**
 * Created by KooHyongMo on 2020/11/13
 */
data class EcampusMain(
    val lectures: List<Class>
)

data class Class(
    val title: String, // 과목명
    val professor: String? = null, // 교수명
    val classDivision: String? = null, // 분반 번호
    val week: List<Week>? = null
)
data class Week(
    val lectures: List<Lecture>? = null // 주차별 수업들
)
data class Lecture(
    val title: String,
    val type: LectureType,
    val dueStart: String,
    val dueEnd: String
)

enum class LectureType {
    TYPE_VIDEO, TYPE_ZOOM, TYPE_ASSIGNMENT, TYPE_FILE
}