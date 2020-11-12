package kr.koohyongmo.untacthelper.common.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.home.ui.fragment.HomeFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        // 제목 항상 보이도록
        bottom_navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        // 기본적으로 첫화면은 Home 화면
        bottom_navigation.selectedItemId = R.id.nav_home
        setCurrentFragmentTab(HomeFragment())

        // 탭 눌릴때 마다 해당 fragment로 화면 설정
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> setCurrentFragmentTab(HomeFragment())
                R.id.nav_lecture -> setCurrentFragmentTab(Fragment())
                else -> throw Exception("Not defined id")
            }
            true
        }
    }

    private fun setCurrentFragmentTab(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_fragment_container, fragment)
            .commitAllowingStateLoss()
    }
}