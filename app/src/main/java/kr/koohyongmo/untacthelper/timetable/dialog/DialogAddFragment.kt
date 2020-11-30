package kr.koohyongmo.untacthelper.timetable.dialog

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_dialogadd.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.ui.activity.MainActivity

/**
 * Created by KimMinJeong on 11/19/20
 */
class DialogAddFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_dialogadd, container, false)
        return view.rootView
    }

    //다이얼로그가 생성될 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // timepicker
        layout_starttime.setOnClickListener {
            val timeListener = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    if(hourOfDay < 10) {
                        tv_hour1.text = "0$hourOfDay"
                    }
                    else {
                        tv_hour1.text = "${hourOfDay}"
                    }
                    if(minute < 10) {
                        tv_minute1.text = "0${minute}"
                    }
                    else {
                        tv_minute1.text = "${minute}"
                    }
                }
            }
            val builder = TimePickerDialog(context, timeListener, Integer.parseInt(tv_hour1.text.toString()),
                                            Integer.parseInt(tv_minute1.text.toString()), true)
            builder.show()
        }

        layout_endtime.setOnClickListener {
            val timeListener = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    if(hourOfDay < 10) {
                        tv_hour2.text = "0${hourOfDay}"
                    }
                    else {
                        tv_hour2.text = "$hourOfDay"
                    }
                    if(minute < 10) {
                        tv_minute2.text = "0${minute}"
                    }
                    else {
                        tv_minute2.text = "${minute}"
                    }
                }
            }
            val builder = TimePickerDialog(context, timeListener, Integer.parseInt(tv_hour2.text.toString()),
                                            Integer.parseInt(tv_minute2.text.toString()), true)
            builder.show()
        }

        // 입력 버튼
        btn_submit.setOnClickListener {
            // 알맞지 않은 요일 체크
            if(sp_day.selectedItemPosition == 0) {
                Toast.makeText(context, "요일을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                //강의 등록
                (activity as MainActivity).timetableFragment.addLecture(
                    et_title.text.toString(),
                    et_place.text.toString(),
                    et_professor.text.toString(),
                    sp_day.selectedItemPosition - 1,
                    Integer.parseInt(tv_hour1.text.toString()),
                    Integer.parseInt(tv_minute1.text.toString()),
                    Integer.parseInt(tv_hour2.text.toString()),
                    Integer.parseInt(tv_minute2.text.toString())
                )
                dismiss()
            }
        }
    }

    class DialogAddFragmentBuilder {
        private val dialog = DialogAddFragment()

        fun create(): DialogAddFragment {
            return dialog
        }
    }
}