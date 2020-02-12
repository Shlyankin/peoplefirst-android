package rokolabs.com.peoplefirst.auth.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.databinding.ActivityLoginBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var mRepository: HarassmentRepository
    @Inject
    lateinit var mService: PeopleFirstService
    var viewModel: LoginViewModel = LoginViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        var binder =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binder.viewModel = viewModel
        if ("" != Utils.getPermanentValue("x-access-token", this)) {
            gotoReportList()
            mRepository.getMe()
        }
        viewModel.loginClick.subscribe {
            mService.auth(viewModel.email.get(), viewModel.pass.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginRespose ->
                        if (loginRespose.success) {
                            Utils.savePermanentValue(
                                "x-access-token",
                                loginRespose.data.token,
                                applicationContext
                            )
                            sendPushNotificationToken()
                            mRepository.getMe()
                            mRepository.getMyReports()
                            if ("shown" == Utils.getPermanentValue("welcome", applicationContext)) {
                                gotoReportList()
                            } else {
                                Utils.savePermanentValue("welcome", "shown", applicationContext)
                                gotoIntro()
                            }
                        }
                    },
                    { throwable ->
                        Toast.makeText(this, "Unable to authroize", Toast.LENGTH_LONG).show()
                    })
        }
    }

    fun gotoReportList() {
//        val intent = Intent(this, ReportsActivity::class.java)
//        startActivity(intent)
//        finish()
    }

    fun gotoIntro() {
//        val intent = Intent(this, IntroActivity::class.java)
//        startActivity(intent)
//        finish()
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
                applicationContext.getContentResolver(),
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