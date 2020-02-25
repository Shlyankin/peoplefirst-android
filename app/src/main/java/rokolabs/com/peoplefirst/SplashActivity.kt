package rokolabs.com.peoplefirst

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.Toast

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.auth.login.LoginActivity
//import rokolabs.com.peoplefirst.auth.login.LoginActivity
import rokolabs.com.peoplefirst.auth.password.reset.update.SetNewPasswordActivity
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.model.requests.ValidateEmailRequest

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var mService: PeopleFirstService

    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        handleDeepLink()
    }

    private fun handleDeepLink() {
        val activity = this
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    val deepLink: Uri?
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                        if (deepLink != null) {
                            val path = deepLink.lastPathSegment
                            when (path) {
                                "auth" -> kotlin.run {
                                    var email = deepLink.getQueryParameter("user")!!
                                    var token = deepLink.getQueryParameter("token")!!
                                    mService!!.validateEmail(ValidateEmailRequest(email, token))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeBy(onSuccess = {
                                                if (it.success) {
                                                    Toast.makeText(this, "Account verified", Toast.LENGTH_LONG).show()
                                                    startActivity(Intent(this, LoginActivity::class.java))
                                                }
                                            })
                                }
                                "setpassword"->kotlin.run {
                                    var email = deepLink.getQueryParameter("user")!!
                                    var token = deepLink.getQueryParameter("token")!!
                                    var intent=Intent(this,SetNewPasswordActivity::class.java)
                                    intent.putExtra("token",token)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    } else {
                        startLoginActivity()
                    }
                }
                .addOnFailureListener(this) { startLoginActivity() }
    }

    @SuppressLint("CheckResult")
    private fun startLoginActivity() {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe { i ->
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
    }
}
