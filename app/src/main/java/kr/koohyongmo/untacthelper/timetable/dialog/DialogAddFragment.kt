package kr.koohyongmo.untacthelper.timetable.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_dialogadd.*
import kr.koohyongmo.untacthelper.R


class DialogAddFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_dialogadd, container, false)
        val submitBtn = btn_submit
        return view.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    class DialogAddFragmentBuilder {
        private val dialog = DialogAddFragment()

        fun create(): DialogAddFragment {
            return dialog
        }

    }

}