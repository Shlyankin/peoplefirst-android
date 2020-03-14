package rokolabs.com.peoplefirst.resolution.confirm

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.responses.BaseResponse
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.resolution.result.ResolutionStatusActivity
import javax.inject.Inject

class ConfirmResolutionModel @Inject constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: ConfirmResolutionActivity = context as ConfirmResolutionActivity
    var mDisposable = CompositeDisposable()

    var acceptClick: Subject<View> = PublishSubject.create()
    var rejectClick: Subject<View> = PublishSubject.create()

    fun initDisposable() {
        mDisposable.addAll(
            rejectClick.subscribe {
//                ResolutionStatusActivity.showRejected(activity)
                mService.reopenReport(mRepository.currentReport.value!!.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { br: BaseResponse ->
                            if (br.success) {
                                mRepository.getMyReports()
                                ResolutionStatusActivity.showRejected(activity)
                                activity.finish()
                            } else Toast.makeText(context, br.error.message, Toast.LENGTH_LONG)
                        }
                    ) { throwable: Throwable? ->
                        Toast.makeText(
                            context,
                            "Could not reopen report",
                            Toast.LENGTH_LONG
                        )
                    }
            },
            acceptClick.subscribe {
//                ResolutionStatusActivity.showAccepted(activity)
                mService.closeReport(mRepository.currentReport.value!!.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { br: BaseResponse ->
                            if (br.success) {
                                mRepository.getMyReports()
                                ResolutionStatusActivity.showAccepted(activity)
                                activity.finish()
                            } else Toast.makeText(
                                context,
                                br.error.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    ) { throwable: Throwable? ->
                        Toast.makeText(
                            context,
                            "Could not close report",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        )
    }

    fun dispose() {
        mDisposable.clear()
    }
}