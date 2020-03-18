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
                // отдельное отведение с witness через navigateTo
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
        // activity.navigateTo(R.id.nav_report_place)
        activity.navigateNext()
    }

    fun previous() {
        // activity.navigateTo(R.id.nav_report_who_agressor_was)
        activity.navigatePrev()
    }

    fun dispose() {
        mDisposable.clear()
    }
}