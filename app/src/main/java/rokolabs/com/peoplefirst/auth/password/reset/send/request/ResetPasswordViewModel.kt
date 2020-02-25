package rokolabs.com.peoplefirst.auth.password.reset.send.request

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import javax.inject.Inject

class ResetPasswordViewModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: ResetPasswordActivity
    var mDisposable = CompositeDisposable()

    var email: ObservableField<String> = ObservableField()
    var resetClick: Subject<View> = PublishSubject.create()

    init {
        activity = context as ResetPasswordActivity
    }

    fun initDisposable() {
        mDisposable.addAll(
            resetClick.subscribe {
                mDisposable.add(
                    mService.resetPassword(email?.get())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { baseResponse ->
                                if (baseResponse.success) {
                                    Toast.makeText(
                                        context,
                                        "Reset email has been sent",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    activity.finish()
                                } else {
                                    Toast.makeText(
                                        context,
                                        baseResponse.error.message,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            },
                            { throwable ->
                                Toast.makeText(
                                    context,
                                    "Could not reset password",
                                    Toast.LENGTH_LONG
                                ).show()
                            })
                )
            }
        )
    }

    fun dispose() {
        mDisposable.clear()
    }
}