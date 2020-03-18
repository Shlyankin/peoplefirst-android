package rokolabs.com.peoplefirst.report.ui.resolution.how

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class HowResolvedModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity: EditReportActivity = context as EditReportActivity
    private var mDisposable = CompositeDisposable()

    var nextClick: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()

    var details: ObservableField<String> = ObservableField()

    fun initDisposable() {
        mDisposable.addAll(
            activity.onBackPressedObject.subscribe {
                previous()
            },
            prevClick.subscribe {
                previous()
            },
            nextClick.subscribe {
                if (save()) {
                    // activity.navigateTo(R.id.nav_report_summary)
                    activity.navigateNext()
                }
            },
            mRepository.currentReport.subscribe {
                details.set(it.victim_proposed_solution)
            }
        )
    }

    fun save(): Boolean {
       if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null) {
            mRepository.currentReport.value!!.victim_proposed_solution =
                details.get().toString()
            mRepository.currentReport.onNext(mRepository.currentReport.value!!)
        }
        return true
    }

    fun previous() {
        // activity.navigateTo(R.id.nav_report_date_time)
        activity.navigatePrev()
    }

    fun dispose() {
        mDisposable.clear()
    }
}