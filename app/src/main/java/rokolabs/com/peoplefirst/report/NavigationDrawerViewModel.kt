package rokolabs.com.peoplefirst.report

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class NavigationDrawerViewModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var mActivity:EditReportActivity = context as EditReportActivity
    private var mDisposable = CompositeDisposable()
    var currentPos :ObservableField<Int> = ObservableField()
    var menuItemClick: Subject<View> = PublishSubject.create()

    var harassmentTypeDone : ObservableField<Boolean> =ObservableField()
    var harassmentreasonsDone : ObservableField<Boolean> = ObservableField()
    var detailsDone : ObservableField<Boolean> =ObservableField()
    var victimDone : ObservableField<Boolean> =ObservableField()
    var agressorsDone : ObservableField<Boolean> =ObservableField()
    var witnessesDone : ObservableField<Boolean> =ObservableField()
    var locationCityDone : ObservableField<Boolean> =ObservableField()
    var dateTimeDone : ObservableField<Boolean> =ObservableField()
    var happenedBeforeDone : ObservableField<Boolean> =ObservableField()

    init {
        currentPos.set(R.id.nav_main_questions)
        initDisposable()
    }

    fun initDisposable() {
        mDisposable.addAll(
            menuItemClick.subscribe {
                mActivity.navigateTo(it)
            },
            mRepository.currentReport.subscribe { report ->
                harassmentTypeDone.set(report.harassment_types.size > 0)
                harassmentreasonsDone.set(report.harassment_reasons.size > 0)
                detailsDone.set(report.details != null && "" != report.details)
                victimDone.set(report.victim != null)
                agressorsDone.set(report.aggressors.size > 0)
                witnessesDone.set(report.witnesses.size > 0)
                locationCityDone.set(report.location_city != null || report.location_details != null)
                dateTimeDone.set(report.datetime != null)
                happenedBeforeDone.set(report.happened_before != null)
            }
        )
    }

    fun dispose() {
        mDisposable.dispose()
    }
}