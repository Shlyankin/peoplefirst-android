package rokolabs.com.peoplefirst.report.ui.notification.result

import android.content.Context
import android.text.Html
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class ResultNotificationModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity: ResultNotificationActivity = context as ResultNotificationActivity
    private var mDisposable = CompositeDisposable()

    public var goToReportsClick: Subject<View> = PublishSubject.create()
    public var title: ObservableField<String> = ObservableField()
    public var body: ObservableField<String> = ObservableField()
    fun initDisposable() {
        mDisposable.addAll(
            mRepository.currentReport.subscribe {
                when (mRepository.defineName(it)) {
                    HarassmentRepository.AGGRESSOR -> {
                        showAggressorText()
                    }
                    HarassmentRepository.EMPTY -> {

                    }
                    HarassmentRepository.VICTIM -> {
                        showVictimText()
                    }
                    HarassmentRepository.WITNESS -> {
                        showWitnessText()
                    }
                }
            },
            goToReportsClick.subscribe {
                activity.finish()
            }
        )
    }

    fun dispose() {

    }

    private fun showVictimText() {
        if (mRepository.currentReport.value!!.witnesses.isEmpty()) {
            title.set("Your report has been submitted")
            val text =
                "<b>" + mRepository.me.value!!.company.name + "</b> has <b>60 days</b> to respond to and resolve your report. If a resolution is not reached within this time, we will provide the steps you need to take to submit your report to city, state, and federal agencies for investigation."
            body.set(Html.fromHtml(text).toString())
        } else {
            title.set("Your report has been created")
            body.set("All witnesses have been notified. Your report will be submitted to " + mRepository.me.value!!.company.name + " within 10 days.")
        }
    }

    private fun showWitnessText() {
        title.set(mRepository.currentReport.value!!.victim.first_name + " " + mRepository.currentReport.value!!.victim.last_name + " has been notified")
        body.set("If they verify the report it will be submitted to " + mRepository.currentReport.value!!.victim.company.name)
    }

    private fun showAggressorText() {
        title.set("Your report has been submitted")
        body.set("")
    }
}