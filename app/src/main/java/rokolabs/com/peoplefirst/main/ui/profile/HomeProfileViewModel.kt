package rokolabs.com.peoplefirst.main.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.auth.login.LoginActivity
import rokolabs.com.peoplefirst.main.MainActivity
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import javax.inject.Inject

class HomeProfileViewModel @Inject constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: MainActivity

    var changePassword: Subject<View> = PublishSubject.create()
    var logOut: Subject<View> = PublishSubject.create()

    var mDisposable = CompositeDisposable()

    init {
        activity = context as MainActivity


    }

    fun initDisposable() {
        mDisposable.addAll(
            logOut.subscribe {
                mService.deletePushNotificationsToken(
                    Settings.Secure.getString(
                        context?.applicationContext?.getContentResolver(),
                        Settings.Secure.ANDROID_ID
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        if (response.success)
                            Log.d("ProfileActivity", "firebase token deleted successfully")
                        logout()
                    },
                        { e ->
                            e.printStackTrace()
                            logout()
                        })
            },
            changePassword.subscribe {
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("We'll send you an email to reset your password")
                    .setMessage("Please confirm you'd like to do this")
                    .setCancelable(false)
                    .setPositiveButton("Confirm") { dialog, which ->
                        mService.resetPassword(mRepository.me.value?.email)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                { baseResponse ->
                                    if (baseResponse.success) {
                                        Toast.makeText(
                                            context,
                                            "Reset email sent",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            baseResponse.error.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                { throwable ->
                                    Toast.makeText(
                                        context,
                                        "Could not reset password",
                                        Toast.LENGTH_LONG
                                    ).show()
                                })
                        dialog.cancel()
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, id -> dialog.cancel() }
                val alert = builder.create()
                alert.show()
            }
        )

    }

    fun dispose() {
        mDisposable.clear()
    }
    @SuppressLint("CheckResult")
    private fun logout() {
        mService.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { baseResponse ->
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                    Utils.savePermanentValue("x-access-token", "", context)
                    mRepository.me.onNext(User.EMPTY)
                },
                { throwable ->
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                    Utils.savePermanentValue("x-access-token", "", context)
                    mRepository.me.onNext(User.EMPTY)
                })
    }
}