package rokolabs.com.peoplefirst.report.ui.home

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

class MainQuestionsModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: EditReportActivity
    var mDisposable = CompositeDisposable()

    var whatHappenedObject: Subject<View> = PublishSubject.create()
    var whoInvolvedObject: Subject<View> = PublishSubject.create()
    var whereHappenedObject: Subject<View> = PublishSubject.create()
    var whenHappenedObject: Subject<View> = PublishSubject.create()

    init {
        activity = context as EditReportActivity
    }

    fun initDisposable() {
        mDisposable = CompositeDisposable()
        mDisposable.addAll(
            whatHappenedObject.subscribe {
                activity.navigateTo(it)
            },
            whoInvolvedObject.subscribe {
                activity.navigateTo(it)
            },
            whereHappenedObject.subscribe {
                activity.navigateTo(it)
            },
            whenHappenedObject.subscribe {
                activity.navigateTo(it)

            },
            activity.onBackPressedObject.subscribe {
                previous()
            }
        )
    }
    fun previous(){
        activity.finish()
    }


    fun dispose() {
        mDisposable.dispose()
        mDisposable.clear()
    }
}