package rokolabs.com.peoplefirst.report.ui.witness.information

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
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

class WitnessInformationModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: EditReportActivity
    var mDisposable = CompositeDisposable()
    lateinit var mSelectedUsers: SelectedUsersFragment

    var nextClick: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()

    init {
        activity = context as EditReportActivity
    }

    fun initDisposable() {
        mSelectedUsers.viewModel.mRetailMode = mRepository.me.getValue()?.retail == 1
        mSelectedUsers.viewModel.mRetailMeEmail = mRepository.me.getValue()?.email!!

        mDisposable.addAll(
            prevClick.subscribe {
                previous()
            },
            nextClick.subscribe {
                if (save()) {
                    var t = 0
                }
            },
            activity.onBackPressedObject.subscribe {
                previous()
            },
            mRepository.currentReport.subscribe { report ->
                if (report !== Report.EMPTY) {
                    if (report.witnesses != null) {
                        mSelectedUsers.setUsers(report.witnesses)
                    }
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
            if (mRepository.named == HarassmentRepository.EMPTY) {
                if (mRepository.me.value?.retail == 1 && !mSelectedUsers.viewModel.conformsToDomain()) {
                    Toast.makeText(
                        context,
                        "People included in your report must have the same email domain as yours",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                mRepository.currentReport.value?.witnesses = mSelectedUsers.getUsers()
                mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
            } else {
                mRepository.mSelectedUsersForWitnessOrAggressorReport = mSelectedUsers.getUsers()
            }
        }
        return true
    }
}