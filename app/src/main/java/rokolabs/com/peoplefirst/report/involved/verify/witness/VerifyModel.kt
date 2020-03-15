package rokolabs.com.peoplefirst.report.involved.verify.witness

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
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import javax.inject.Inject

class VerifyModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity = context as VerifyActivity
    private var mDisposable = CompositeDisposable()

    var continueSubject: Subject<View> = PublishSubject.create()
    var whereChecked: ObservableField<Int> = ObservableField()
    var witnessChecked: ObservableField<Int> = ObservableField()

    fun initDisposable() {
        mDisposable.addAll(
            continueSubject.subscribe {
                if (mRepository.named == HarassmentRepository.WITNESS) {
                    gotoHarType()
                } else if (mRepository.named == HarassmentRepository.AGGRESSOR) {
                    gotoWhatHappened()
                }
            },
            whereChecked.addOnPropertyChanged {
                if (it.get() == R.id.whereWhenYes)
                    if (mRepository.named == HarassmentRepository.WITNESS)
                        mRepository.currentWitnessTestimony.value!!.location_confirmation = true
                    else if (mRepository.named == HarassmentRepository.AGGRESSOR)
                        mRepository.currentTransgressorReport.value!!.location_confirmation = true

                if (it.get() == R.id.whereWhenNo)
                    if (mRepository.named == HarassmentRepository.WITNESS)
                        mRepository.currentWitnessTestimony.value!!.location_confirmation = false
                    else if (mRepository.named == HarassmentRepository.AGGRESSOR)
                        mRepository.currentTransgressorReport.value!!.location_confirmation = false
                if (it.get() == R.id.whereWhenSkip)
                    if (mRepository.named == HarassmentRepository.WITNESS)
                        mRepository.currentWitnessTestimony.value!!.location_confirmation = null
                    else if (mRepository.named == HarassmentRepository.AGGRESSOR)
                        mRepository.currentTransgressorReport.value!!.location_confirmation = null
            },
            witnessChecked.addOnPropertyChanged {
                if (it.get() == R.id.interactYes)
                    if (mRepository.named == HarassmentRepository.WITNESS)
                        mRepository.currentWitnessTestimony.value!!.documentation_experienced = true
                    else if (mRepository.named == HarassmentRepository.AGGRESSOR)
                        mRepository.currentTransgressorReport.value!!.interact_confirmation = true
                if (it.get() == R.id.interactNo)
                    if (mRepository.named == HarassmentRepository.WITNESS)
                        mRepository.currentWitnessTestimony.value!!.documentation_experienced =
                            false
                    else if (mRepository.named == HarassmentRepository.AGGRESSOR)
                        mRepository.currentTransgressorReport.value!!.interact_confirmation = false

                if (it.get() == R.id.interactSkip)
                    if (mRepository.named == HarassmentRepository.WITNESS)
                        mRepository.currentWitnessTestimony.value!!.documentation_experienced = null
                    else if (mRepository.named == HarassmentRepository.AGGRESSOR)
                        mRepository.currentTransgressorReport.value!!.interact_confirmation = null
            }
        )
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

    fun dispose() {
        mDisposable.clear()
    }
}