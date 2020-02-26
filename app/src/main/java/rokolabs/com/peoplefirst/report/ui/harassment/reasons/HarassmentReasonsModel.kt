package rokolabs.com.peoplefirst.report.ui.harassment.reasons

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.HarassmentReason
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import java.util.ArrayList
import javax.inject.Inject

class HarassmentReasonsModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var acitivity: EditReportActivity
    private var mDisposable = CompositeDisposable()

    var mAdapter: ReasonsAdapter
    private var selectedReasons = ArrayList<HarassmentReason>()

    var prevSubject: Subject<View> = PublishSubject.create()
    var nextSubject: Subject<View> = PublishSubject.create()

    init {
        mAdapter = ReasonsAdapter()
        acitivity = context as EditReportActivity
    }

    fun initDisposable() {
        mDisposable = CompositeDisposable()
        mDisposable.addAll(
            prevSubject.subscribe {
                previous()
            },
            acitivity.onBackPressedObject.subscribe {
                previous()
            },
            nextSubject.subscribe {
                if (save())
                    acitivity.navigateTo(R.id.nav_report_happened_before)
            },
            mAdapter.typeClick.subscribe { harassmentReason ->
                if (selectedReasons.indexOf(harassmentReason) >= 0)
                    selectedReasons.remove(harassmentReason)
                else
                    selectedReasons.add(harassmentReason)
            },
            mService.harassmentReasons
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    it.printStackTrace()
                    showToast("Unable to get harassment types")
                }, onSuccess = {
                    if (it.success) {
                        showReasons(it.data)
                    }
                }),
            mRepository.currentReport.subscribe { report ->
                if (report !== Report.EMPTY) selectedReasons = report.harassment_reasons
            }
        )
    }

    fun previous() {
        acitivity.navigateTo(R.id.nav_harassment_type)
    }

    private fun save(): Boolean {
        val intent = Intent()
        if (selectedReasons.size == 0) {
            showToast("This info is required")
            return false
        }
        if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null) {
            mRepository.currentReport.value?.harassment_reasons = selectedReasons
            mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
        }
        return true
    }

    fun showReasons(reasons: ArrayList<HarassmentReason>) {
        mAdapter.setItems(
            context, reasons,
            if (mRepository.currentReport.value !== Report.EMPTY
                && mRepository.currentReport.value != null && mRepository.currentReport.value?.harassment_reasons != null
            )
                mRepository.currentReport.value?.harassment_reasons
            else
                ArrayList()
        )
        mAdapter.notifyDataSetChanged()
    }

    fun dispose() {
        mDisposable.dispose()
        mDisposable.clear()
    }

    fun showToast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }
}