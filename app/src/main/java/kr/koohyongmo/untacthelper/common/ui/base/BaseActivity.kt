package kr.koohyongmo.untacthelper.common.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by KooHyongMo on 2020/08/05
 */
abstract class BaseActivity
    : AppCompatActivity() {

    /**
     * setContentView로 호출할 Layout의 리소스 ID
     * ex) R.layout.activity_main
     */
    protected abstract val layoutResourceID: Int

    /**
     * setSupportActionBar 로 설정할 Toolbar의 ID
     * ex)
     */
    protected abstract val layoutToolbarID: Int

    lateinit var toolbar: Toolbar

    private val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceID)
        if (layoutToolbarID != 0) {
            toolbar = findViewById(layoutToolbarID)
            setSupportActionBar(toolbar)

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = ""
        }

        initLayoutAttributes()
    }

    /**
     * 레이아웃을 띄운 직후 호출.
     * 뷰나 액티비티의 속성 등을 초기화.
     * ex) 리사이클러뷰, 툴바, 드로어뷰..
     */
    abstract fun initLayoutAttributes()

    /**
     * 툴바 백버튼의 기본 액션을 finish()로 정의합니다.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun addToDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }


}