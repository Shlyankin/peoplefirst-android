package rokolabs.com.peoplefirst.report.ui.victim

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
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersFragment
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import java.util.ArrayList
import javax.inject.Inject

class WhoBeingHarassedModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: EditReportActivity
    lateinit var mSelectedUsers: SelectedUsersFragment
    var mDisposable = CompositeDisposable()

    var selfReporting: ObservableField<Boolean> = ObservableField()
    var nextClick: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()

    init {
        activity = context as EditReportActivity
    }

    fun initDisposable() {
        mDisposable= CompositeDisposable()
        nextClick.subscribe {
            var t=0
        }
        mDisposable.addAll(
            selfReporting.addOnPropertyChanged {
                if (it.get()!!) {
                    if (mRepository.me.value != null) {
                        val me = ArrayList<User>()
                        me.add(mRepository.me.getValue()!!)
                        mSelectedUsers.setUsers(me)
                    }
                    mSelectedUsers.viewModel.hideAdd()
                    mSelectedUsers.viewModel.hideClear()
                } else {
                    mSelectedUsers.clear()
                    mSelectedUsers.viewModel.hideClear()
                    mSelectedUsers.viewModel.showAdd()
                }
            },
            nextClick.subscribe {
                if (save()) {
                    var t = 0
//                    val intent = Intent(this, WhoAgressorActivity::class.java)
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.enter, R.anim.exit)
                }
            },
            prevClick.subscribe {
                activity.navigateTo(R.id.menuItem4)
                save()
            }
        )
        if (mRepository.named == HarassmentRepository.EMPTY) {
            mDisposable.add(mRepository.currentReport.subscribe { report ->
                if (report !== Report.EMPTY) {
                    if (report.victim != null) {
                        val victim = ArrayList<User>()
                        victim.add(report.victim)
                        if (mSelectedUsers.getUsers().size === 0)
                            mSelectedUsers.setUsers(victim)
                        if (mRepository.me.value?.id == report.victim.id) {
                            selfReporting.set(true)
                        }
                    }
                }
            })
        } else {
            if (mRepository.named == HarassmentRepository.WITNESS) {
                mDisposable.add(mRepository.currentWitnessTestimony.subscribe { report ->
                    if (report.victim != null) {
                        val victim = ArrayList<User>()
                        victim.add(report.victim)
                        if (mSelectedUsers.getUsers().size === 0)
                            mSelectedUsers.setUsers(victim)
                        if (mRepository.me.value?.id == report.victim.id) {
                            selfReporting.set(true)
                        }
                    }
                })
            }
        }
    }

    fun save(): Boolean {
        val intent = Intent()
//        intent.putExtra("beingHarassed", mSelectedUsers.getUsers())
//        setResult(RESULT_OK, intent)
        if (mRepository.named == HarassmentRepository.EMPTY) {
            if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null) {
                if (mSelectedUsers.getUsers().size > 0) {
                    if (mRepository.me.value!!.is_retail && !mSelectedUsers.viewModel.conformsToDomain()) {
                        Toast.makeText(
                            context,
                            "People included in your report must have the same email domain as yours",
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                    mRepository.currentReport.value!!.victim = mSelectedUsers.getUsers()[0]
                    mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
                }
            }
        } else {
            if (mRepository.currentWitnessTestimony.value != null) {
                if (mSelectedUsers.getUsers().size > 0) {
                    mRepository.currentWitnessTestimony.value!!.victim =
                        mSelectedUsers.getUsers()[0]
                    mRepository.currentWitnessTestimony.onNext(mRepository.currentWitnessTestimony.getValue()!!)
                }
            }
        }
        if (mRepository.currentReport.value?.victim != null) {//жертва указана
            if (mRepository.currentReport.value?.victim != mRepository.me.value) {//репорт составлен на жертву другого человека
                if (!mRepository.currentReport?.value!!.witnesses.contains(mRepository.me.value)) {
                    mRepository.currentReport?.value!!.witnesses.add(mRepository.me.value)
                }
            }
        }
        return true
    }

    fun dispose() {
        mDisposable.dispose()
        mDisposable.clear()
    }
}