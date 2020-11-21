package kr.koohyongmo.untacthelper.lecture.fragment

import android.content.Intent
import android.net.Uri
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
import kotlin.random.Random


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

    // 강의 목록 리스트 초기화
    override fun initLayoutAttributes() {
        rv_lecture.layoutManager = LinearLayoutManager(requireContext())
        lectureAdapter
            .map<LectureViewModel, ItemLectureBinding>(R.layout.item_lecture) {
                onBind {
                    it.binding.progress.progress = it.binding.listContent!!.percent
                }
                onClick {
                    val redirectLink = it.binding.listContent!!.link
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectLink)))
                }
            }
            .into(rv_lecture)
        addToDisposable(
            RxBus.listen(RxEvents.OnEcampusClassReady::class.java).subscribe {
                initLectureItem()
            }
        )
    }

    // 강의 목록 아이템 추가
    fun initLectureItem() {
        lectureList.clear()
        EcampusCacheUtil.mEcampusMain.classes.forEachIndexed { index, it ->
            val percent = if (index <= 1) 0 else 85 + Random(it.hashCode()).nextInt(0,15)
            lectureList.add(
                LectureViewModel(
                    it.title,
                    if (it.professor.isNotBlank()) "${it.professor} 교수님" else "", // 교수명이 존재 하면 이름 끝에 교수님 추가
                    it.link,
                    percent
                )
            )
        }
        lectureAdapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "LectureFragment"
    }
}