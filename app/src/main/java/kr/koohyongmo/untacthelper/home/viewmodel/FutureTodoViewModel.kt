package kr.koohyongmo.untacthelper.home.viewmodel

import kr.koohyongmo.untacthelper.common.data.local.ecampus.LectureType

/**
 * Created by KooHyongMo on 11/17/20
 */
data class FutureTodoViewModel(
    val dueDate: String,
    val dueTime: String,
    val title: String,
    val contentType: LectureType,
    val contentTitle: String,
    val contentURL: String
)