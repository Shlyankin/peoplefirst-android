package rokolabs.com.peoplefirst.report.ui.details.happened.before

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.layout_edit_profile_drawer.view.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject


class HappenedBeforeModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity:EditReportActivity
    var mDisposable = CompositeDisposable()

    var nextClick : Subject<View> = PublishSubject.create()
    var prevClick : Subject<View> = PublishSubject.create()
    var yesSubject : Subject<View> =PublishSubject.create()
    var noSubject : Subject<View> =PublishSubject.create()
    var yesFlag:ObservableField<Boolean> = ObservableField()
    init {
        yesFlag.set(false)
        activity=context as EditReportActivity
    }
    fun initDisposable(){
        mDisposable=CompositeDisposable()
        mDisposable.addAll(
            nextClick.subscribe {
                save()
                activity.navigateTo(R.id.menuItem4)
            },
            prevClick.subscribe {
                save()
                activity.navigateTo(R.id.menuItem2)
            },
            yesSubject.subscribe {
                yesFlag.set(true)
            },
            noSubject.subscribe {
                yesFlag.set(false)
            }
        )
        if (mRepository.named == HarassmentRepository.EMPTY) {
            mDisposable.add(
                mRepository.currentReport.subscribe { report ->
                    if (report !== Report.EMPTY) {
                        yesFlag.set(report.happened_before != null && report.happened_before)
                    }
                }
            )
        } else if (mRepository.named == HarassmentRepository.VICTIM) {
            mDisposable.add(
                mRepository.currentVictimTestimony.subscribe { report ->
                    yesFlag.set(report.happened_before != null && report.happened_before)

                }
            )
        }
    }
    fun dispose(){
        mDisposable.dispose()
        mDisposable.clear()
    }
    private fun save() {
        if (mRepository.named == HarassmentRepository.VICTIM) {
            mRepository.currentVictimTestimony.value?.happened_before = yesFlag.get()
            mRepository.currentVictimTestimony.onNext(mRepository.currentVictimTestimony.getValue()!!)
        } else if (mRepository.named == HarassmentRepository.WITNESS) {
            mRepository.currentWitnessTestimony.value?.happened_before = yesFlag.get()
            mRepository.currentWitnessTestimony.onNext(mRepository.currentWitnessTestimony.getValue()!!)
        }
        if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null) {
            mRepository.currentReport.value?.happened_before = yesFlag.get()
            mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
        }
    }
}