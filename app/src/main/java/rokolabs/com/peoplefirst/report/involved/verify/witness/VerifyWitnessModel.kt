package rokolabs.com.peoplefirst.report.involved.verify.witness

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
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import javax.inject.Inject

class VerifyWitnessModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity = context as VerifyWitnessActivity
    private var mDisposable = CompositeDisposable()

    var continueSubject: Subject<View> = PublishSubject.create()
    var rejectSubject: Subject<View> = PublishSubject.create()

    var whereWhenField: ObservableField<String> = ObservableField()

    var whereChecked: ObservableField<Int> = ObservableField()
    var witnessChecked: ObservableField<Int> = ObservableField()
    var harassmentChecked: ObservableField<Int> = ObservableField()

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
                    if (mRepository.named == HarassmentRepository.WITNESS) {
                        // TODO: POST create report
                        // TODO: refactor to null safe this
                        val witnessReport = Report()
                        witnessReport.parent_id = mRepository.currentReport.value!!.id
                        witnessReport.status = "created"
                        witnessReport.location_confirmation = true
                        witnessReport.document_confirmation = true
                        witnessReport.harassment_confirmation = true
                        witnessReport.victim = mRepository.currentReport.value!!.victim
                        witnessReport.datetime = mRepository.currentReport.value!!.datetime
                        witnessReport.location_city = mRepository.currentReport.value!!.location_city
                        witnessReport.location_details = mRepository.currentReport.value!!.location_details
                        mRepository.addReport(witnessReport)

                        EditReportActivity.showVerifyWitness(activity)
                        activity.finish()
                    }
                }
            },
            rejectSubject.subscribe {
                rejectOnNo()
            },
            whereChecked.addOnPropertyChanged {
                manageButtons()
            },
            witnessChecked.addOnPropertyChanged {
                manageButtons()
            },
            harassmentChecked.addOnPropertyChanged {
                manageButtons()
            },
            mRepository.currentReport.subscribe {report ->
                whereWhenField.set(
                    "Were you at " + report.location_city + " " + report.location_details + " on " + Utils.newDateFormat(
                        report.datetime
                    ) + " at or around " + Utils.newTimeFormat(report.datetime) + "?"
                )
            }
        )
    }

    fun manageButtons() {
        val second = whereChecked.get() == R.id.whereWhenYes
                && witnessChecked.get() == R.id.interactYes
                && harassmentChecked.get() == R.id.harrassementYes
        val first = whereChecked.get() == R.id.whereWhenNo
                && witnessChecked.get() == R.id.interactNo
                && harassmentChecked.get() == R.id.harrassementNo
        rejectButtonColor.set(first)
        continueButtonColor.set(second)
    }

    fun gotoHarType() {
//        val intent = Intent(activity, HarassmentTypeActivity::class.java)
        val intent = Intent(activity, EditReportActivity::class.java)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity.finish()
    }


    fun gotoWhatHappened() {
//        val intent = Intent(activity, WhatHappenedActivity::class.java)
        val intent = Intent(activity, EditReportActivity::class.java)
        intent.putExtra("verifyMode", true)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity.finish()
    }

    fun reject() {
        // TODO: Right reject?
        mService.rejectWitnessReport(mRepository.currentReport.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    //TODO: rejected
                    activity.finish()
                },
                {
                    Toast.makeText(
                        context,
                        "Could not reject report",
                        Toast.LENGTH_LONG
                    ).show()
                })
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