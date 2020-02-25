package rokolabs.com.peoplefirst.report.ui.details.what.happened

import android.content.Context
import android.content.Intent
import android.view.View
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
        mDisposable = CompositeDisposable()
        mDisposable.addAll(
            nextClick.subscribe {
                activity.navigateTo(R.id.nav_report_who_being_harassed)
                save()
            },
            prevClick.subscribe {
                activity.navigateTo(R.id.nav_report_happened_before)
            },
            attachClick.subscribe {
                activity.chooseFile()
            }
        )
        mDisposable.add(mRepository.currentReport.subscribe { report ->
            if (report !== Report.EMPTY) {
                if (details.get().toString().isEmpty())
                    details.set(report.details)
            }
        })
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