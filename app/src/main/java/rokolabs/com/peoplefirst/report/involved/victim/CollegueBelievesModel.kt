package rokolabs.com.peoplefirst.report.involved.victim

import android.content.Context
import android.content.Intent
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
import rokolabs.com.peoplefirst.report.involved.rights.AfterYouViewActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class CollegueBelievesModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity = context as CollegueBelievesActivity
    private var mDisposable = CompositeDisposable()
    var continueSubject: Subject<View> = PublishSubject.create()
    var denySubject: Subject<View> = PublishSubject.create()
    fun initDisposabale() {
        mDisposable.addAll(
            continueSubject.subscribe {
                val intent = Intent(activity, AfterYouViewActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            },
            denySubject.subscribe {
                mService.victimConfirmReport(mRepository.currentReport.value!!.id, "deny")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { br: BaseResponse ->
                            if (br.success) {
//                                val intent = Intent(this, UpdatedReportActivity::class.java)
//                                startActivity(intent)
//                                finish()
                            } else Toast.makeText(
                                activity,
                                br.error.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    ) { throwable: Throwable? ->
                        Toast.makeText(
                            context,
                            "Could not deny report",
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