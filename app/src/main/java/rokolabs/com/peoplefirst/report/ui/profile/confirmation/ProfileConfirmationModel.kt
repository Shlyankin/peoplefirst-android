package rokolabs.com.peoplefirst.report.ui.profile.confirmation

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class ProfileConfirmationModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity: EditReportActivity = context as EditReportActivity
    private var mDisposable = CompositeDisposable()

    var submitClick: Subject<View> = PublishSubject.create()
    fun initDisposable() {
        mDisposable.addAll(
            activity.onBackPressedObject.subscribe {
                activity.navigateTo(R.id.nav_report_summary)
            }
        )
    }

    fun dispose() {
        mDisposable.clear()
    }
}