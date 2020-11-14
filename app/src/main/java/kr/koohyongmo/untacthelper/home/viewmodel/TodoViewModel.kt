package kr.koohyongmo.untacthelper.home.viewmodel

import kr.koohyongmo.untacthelper.common.data.local.ecampus.LectureType

/**
 * Created by KooHyongMo on 11/14/20
 */
data class TodoViewModel(
    val time: String,
    val lectureTitle: String,
    val lectureType: LectureType,
    val contentTitle: String
)