package kr.koohyongmo.untacthelper.lecture.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.fragment_lecture.*
import kr.koohyongmo.untacthelper.BR
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.data.local.ecampus.EcampusCacheUtil
import kr.koohyongmo.untacthelper.common.data.local.sharedpreference.LoginPreference
import kr.koohyongmo.untacthelper.common.rx.RxBus
import kr.koohyongmo.untacthelper.common.rx.RxEvents
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import kr.koohyongmo.untacthelper.databinding.ItemLectureBinding
import kr.koohyongmo.untacthelper.lecture.viewmodel.LectureViewModel


/**
 * Created by KooHyongMo on 11/19/20
 */
class LectureFragment : BaseFragment() {
    override val layoutResourceID: Int
        get() = R.layout.fragment_lecture
    override val layoutToolbarID: Int
        get() = R.id.toolbar_lecture

    private val lectureList = arrayListOf<Any>()
    private val lectureAdapter by lazy { LastAdapter(lectureList, BR.listContent) }

    override fun initLayoutAttributes() {
        rv_lecture.layoutManager = LinearLayoutManager(requireContext())
        lectureAdapter
            .map<LectureViewModel, ItemLectureBinding>(R.layout.item_lecture) {
                onBind {
                    it.binding.progress.progress = it.binding.listContent!!.percent
                }
            }
            .into(rv_lecture)
        addToDisposable(
            RxBus.listen(RxEvents.OnEcampusClassReady::class.java).subscribe {
                initLectureItem()
            }
        )
    }

    fun initLectureItem() {
        lectureList.clear()
        EcampusCacheUtil.mEcampusMain.classes.forEach {
            lectureList.add(
                LectureViewModel(
                    it.title,
                    it.professor,
                    50
                )
            )
        }
        lectureAdapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "LectureFragment"
    }
}