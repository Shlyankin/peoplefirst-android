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
        details.set("")
    }

    fun initDisposable() {
//        mDisposable = CompositeDisposable()
        mDisposable.addAll(
            nextClick.subscribe {
                var mode = activity.mode.get()
                if (mode == EditReportActivity.MODE_VERIFY_AGGRESSOR) {
                    activity.navigateTo(R.id.nav_report_were_any_witnesses)
                } else if (mode == EditReportActivity.MODE_CREATE_NEW || mode == EditReportActivity.MODE_VERIFY_WITNESS) {
                    activity.navigateTo(R.id.nav_report_who_being_harassed)
                } else if (mode == EditReportActivity.MODE_VERIFY_VICTIM) {
                    activity.navigateTo(R.id.nav_report_who_agressor_was)
                }
                save()
            },
            prevClick.subscribe {
                previous()
            },
            activity.onBackPressedObject.subscribe {
                previous()
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

    fun previous() {
        if (activity.mode.get() == EditReportActivity.MODE_VERIFY_AGGRESSOR) {
            activity.finish()
        } else
            activity.navigateTo(R.id.nav_report_happened_before)
    }

    fun dispose() {
//        mDisposable.dispose()
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