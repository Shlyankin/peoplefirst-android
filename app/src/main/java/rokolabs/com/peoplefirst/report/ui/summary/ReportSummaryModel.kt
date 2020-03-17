package rokolabs.com.peoplefirst.report.ui.summary

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.model.responses.UploadedFilesResponse
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.ui.notification.result.ResultNotificationActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import javax.inject.Inject

class ReportSummaryModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private lateinit var activity: EditReportActivity
    private var mDisposable = CompositeDisposable()

    var submitClick: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()
    var editClick: Subject<View> = PublishSubject.create()

    var anonymousChecked: ObservableField<Boolean> = ObservableField()
    var harassmentType: ObservableField<String> = ObservableField()
    var harassmentBasis: ObservableField<String> = ObservableField()
    var happenedBefore: ObservableField<String> = ObservableField()
    var whatHappened: ObservableField<String> = ObservableField()
    var whoHarassed: ObservableField<String> = ObservableField()
    var whoAggressor: ObservableField<String> = ObservableField()
    var wereWitnesses: ObservableField<String> = ObservableField()
    var place: ObservableField<String> = ObservableField()
    var dateTime: ObservableField<String> = ObservableField()
    var howResolved: ObservableField<String> = ObservableField()
    var title: ObservableField<String> = ObservableField()
    var readOnly: ObservableField<Boolean> = ObservableField()

    var anonymousCheckedVisibility: ObservableField<Int> = ObservableField()
    var doneVisibility: ObservableField<Int> = ObservableField()
    var tapAnyQuestionVisibility: ObservableField<Int> = ObservableField()

    init {
        if (context is EditReportActivity) {
            activity = context
        }
        anonymousCheckedVisibility.set(View.VISIBLE)
        doneVisibility.set(View.VISIBLE)
        tapAnyQuestionVisibility.set(View.VISIBLE)
        title.set("Your Report")
        readOnly.set(false)
    }

    private val mMode = EDIT_MODE
    fun previous() {
        var mode = activity.mode.get()
        if (mode == EditReportActivity.MODE_VERIFY_VICTIM || mode == EditReportActivity.MODE_CREATE_NEW) {
            activity.navigateTo(R.id.nav_report_how_resolved)
        } else {
            activity.navigateTo(R.id.nav_report_were_any_witnesses)
        }
    }

    fun initDisposable() {
        mDisposable.addAll(editClick.subscribe {
            if (!readOnly.get()!!) {
                activity.navigateTo(it)
            }
        },
            mRepository.currentReport.subscribe {
                showReport(it)
            }
        )
        if (!readOnly.get()!!) {
            mDisposable.addAll(
                submitClick.subscribe {
                    if (readOnly.get()!! && checkRequired()) {
                        mRepository.currentReport.onNext(mRepository.currentReport.value!!)
                        mRepository.me.subscribe {
                            if (it.isFullyFilled) {
                                activity.startActivity(
                                    Intent(
                                        activity,
                                        ResultNotificationActivity::class.java
                                    )
                                )
                                activity.finish()
                            } else {
                                activity.navigateTo(R.id.nav_report_profile_confirmation)
                            }
                        }
                    }
                }

            )
        }
        if (context is EditReportActivity) {
            activity.onBackPressedObject.subscribe {
                previous()
            }
        }
    }

    fun showReport(report: Report) {
        if (mMode != EDIT_MODE) {
            tapAnyQuestionVisibility.set(View.GONE)
            doneVisibility.set(View.GONE)
            anonymousCheckedVisibility.set(View.GONE)
            title.set("Report details")
        }
        anonymousChecked.set(report.anonym)
        if (mRepository.me.value!!.equals(report.victim)) anonymousCheckedVisibility.set(View.GONE)
        if (report.victim != null) whoHarassed.set(report.victim.first_name + "\n" + report.victim.last_name)
        if (report.aggressors != null && report.aggressors.size > 0) whoAggressor.set(
            report.aggressors[0].first_name + "\n" + report.aggressors[0].last_name
        )
        var where = report.location_city
        if (report.location_details != null && !report.location_details.isEmpty()) {
            where += "\n" + report.location_details
        }
        place.set(where)
        whatHappened.set(report.details)
        if (report.harassment_types != null && report.harassment_types.size > 0) {
            var types = ""
            for (type in report.harassment_types) {
                types += type.title + "\n"
            }
            harassmentType.set(types)
        }
        if (report.harassment_reasons != null && report.harassment_reasons.size > 0) {
            var types = ""
            for (reason in report.harassment_reasons) {
                types += reason.title + "\n"
            }
            harassmentBasis.set(types)
        }
        dateTime.set(
            Utils.newDateFormat(report.datetime) + " " + Utils.newTimeFormat(
                report.datetime
            )
        )
        if (report.witnesses != null && report.witnesses.size > 0) {
            val witnesses = StringBuilder()
            for (witness in report.witnesses) {
                witnesses.append(witness.first_name).append("\n").append(witness.last_name)
                    .append("\n")
            }
            wereWitnesses.set(witnesses.substring(0, witnesses.length - 1))
        }
        howResolved.set(report.victim_proposed_solution)
        if (report.happened_before != null) happenedBefore.set(if (report.happened_before) "Yes" else "No")
        if (!report.attachments.isEmpty()) {
//            attachmentsLayout.setVisibility(View.VISIBLE)
//            for (file in report.attachments) {
//                val attachment =
//                    getLayoutInflater().inflate(R.layout.listitem_attachment, null) as ViewGroup
//                (attachment.findViewById<View>(R.id.text) as TextView).text = file.name
//                attachmentsLayout.addView(attachment)
//            }
        } else if (report.id != null) {
            mDisposable.add(mService.getReportAttachments(report.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    Consumer { response: UploadedFilesResponse ->
                        if (response.success && !response.data.isEmpty()) {
//                            attachmentsLayout.setVisibility(View.VISIBLE)
                            var counter = 1
//                            for (data in response.data) {
//                                val attachment = getLayoutInflater().inflate(
//                                    R.layout.listitem_attachment,
//                                    null
//                                ) as ViewGroup
//                                val filename =
//                                    "File " + counter++ + data.url.substring(
//                                        data.url.lastIndexOf(
//                                            "."
//                                        )
//                                    )
//                                (attachment.findViewById<View>(R.id.text) as TextView).text =
//                                    filename
//                                attachmentsLayout.addView(attachment)
//                            }
                        }
                    },
                    Consumer { e: Throwable -> e.printStackTrace() }
                ))
        }
    }

    private fun checkRequired(): Boolean {
        val report: Report =
            mRepository.currentReport.getValue()!!
        var message = "Please complete all information on the following pages:\n"
        if (report.harassment_types.size == 0) {
            message += "Type of harassment took place\n"
        } else {
            var otherSelected = false
            for (ht in report.harassment_types) {
                if (ht.title.contains("ther")) {
                    otherSelected = true
                    break
                }
            }
            if (otherSelected && "" == report.details) {
                message += "What happened\n"
            }
        }
        if (report.victim == null) {
            message += "Who do you believe was harassed\n"
        }
        //aggressor
        if (report.victim != null) {
            if (mRepository.me.getValue()!!.email == report.victim.email) { //current user is victim
                if (report.aggressors.size == 0) {
                    message += "Who dod you believe the aggressor was\n"
                }
            }
        }
        /*if (report.witnesses.size() == 0) {
            message += "Where there any witnesses is required\n";
        }*/if (report.location_city == null || "" == report.location_city) {
            message += "Where did this take place\n"
        }
        if (report.datetime == null || "" == report.datetime || " " == report.datetime) {
            message += "When did this take place\n"
        }
        return if ("Please complete all information on the following pages:\n" != message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            false
        } else {
            true
        }
    }

    fun dispose() {
        mDisposable.clear()
    }

    companion object {
        var EDIT_MODE = 1
    }
}