package kr.koohyongmo.untacthelper.lecture.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.fragment_lecture.*
import kr.koohyongmo.untacthelper.BR
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import kr.koohyongmo.untacthelper.databinding.ItemFutureTodoHeaderBinding

/**
 * Created by KooHyongMo on 11/19/20
 */
class LectureFragment: BaseFragment() {
    override val layoutResourceID: Int
        get() = R.layout.fragment_lecture
    override val layoutToolbarID: Int
        get() = R.id.toolbar_lecture

    val lectureList = arrayListOf<Any>()
    val lectureAdapter by lazy { LastAdapter(lectureList, BR.listContent) }

    override fun initLayoutAttributes() {
        rv_lecture.layoutManager = LinearLayoutManager(requireContext())
        lectureAdapter
            .map<String, ItemFutureTodoHeaderBinding>(R.layout.item_lecture_header)
            .into(rv_lecture)

        lectureList.add("[1주차]")
    }
}