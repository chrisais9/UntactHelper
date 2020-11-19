package kr.koohyongmo.untacthelper.lecture.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.fragment_lecture.*
import kr.koohyongmo.untacthelper.BR
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import kr.koohyongmo.untacthelper.databinding.ItemLectureBinding
import kr.koohyongmo.untacthelper.lecture.viewmodel.LectureViewModel

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
            .map<LectureViewModel, ItemLectureBinding>(R.layout.item_lecture) {
                onBind {
                    it.binding.progress.progress = it.binding.listContent!!.percent
                }
            }
            .into(rv_lecture)

        lectureList.add(
            LectureViewModel(
                "컴퓨터구조",
                "임은진 교수님",
                50
            )
        )
        lectureList.add(
            LectureViewModel(
                "응용통계학",
                "가나다 교수님",
                32
            )
        )
        lectureList.add(
            LectureViewModel(
                "모바일 프로그래밍",
                "오우야 교수님",
                96
            )
        )
        lectureAdapter.notifyDataSetChanged()
    }
}