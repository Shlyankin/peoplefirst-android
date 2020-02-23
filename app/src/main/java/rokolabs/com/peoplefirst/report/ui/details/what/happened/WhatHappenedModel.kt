package rokolabs.com.peoplefirst.report.ui.details.what.happened

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
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

class WhatHappenedModel @Inject
constructor(
    private val context: Context,
    private val selectedUsersFragment: Fragment,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: EditReportActivity
    var mDisposable = CompositeDisposable()

    var nextClick: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()
    var attachClick: Subject<View> = PublishSubject.create()
    var details: ObservableField<String> = ObservableField()

    init {
        activity = context as EditReportActivity
    }

    fun initDisposable() {
        mDisposable=CompositeDisposable()
        mDisposable.addAll(
            nextClick.subscribe {
                if (mRepository.named != HarassmentRepository.EMPTY) {
                    var intent: Intent? = null
                    if (mRepository.named == HarassmentRepository.WITNESS) {
                        mRepository.currentWitnessTestimony.value?.details =
                            details.get().toString()
                        mRepository.currentWitnessTestimony.onNext(mRepository.currentWitnessTestimony.getValue()!!)
                        activity.navigateTo(R.id.menuItem5)
                    } else if (mRepository.named == HarassmentRepository.TRANSGRESSOR) {
                        mRepository.currentTransgressorReport.value?.details =
                            details.get().toString()
                        mRepository.currentTransgressorReport.onNext(mRepository.currentTransgressorReport.getValue()!!)
//                        intent = Intent(this, WitnessYesNoActivity::class.java)
                    } else if (mRepository.named == HarassmentRepository.VICTIM) {
                        mRepository.currentVictimTestimony.value?.details =
                            details.get().toString()
                        mRepository.currentVictimTestimony.onNext(mRepository.currentVictimTestimony.getValue()!!)
//                        intent = Intent(this, WhoAgressorActivity::class.java)
                    }
                } else {
                    activity.navigateTo(R.id.menuItem5)
                    save()
                }
            },
            prevClick.subscribe {
                if (mRepository.named != HarassmentRepository.EMPTY) {
                    var intent: Intent? = null
                    if (mRepository.named == HarassmentRepository.WITNESS) {
                        mRepository.currentWitnessTestimony.value?.details =
                            details.get().toString()
                        mRepository.currentWitnessTestimony.onNext(mRepository.currentWitnessTestimony.getValue()!!)
//                        intent = Intent(this, VerifyActivity::class.java)
                    } else if (mRepository.named == HarassmentRepository.TRANSGRESSOR) {
                        mRepository.currentTransgressorReport.value?.details =
                            details.get().toString()
                        mRepository.currentTransgressorReport.onNext(mRepository.currentTransgressorReport.getValue()!!)
//                        intent = Intent(this, VerifyActivity::class.java)
                    } else if (mRepository.named == HarassmentRepository.VICTIM) {
                        mRepository.currentVictimTestimony.value?.details =
                            details.get().toString()
                        mRepository.currentVictimTestimony.onNext(mRepository.currentVictimTestimony.getValue()!!)
                        activity.navigateTo(R.id.menuItem3)
                    }
                } else {
//                    val intent = Intent(this, HarassmentReasonActivity::class.java)
                    save()
                    activity.navigateTo(R.id.menuItem3)
                }
            },
            attachClick.subscribe {
                activity.chooseFile()
            }
        )
        if (mRepository.named == HarassmentRepository.EMPTY) {
            mDisposable.add(mRepository.currentReport.subscribe { report ->
                if (report !== Report.EMPTY) {
                    if (details.get().toString().isEmpty())
                        details.set(report.details)
                }
            })
        } else {
            if (mRepository.named == HarassmentRepository.WITNESS) {
                mDisposable.add(mRepository.currentWitnessTestimony.subscribe { witnessReport ->
                    if (details.get().toString().isEmpty())
                        details.set(witnessReport.details)
                })
            } else if (mRepository.named == HarassmentRepository.TRANSGRESSOR) {
                mDisposable.add(mRepository.currentTransgressorReport.subscribe { witnessReport ->
                    if (details.get().toString().isEmpty())
                        details.set(witnessReport.details)
                })
            } else if (mRepository.named == HarassmentRepository.VICTIM) {
                mDisposable.add(mRepository.currentVictimTestimony.subscribe { victimTestimony ->
                    if (details.get().toString().isEmpty())
                        details.set(victimTestimony.details)
                })
            }

        }
    }

    fun dispose() {
        mDisposable.dispose()
        mDisposable.clear()
    }

    fun save() {
        val intent = Intent()
        intent.putExtra("whatHappened", details.get().toString())
        if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null) {
            mRepository.currentReport.value?.details = details.get().toString()
            mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
        }
    }
}