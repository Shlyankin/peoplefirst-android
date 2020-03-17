package rokolabs.com.peoplefirst.report.ui.agressor

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

class WhoAgressorWasModel @Inject
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
        mSelectedUsers.viewModel.mRetailMode = mRepository.me.value?.retail == 1
        mSelectedUsers.viewModel.mRetailMeEmail = mRepository.me.value?.email!!
        mDisposable.addAll(
            nextClick.subscribe {
                save()
                activity.navigateTo(R.id.nav_report_were_any_witnesses)
            },
            prevClick.subscribe {
                previos()
            },
            activity.onBackPressedObject.subscribe {
                previos()
            },
            mRepository.currentReport.subscribe {
                if (it !== Report.EMPTY) {
                    if (it.aggressors != null && it.aggressors.size > 0) {
                        mSelectedUsers.setUsers(it.aggressors)
                    }
                }
            }
        )


    }

    fun previos() {
        if (activity.mode.get() == EditReportActivity.MODE_VERIFY_VICTIM) {
            activity.navigateTo(R.id.nav_report_who_being_harassed)
        } else {
            activity.navigateTo(R.id.nav_report_who_being_harassed)
        }
    }

    fun dispose() {
        mDisposable.clear()
    }

    fun save(): Boolean {
        if (mRepository.named == HarassmentRepository.EMPTY) {
            if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null && mSelectedUsers.checkConditions()) {
                if (mSelectedUsers.getUsers().contains(mRepository.me.value)) {
                    Toast.makeText(
                        context,
                        "Current user can not be named and aggressor",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (mSelectedUsers.getUsers().contains(mRepository.currentReport.value?.victim)) {
                    Toast.makeText(
                        context,
                        "One and the same user can not be victim and aggressor simultaneously",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (mRepository.me.value?.retail == 1 && !mSelectedUsers.viewModel.conformsToDomain()) {
                    Toast.makeText(
                        context,
                        "People included in your report must have the same email domain as yours",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                mRepository.currentReport.value?.aggressors = mSelectedUsers.getUsers()
                mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
            }
        }
        return true
    }
}