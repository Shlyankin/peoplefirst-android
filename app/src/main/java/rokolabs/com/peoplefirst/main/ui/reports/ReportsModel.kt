package rokolabs.com.peoplefirst.main.ui.reports

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.main.MainActivity
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.model.WitnessTestimony
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.involved.named.NamedActivity
import rokolabs.com.peoplefirst.report.involved.victim.CollegueBelievesActivity
import rokolabs.com.peoplefirst.report.ui.summary.ReportSummaryActivity
import rokolabs.com.peoplefirst.report.ui.summary.confirm.ConfirmActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.resolution.confirm.ConfirmResolutionActivity
import rokolabs.com.peoplefirst.resolution.result.ResolutionStatusActivity
import javax.inject.Inject

class ReportsModel @Inject constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: MainActivity
    var activeAdapter = ReportsAdapter()
    var inactiveAdapter = ReportsAdapter()


    var mActive: ObservableField<String> = ObservableField()
    var mSubmitted: ObservableField<String> = ObservableField()

    var addReportSubject: Subject<View> = PublishSubject.create<View>()
    var mDisposable = CompositeDisposable()

    init {
        activity = context as MainActivity
    }

    fun initDisposable() {
        mDisposable.addAll(
            mRepository.myOpenReports.subscribe {
                mActive.set("Reports that need your attention: " + it.size)
                activeAdapter.setEntities(context, it)
                activeAdapter.notifyDataSetChanged()
            },
            mRepository.myClosedResolvedReports.subscribe {
                mSubmitted.set("Reports that do not need attention: " + it.size)
                inactiveAdapter.setEntities(context, it)
                inactiveAdapter.notifyDataSetChanged()
            },
            activeAdapter.detailsClicks.subscribe {
                mRepository.currentReport.onNext(it);
//                activity.startActivity(Intent(context, EditReportActivity::class.java))
                loadReport()
//                testLoad()
            },
            addReportSubject.subscribe {
                mRepository.currentReport.onNext(Report())
                EditReportActivity.show(activity)
            }
        )
        mRepository.getMyReports()
    }

    fun dispose() {
        mDisposable.clear()
    }

    fun showVictimAfterHRResolved() {
        val intent = Intent(activity, ConfirmResolutionActivity::class.java)
        activity.startActivity(intent)
    }

    fun showNamedWitness() {
        NamedActivity.showForWitness(activity)
    }

    fun showNamedAggressor() {
        NamedActivity.showForTransgressor(activity)
    }

    fun showWitnessReport() {
        ConfirmActivity.showReadOnly(activity)
    }


    fun showAggressorReport() {
        ConfirmActivity.showReadOnly(activity)
    }

    fun showVictimRreportDetails(edit: Boolean) {
        EditReportActivity.showEdit(activity)

    }

    fun showHRReportNoResolution() {
        ResolutionStatusActivity.showNpResolution(activity)
    }

    fun showHrReportDetails() {
//        val intent = Intent(this, ReportDetailsActivity::class.java)
//        startActivity(intent)
    }

    fun showVictimAfterWtiness() {
        mRepository.named = HarassmentRepository.VICTIM
        mRepository.currentReport.onNext(Report())
        val intent = Intent(activity, CollegueBelievesActivity::class.java)
        activity.startActivity(intent)
    }

    fun showHRAfterVictimAccepted() {
        ResolutionStatusActivity.showAccepted(activity)
    }

    fun showHRAfterVictimRejected() {
        ResolutionStatusActivity.showRejected(activity)
    }

    fun showVictimNamedResolved() {
        val intent = Intent(activity, ResolutionStatusActivity::class.java)
        activity.startActivity(intent)
    }

    fun testLoad() {
        showWitnessReport()
    }

    fun loadReport() {
        mDisposable.addAll(
            mRepository.currentReport.subscribe { report: Report ->
                if (report !== Report.EMPTY) {
                    var isWitness = false
                    var isAggressor = false
                    for (aggressor in report.aggressors) {
                        if (aggressor.email == mRepository.me.value!!.email) {
                            isAggressor = true
                            break
                        }
                    }
                    for (witness in report.witnesses) {
                        if (witness.email != null && witness.email == mRepository.me.value!!.email) {
                            isWitness = true
                            break
                        }
                    }
                    if (isAggressor) {
                        mRepository.getTransgressorReport()
                        mRepository.named = HarassmentRepository.AGGRESSOR
                        val reportAggressor =
                            report.aggressors[report.aggressors.indexOf(mRepository.me.value)]
                        if ("new" == reportAggressor.report_aggressor_status) {
                            showNamedAggressor()
                        } else {
                            // TODO: show editing screens
                            showAggressorReport()
                        }
                    } else if (isWitness) {
                        mRepository.currentWitnessTestimony.onNext(WitnessTestimony())
                        mRepository.named = HarassmentRepository.WITNESS
                        val reportWitness =
                            report.witnesses[report.witnesses.indexOf(mRepository.me.value)]
                        if (report.author_id != mRepository.me.value!!.id) {
                            if ("new" == reportWitness.report_witness_status) {
                                showNamedWitness()
                            } else {
                                showWitnessReport()
                            }
                        } else {
                            // TODO: show editing screens
                            showVictimRreportDetails(false)
                        }
                    } else if ("hr" == mRepository.me.value!!.role) {
                        if ("resolved" == report.status) {
                            showHRAfterVictimAccepted()
                        } else if ("submitted" == report.status && report.resolutions_offered > 0) {
                            showHRAfterVictimRejected()
                        } else if ("submitted" == report.status && report.resolutions_offered == 0) {
                            showHrReportDetails()
                        } else if ("resolution pending" == report.status) {
                            showHrReportDetails()
                        } else if ("resolved" != report.status) {
                            showHRReportNoResolution()
                        } else {
                            showHrReportDetails()
                        }
                    } else if (report.victim.id === mRepository.me.value!!.id && "resolution pending" == report.status) {
                        showVictimAfterHRResolved()
                    } else if (report.victim.id === mRepository.me.value!!.id && report.author_id != mRepository.me.value!!.id) {
                        if ("created" == report.status) {
                            showVictimAfterWtiness()
                        } else if ("resolved" == report.status) {
                            showVictimNamedResolved()
                        } else {
                            showVictimRreportDetails(false)
                        }
                    } else if (report.author_id == mRepository.me.value!!.id) {
                        showVictimRreportDetails(
                            "open" == report.status || "created" == report.status
                        )
                    }
//                    if (currentDisposable != null) currentDisposable.dispose()
                }
            }
        )
    }
}