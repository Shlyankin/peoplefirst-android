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
    private var activity = context as VerifyActivity
    private var mDisposable = CompositeDisposable()

    var continueSubject: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()
    var whereChecked: ObservableField<Int> = ObservableField()
    var harassmentChecked: ObservableField<Int> = ObservableField()
    var documentChecked: ObservableField<Int> = ObservableField()
    var whereWhen: ObservableField<String> = ObservableField()

    fun initDisposable() {
        mDisposable.addAll(
            continueSubject.subscribe {
                if (!rejectOnNo()) {
//                    val intent = Intent(this, HarassmentTypeActivity::class.java)
                    val intent = Intent(context, EditReportActivity::class.java)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(R.anim.enter, R.anim.exit)
                    activity.finish()
                }
            },
            prevClick.subscribe {
                reject()
            },
            whereChecked.addOnPropertyChanged {
                if (it.get() == R.id.whereWhenYes)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.location_confirmation = true

                if (it.get() == R.id.whereWhenNo)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.location_confirmation = false

                if (it.get() == R.id.whereWhenSkip)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.location_confirmation = null
            },
            harassmentChecked.addOnPropertyChanged {
                if (it.get() == R.id.harrassementYes)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.harassment_confirmation = true
                if (it.get() == R.id.harrassementNo)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.harassment_confirmation = false

                if (it.get() == R.id.harrassementSkip)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.harassment_confirmation = null
            },
            documentChecked.addOnPropertyChanged {
                if (it.get() == R.id.documentYes)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.documentation_experienced = true
                if (it.get() == R.id.documentNo)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.documentation_experienced = false

                if (it.get() == R.id.documentSkip)
                    if (mRepository.named == HarassmentRepository.VICTIM)
                        mRepository.currentVictimTestimony.value!!.documentation_experienced = null
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

    fun rejectOnNo(): Boolean {
        if (mRepository.currentVictimTestimony.value!!.location_confirmation != null
            && mRepository.currentVictimTestimony.value!!.harassment_confirmation != null
            && mRepository.currentVictimTestimony.value!!.documentation_experienced != null
        ) {
            if (!mRepository.currentVictimTestimony.value!!.location_confirmation
                && !mRepository.currentVictimTestimony.value!!.harassment_confirmation
                && !mRepository.currentVictimTestimony.value!!.documentation_experienced
            ) {
                //на все ответил отрицательно значит отказался
                reject()
                return true
            }
        }
        return false
    }

    fun dispose() {
        mDisposable.clear()
    }
}