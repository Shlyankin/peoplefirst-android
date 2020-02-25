package rokolabs.com.peoplefirst.auth.password.reset.update

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.auth.login.LoginActivity
import rokolabs.com.peoplefirst.model.requests.UpdatePasswordRequest
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class SetNewPasswordViewModel  @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: SetNewPasswordActivity
    var mDisposable = CompositeDisposable()
    var token: String? = null

    var newPassword: ObservableField<String> = ObservableField()
    var setClick: Subject<View> = PublishSubject.create()

    init {
        activity= context as SetNewPasswordActivity
    }
    fun initDisposable(){
        mDisposable.addAll(
            setClick.subscribe {
                if (token != null && newPassword.get() != "") {
                    mService.setNewPassword(UpdatePasswordRequest(token, newPassword.get()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError { it.printStackTrace() }
                        .subscribeBy(onSuccess = {
                            if (it.success) {
                                Toast.makeText(context, "Password has been updated", Toast.LENGTH_LONG)
                                    .show()
                                activity.startActivity(Intent(context, LoginActivity::class.java))
                                activity.finish()
                            }
                        }, onError = { it.printStackTrace() })
                }
            }
        )
    }
    fun dispose(){
        mDisposable.clear()
    }
}