package rokolabs.com.peoplefirst.report.ui.agressor

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class WhoAgressorWasModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity:EditReportActivity
    var mDisposable=CompositeDisposable()

    var nextClick :Subject<View> = PublishSubject.create()
    var prevClick :Subject<View> = PublishSubject.create()

    init {
        activity= context as EditReportActivity
    }
    fun initDisposable(){
        mDisposable.addAll(
            nextClick.subscribe {

            },
            prevClick.subscribe {

            }
        )
    }
    fun dispose(){
        mDisposable.clear()
    }
}