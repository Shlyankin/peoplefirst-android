package rokolabs.com.peoplefirst.report.involved.verify.victim

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.involved.verify.witness.VerifyActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import javax.inject.Inject

class VerifyVictimModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity = context as VerifyVictimActivity
    private var mDisposable = CompositeDisposable()

    var continueSubject: Subject<View> = PublishSubject.create()
    var rejectSubject: Subject<View> = PublishSubject.create()

    var whereChecked: ObservableField<Int> = ObservableField()
    var harassmentChecked: ObservableField<Int> = ObservableField()
    var documentChecked: ObservableField<Int> = ObservableField()
    var whereWhen: ObservableField<String> = ObservableField()

    var rejectButtonColor: ObservableField<Boolean> = ObservableField()
    var continueButtonColor: ObservableField<Boolean> = ObservableField()

    init {
        rejectButtonColor.set(false)
        continueButtonColor.set(false)
    }

    fun initDisposable() {
        mDisposable.addAll(
            continueSubject.subscribe {
                if (continueButtonColor.get()!!) {
//                    val intent = Intent(this, HarassmentTypeActivity::class.java)
                    val intent = Intent(context, EditReportActivity::class.java)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(R.anim.enter, R.anim.exit)
                    activity.finish()
                }
            },
            rejectSubject.subscribe {
                rejectOnNo()
            },
            whereChecked.addOnPropertyChanged {
                manageButtons()
            },
            harassmentChecked.addOnPropertyChanged {
                manageButtons()
            },
            documentChecked.addOnPropertyChanged {
                manageButtons()
            },
            mRepository.currentReport.subscribe { report ->
                if (report !== Report.EMPTY) {
                    whereWhen.set(
                        "Were you at " + report.location_city + " " + report.location_details + " on " + Utils.newDateFormat(
                            report.datetime
                        ) + " at or around " + Utils.newTimeFormat(report.datetime) + "?"
                    )
                }
            }
        )
    }

    fun reject() {
        mService.rejectTestimony(mRepository.currentReport.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { reportResponse ->
                    if (reportResponse.success) {
                        mRepository.getMyReports()
//                                val intent = Intent(context, RejectedReportActivity::class.java)
//                                activity.startActivity(intent)
//                                activity.finish()
                    }
                },
                { throwable ->
                    Toast.makeText(
                        context,
                        "Could not reject report",
                        Toast.LENGTH_LONG
                    ).show()
                })
    }


    fun manageButtons() {
        var second = whereChecked.get() == R.id.whereWhenYes
                && harassmentChecked.get() == R.id.harrassementYes
                && documentChecked.get() == R.id.documentYes
        var first = whereChecked.get() == R.id.whereWhenNo
                && harassmentChecked.get() == R.id.harrassementNo
                && documentChecked.get() == R.id.documentNo
        rejectButtonColor.set(first)
        continueButtonColor.set(second)
    }

    fun rejectOnNo(): Boolean {
        if (rejectButtonColor.get()!!) {
            //на все ответил отрицательно значит отказался
            reject()
            return true
        }
        return false
    }

    fun dispose() {
        mDisposable.clear()
    }
}