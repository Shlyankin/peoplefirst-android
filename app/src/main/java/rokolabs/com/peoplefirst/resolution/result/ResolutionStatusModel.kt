package rokolabs.com.peoplefirst.resolution.result

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_resolution_status.*
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.resolution.confirm.ConfirmResolutionActivity
import javax.inject.Inject

class ResolutionStatusModel @Inject constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: ResolutionStatusActivity = context as ResolutionStatusActivity
    var mDisposable = CompositeDisposable()

    var noResolution: ObservableField<Boolean> = ObservableField()
    var rejected: ObservableField<Boolean> = ObservableField()
    var locationTime: ObservableField<String> = ObservableField()

    public var goToReportsClick: Subject<View> = PublishSubject.create()

    init {
        rejected.set(false)
        noResolution.set(false)
    }

    fun initDisposable() {
        mDisposable.addAll(
            goToReportsClick.subscribe {
                activity.onBackPressed()
            }
        )
    }

    fun dispose() {
        mDisposable.clear()
    }
}