package rokolabs.com.peoplefirst.main.ui.reports

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.main.MainActivity
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
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
                activity.startActivity(Intent(context, EditReportActivity::class.java))
            },
            addReportSubject.subscribe {
                mRepository.currentReport.onNext(Report())
//                mRepository.named = HarassmentRepository.EMPTY
                activity.startActivity(Intent(context, EditReportActivity::class.java))
            }
        )
        mRepository.getMyReports()
    }

    fun dispose() {
        mDisposable.clear()
    }
}