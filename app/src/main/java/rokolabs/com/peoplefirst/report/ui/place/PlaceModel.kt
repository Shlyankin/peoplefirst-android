package rokolabs.com.peoplefirst.report.ui.place

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersFragment
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class PlaceModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: EditReportActivity
    var mDisposable = CompositeDisposable()

    var nextClick: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()
    var locationClick :Subject<View> = PublishSubject.create()
    var location: ObservableField<String> = ObservableField()
    var details: ObservableField<String> = ObservableField()

    init {
        activity = context as EditReportActivity
        details.set("")
        location.set("")
    }

    fun initDisposable() {
        mDisposable.addAll(
            nextClick.subscribe {
                if (save()) {
                    var t = 0
                }
            },
            prevClick.subscribe {
                previous()
            },
            activity.onBackPressedObject.subscribe {
                previous()
            },
            mRepository.currentReport.subscribe { report ->
                if (report !== Report.EMPTY) {
                    if (location.get().toString().isEmpty())
                        location.set(report.location_city)
                    details.set(report.location_details)
                }
            }
        )
    }

    fun previous() {
        activity.navigateTo(R.id.nav_report_were_any_witnesses)
    }

    fun dispose() {
        mDisposable.clear()
    }

    fun save(): Boolean {
        if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null) {
            mRepository.currentReport.value!!.location_city = location.get().toString()
            mRepository.currentReport.value!!.location_details = details.get().toString()
            if ("" == mRepository.currentReport.value!!.location_city) {
                Toast.makeText(
                    context,
                    "Location should be selected to proceed",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return false
            }
            mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
        }
        return true
    }
}