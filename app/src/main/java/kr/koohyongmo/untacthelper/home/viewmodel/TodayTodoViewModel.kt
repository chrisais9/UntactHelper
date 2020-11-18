package kr.koohyongmo.untacthelper.home.viewmodel

import kr.koohyongmo.untacthelper.common.data.local.ecampus.LectureType

/**
 * Created by KooHyongMo on 11/14/20
 */
data class TodayTodoViewModel(
    val time: String,
    val classTitle: String,
    val contentType: LectureType,
    val contentTitle: String,
    val contentURL: String
)