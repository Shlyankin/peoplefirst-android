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
        when (mRepository.named) {
            HarassmentRepository.VICTIM ->
                // victim verify flow
                mRepository.currentVictimTestimony.subscribe { testimony ->
                    selectedReasons = testimony.harassment_reasons
                }
            HarassmentRepository.WITNESS ->
                // witness verify flow
                mRepository.currentWitnessTestimony.subscribe { report ->
                    selectedReasons = report.harassment_reasons
                }
            else ->
                // victim flow
                mRepository.currentReport.subscribe { report ->
                    if (report !== Report.EMPTY) selectedReasons = report.harassment_reasons
                }
        }
    }

    fun initDisposable() {
        mDisposable.addAll(
            prevSubject.subscribe {
                acitivity.navigateTo(R.id.menuItem1)
                save()
            },
            nextSubject.subscribe {
                save()
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
                })
        )
    }

    private fun save(): Boolean {
        val intent = Intent()
        if (selectedReasons.size == 0) {
            showToast("This info is required")
            return false
        }
        if (mRepository.named == HarassmentRepository.VICTIM) {
            mRepository.currentVictimTestimony.value?.harassment_reasons = selectedReasons
            mRepository.currentVictimTestimony.onNext(mRepository.currentVictimTestimony.getValue()!!)
        } else if (mRepository.named == HarassmentRepository.WITNESS) {
            mRepository.currentWitnessTestimony.value?.harassment_reasons = selectedReasons
            mRepository.currentWitnessTestimony.onNext(mRepository.currentWitnessTestimony.getValue()!!)
        } else if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null) {
            mRepository.currentReport.value?.harassment_reasons = selectedReasons
            mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
        }
        return true
    }

    fun showReasons(reasons: ArrayList<HarassmentReason>) {
        if (mRepository.named == HarassmentRepository.VICTIM) {
            mAdapter.setItems(
                context, reasons,
                if (mRepository.currentVictimTestimony.value != null && mRepository.currentVictimTestimony.value?.harassment_reasons != null)
                    mRepository.currentVictimTestimony.value?.harassment_reasons
                else
                    ArrayList()
            )
        } else if (mRepository.named == HarassmentRepository.WITNESS) {
            mAdapter.setItems(
                context, reasons,
                if (mRepository.currentWitnessTestimony.value != null && mRepository.currentWitnessTestimony.value?.harassment_reasons != null)
                    mRepository.currentWitnessTestimony.value?.harassment_reasons
                else
                    ArrayList()
            )
        } else {
            mAdapter.setItems(
                context, reasons,
                if (mRepository.currentReport.value !== Report.EMPTY
                    && mRepository.currentReport.value != null && mRepository.currentReport.value?.harassment_reasons != null
                )
                    mRepository.currentReport.value?.harassment_reasons
                else
                    ArrayList()
            )
        }
        mAdapter.notifyDataSetChanged()
    }

    fun dispose() {
        mDisposable.dispose()
    }

    fun showToast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }
}