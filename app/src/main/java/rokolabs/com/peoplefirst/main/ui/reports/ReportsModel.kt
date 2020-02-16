package rokolabs.com.peoplefirst.main.ui.reports

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.repository.HarassmentRepository

class ReportsModel(
    mContext: Context,
    mRepository: HarassmentRepository,
    mService: PeopleFirstService
) {

    var activeAdapter=ReportsAdapter()
    var inactiveAdapter=ReportsAdapter()

    private lateinit var context: Context
    private lateinit var mRepository: HarassmentRepository
    private lateinit var mService: PeopleFirstService

    var mActive:ObservableField<String> = ObservableField()
    var mSubmitted:ObservableField<String> = ObservableField()

    var addReportSubject: Subject<View> =PublishSubject.create<View>()
    var mDisposable=CompositeDisposable()
    init {
        this.context = mContext
        this.mRepository = mRepository
        this.mService = mService
    }
    fun initDisposable(){
        mDisposable.addAll(
            mRepository.myOpenReports.subscribe {
                mActive.set("Reports that need your attention: " + it.size)
                activeAdapter.setEntities(context, it)
                inactiveAdapter.notifyDataSetChanged()
            },
            mRepository.myClosedResolvedReports.subscribe {
                mSubmitted.set("Reports that do not need attention: " + it.size)
                inactiveAdapter.setEntities(context,it)
                inactiveAdapter.notifyDataSetChanged()
            }
        )
    }
    fun dispose(){
        mDisposable.dispose()
    }
}