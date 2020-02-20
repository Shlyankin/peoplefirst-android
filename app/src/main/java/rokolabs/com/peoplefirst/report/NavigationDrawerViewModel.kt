package rokolabs.com.peoplefirst.report

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class NavigationDrawerViewModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mOrderRepository: HarassmentRepository
) : ViewModel() {
    private var mActivity:EditReportActivity = context as EditReportActivity
    private var mDisposable = CompositeDisposable()
    var menuItemClick: Subject<View> = PublishSubject.create()

    init {
        initDisposable()
    }

    fun initDisposable() {
        mDisposable.addAll(
            menuItemClick.subscribe {
                mActivity.navigateTo(it)
            }
        )
    }

    fun dispose() {
        mDisposable.dispose()
    }
}