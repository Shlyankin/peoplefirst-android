package rokolabs.com.peoplefirst.report.involved.named

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.Response
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.responses.BaseResponse
import rokolabs.com.peoplefirst.report.involved.verify.witness.VerifyActivity
import rokolabs.com.peoplefirst.report.involved.victim.CollegueBelievesActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class NamedModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity = context as NamedActivity
    private var mDisposable = CompositeDisposable()
    var continueSubject: Subject<View> = PublishSubject.create()
    var denySubject: Subject<View> = PublishSubject.create()
    fun initDisposable() {
        mDisposable.addAll(
            continueSubject.subscribe {
                val intent = Intent(activity, VerifyActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }, denySubject.subscribe {
                if (mRepository.named == HarassmentRepository.WITNESS) {
                    mService.rejectWitnessReport(mRepository.currentReport.value!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { r: Response<BaseResponse?> ->
                                if (r.body() != null && r.body()!!.success) {
                                    Toast.makeText(activity, "report rejected", Toast.LENGTH_LONG)
                                        .show()
                                } else if (r.errorBody() != null) {
                                    Toast.makeText(
                                        activity,
                                        Gson().fromJson(
                                            r.errorBody()!!.string(),
                                            BaseResponse::class.java
                                        ).error.message
                                        , Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        ) { throwable: Throwable? ->
                            Toast.makeText(
                                activity,
                                "Could not reject report",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                } else if (mRepository.named == HarassmentRepository.AGGRESSOR) {
                    mService.rejectTransgressorReport(mRepository.currentReport.value!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { r: Response<BaseResponse?> ->
                                if (r.body() != null && r.body()!!.success) {
                                    Toast.makeText(activity, "report rejected", Toast.LENGTH_LONG)
                                        .show()
                                } else if (r.errorBody() != null) {
                                    Toast.makeText(
                                        activity,
                                        Gson().fromJson(
                                            r.errorBody()!!.string(),
                                            BaseResponse::class.java
                                        ).error.message
                                        , Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        ) { throwable: Throwable? ->
                            Toast.makeText(
                                activity,
                                "Could not reject report",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
                mRepository.named = HarassmentRepository.EMPTY
                activity.finish()
            }
        )
    }

    fun dispose() {
        mDisposable.clear()
    }
}