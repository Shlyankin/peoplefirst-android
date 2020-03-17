package rokolabs.com.peoplefirst.report.ui.summary.confirm

import android.app.Activity
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
import rokolabs.com.peoplefirst.report.ui.notification.result.ResultNotificationActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import javax.inject.Inject

class ConfirmModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity: Activity = context as Activity
    private var mDisposable = CompositeDisposable()


    var doneSubject: Subject<View> = PublishSubject.create()
    var mLocationTimeCaption: ObservableField<String> = ObservableField()
    var whatHappened: ObservableField<String> = ObservableField()
    var witnesses: ObservableField<String> = ObservableField()
    var readOnly: ObservableField<Boolean> = ObservableField()

    init {
        readOnly.set(false)
    }

    fun initDisposable() {
        mDisposable.addAll(
            doneSubject.subscribe {
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
                        if (activity is EditReportActivity) {
                            (activity as EditReportActivity).navigateTo(R.id.nav_report_profile_confirmation)
                        }
                    }
                }
            },
            mRepository.currentReport.subscribe {
                mLocationTimeCaption.set(
                    "You were at "
                            + it.location_city
                            + " " + it.location_details
                            + " on " + Utils.newDateFormat(it.datetime)
                            + " at or around " + Utils.newTimeFormat(it.datetime)
                );
                //mWitnessedInteractions.setText(report.victim.first_name + " " + report.victim.last_name);
            },
            mRepository.currentReport.subscribe {
                whatHappened.set(it.details)
                if (it.witnesses.size > 0) {
                    var text = ""
                    for (w in it.witnesses) {
                        text = text + w.first_name + " " + w.last_name + "\n"
                    }
                    witnesses.set(text)
                } else {
                    witnesses.set("No")
                }
            }
        )
        if (activity is EditReportActivity) {
            mDisposable.add(
                (activity as EditReportActivity).onBackPressedObject.subscribe {
                    (activity as EditReportActivity).navigateTo(R.id.nav_report_were_any_witnesses)
                }
            )
        }

    }

    fun dispose() {
        mDisposable.clear()
    }
}