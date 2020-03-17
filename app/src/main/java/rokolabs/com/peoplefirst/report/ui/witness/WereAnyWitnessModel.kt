package rokolabs.com.peoplefirst.report.ui.witness

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

class WereAnyWitnessModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: EditReportActivity
    var mDisposable = CompositeDisposable()

    var nextClick: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()
    var yesClick: Subject<View> = PublishSubject.create()
    var noClick: Subject<View> = PublishSubject.create()

    init {
        activity = context as EditReportActivity
    }

    fun initDisposable() {
        mDisposable.addAll(
            nextClick.subscribe {
                next()
            },
            prevClick.subscribe {
                previous()
            },
            yesClick.subscribe {
                activity.navigateTo(R.id.nav_report_witness_information)
            },
            noClick.subscribe {
                next()
            },
            activity.onBackPressedObject.subscribe {
                previous()
            }

        )
    }

    fun next() {
        var mode = activity.mode.get()
        if (mode == EditReportActivity.MODE_CREATE_NEW) {
            activity.navigateTo(R.id.nav_report_place)
        } else if (mode == EditReportActivity.MODE_VERIFY_AGGRESSOR) {
            activity.navigateTo(R.id.nav_report_confirm)
        } else if (mode == EditReportActivity.MODE_VERIFY_VICTIM) {
            activity.navigateTo(R.id.nav_report_how_resolved)
        } else if (mode == EditReportActivity.MODE_VERIFY_WITNESS) {
            activity.navigateTo(R.id.nav_report_summary)
        }
    }

    fun previous() {
        var mode = activity.mode.get()
        if (mode == EditReportActivity.MODE_VERIFY_AGGRESSOR) {
            activity.navigateTo(R.id.nav_report_what_happened)
        } else {
            activity.navigateTo(R.id.nav_report_who_agressor_was)
        }
    }

    fun dispose() {
        mDisposable.clear()
    }
}