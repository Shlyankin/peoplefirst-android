package rokolabs.com.peoplefirst.auth.login

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.auth.password.reset.send.request.ResetPasswordActivity
import rokolabs.com.peoplefirst.auth.registration.CreateAccountRetailActivity
import rokolabs.com.peoplefirst.main.MainActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import javax.inject.Inject

class LoginViewModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: LoginActivity
    var mDisposable = CompositeDisposable()

    var email: ObservableField<String> = ObservableField()
    var pass: ObservableField<String> = ObservableField()
    var registerClick: Subject<View> = PublishSubject.create()
    var loginClick: Subject<View> = PublishSubject.create()
    var forgotClick: Subject<View> = PublishSubject.create()

    init {
        activity = context as LoginActivity
        if ("" != Utils.getPermanentValue("x-access-token", activity)) {
            gotoReportList()
            mRepository.getMe()
        }
    }

    fun initDisposable() {
        mDisposable.addAll(
            loginClick.subscribe {
                mService.auth(email.get(), pass.get())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { loginRespose ->
                            if (loginRespose.success) {
                                Utils.savePermanentValue(
                                    "x-access-token",
                                    loginRespose.data.token,
                                    activity.applicationContext
                                )
                                sendPushNotificationToken()
                                mRepository.getMe()
                                mRepository.getMyReports()
                                if ("shown" == Utils.getPermanentValue(
                                        "welcome",
                                        activity.applicationContext
                                    )
                                ) {
                                    gotoReportList()
                                } else {
                                    Utils.savePermanentValue(
                                        "welcome",
                                        "shown",
                                        activity.applicationContext
                                    )
                                    gotoReportList()
                                }
                            }
                        },
                        { throwable ->
                            Toast.makeText(context, "Unable to authroize", Toast.LENGTH_LONG).show()
                        })
            },
            forgotClick.subscribe {
                activity.startActivity(Intent(context, ResetPasswordActivity::class.java))
            },
            registerClick.subscribe {
                activity.startActivity(
                    Intent(
                        context,
                        CreateAccountRetailActivity::class.java
                    )
                )
            })
    }
    fun dispose(){
        mDisposable.clear()
    }
    fun gotoReportList() {
        val intent = Intent(context, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
    private fun sendPushNotificationToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FirebaseInstanceId", "getInstanceId failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result!!.token
            Log.d("FirebaseInstanceId", "token = $token")

            val deviceId = Settings.Secure.getString(
                activity.applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID
            )
            mService.sendPushNotificationsToken(deviceId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        Log.d("LoginPresenter", "firebase token sent successfully")
                    } else {
                        Log.e("LoginPresenter", response.errorBody()!!.toString())
                    }
                },
                    { e -> e.printStackTrace() })
        }
    }
}