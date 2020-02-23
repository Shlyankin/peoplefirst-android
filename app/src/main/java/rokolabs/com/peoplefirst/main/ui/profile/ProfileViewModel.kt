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
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged

class ProfileViewModel (context: Context,mRepository: HarassmentRepository,mService:PeopleFirstService){

    private lateinit var context:Context
    private lateinit var mRepository:HarassmentRepository
    private lateinit var mService: PeopleFirstService

    var name: ObservableField<String> = ObservableField<String>()
    var email: ObservableField<String> = ObservableField<String>()
    var secondaryEmail: ObservableField<String> = ObservableField<String>()
    var birthDate: ObservableField<String> = ObservableField<String>()
    var mailingAddress: ObservableField<String> = ObservableField<String>()
    var unitSuite: ObservableField<String> = ObservableField<String>()
    var telephone: ObservableField<String> = ObservableField<String>()
    var changePassword: Subject<View> = PublishSubject.create()
    var logOut: Subject<View> = PublishSubject.create()
    var editeable = ObservableField<Boolean>()

    var nameError: ObservableField<String> = ObservableField<String>()
    var emailError: ObservableField<String> = ObservableField<String>()
    var secondaryEmailError: ObservableField<String> = ObservableField<String>()
    var birthDateError: ObservableField<String> = ObservableField<String>()
    var mailingAddressError: ObservableField<String> = ObservableField<String>()
    var unitSuiteError: ObservableField<String> = ObservableField<String>()
    var telephoneError: ObservableField<String> = ObservableField<String>()

    var mDisposable=CompositeDisposable()

    init {
        this.context=context
        this.mRepository=mRepository
        this.mService=mService
        enableEditing()
        name.addOnPropertyChanged {
            if (it.get()?.length == 0) {
                nameError.set("First Name is required")
            } else {
                nameError.set(null)
            }
        }
        email.addOnPropertyChanged {
            if (it.get()?.length == 0) {
                emailError.set("Email is required")
            } else {
                emailError.set(null)
            }
        }
        secondaryEmail.addOnPropertyChanged {
            if (it.get()?.length == 0) {
                secondaryEmailError.set("Secondary email is required")
            } else {
                secondaryEmailError.set(null)
            }
        }
        birthDate.addOnPropertyChanged {
            if (it.get()?.length == 0) {
                birthDateError.set("Date of birth is required")
            } else {
                birthDateError.set(null)
            }
        }
        mailingAddress.addOnPropertyChanged {
            if (it.get()?.length == 0) {
                mailingAddressError.set("Mailing address is required")
            } else {
                mailingAddressError.set(null)
            }
        }
//        unitSuite.addOnPropertyChanged {
//            if(it.get()?.length==0){
//                unitSuiteError.set()
//            }else{
//                unitSuiteError.set(null)
//            }
//        }
        telephone.addOnPropertyChanged {
            if (it.get()?.length == 0) {
                telephoneError.set("Phone number is required")
            } else {
                telephoneError.set(null)
            }
        }


    }
    fun initDisposable(){
        mDisposable=CompositeDisposable()
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
            },
            mRepository.me.subscribe { user ->
                if (user !== User.EMPTY) {
//                    user.role
                    name.set(user.first_name + " " + user.last_name)
                    email.set(user.email)
                    secondaryEmail.set(user.secondary_email)
                    birthDate.set(Utils.formatUsersDateOfBirth(user.birthday))
                    mailingAddress.set(user.address)
                    telephone.set(user.phone)
                    unitSuite.set(user.address_unit_apt_suite)
                }
            }
        )
    }
    fun dispose(){
        mDisposable.dispose()
        mDisposable.clear()
    }
    fun checkConditions(): Boolean {
        return name.get()?.length != 0 &&
                email.get()?.length != 0 &&
                secondaryEmail.get()?.length != 0 &&
                birthDate.get()?.length != 0 &&
                mailingAddress.get()?.length != 0 &&
                unitSuite.get()?.length != 0 &&
                telephone.get()?.length != 0
    }

    fun disableEditing() {
        editeable.set(false)
    }

    fun enableEditing() {
        editeable.set(true)
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
    fun saveUser() {
        if (checkConditions()) {
            val user = User()
            try {
                user.first_name =
                    name.get()!!.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                user.last_name =
                    name.get()!!.toString().split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
            } catch (e: Exception) {
                user.first_name = ""
                user.last_name = ""
            }

            user.email = email.get()!!

            if (user.first_name.isEmpty() || user.last_name.isEmpty()) {
                nameError.set("Name is required")
//            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
                return
            }
            if (user.email.isEmpty()) {
                emailError.set("Email is required")
//            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
                return
            }

            user.secondary_email = secondaryEmail.get()
            user.birthday = Utils.gayDateToNormal(birthDate.get())
            user.address = mailingAddress.get().toString()
            user.phone = telephone.get().toString()
            user.role = mRepository.me.getValue()!!.role
            user.company = mRepository.me.getValue()!!.company
            user.company_id = mRepository.me.getValue()!!.company.id
            user.address_unit_apt_suite = unitSuite!!.get().toString()
            user.department = mRepository.me.getValue()!!.department

            mService.updateMe(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { baseResponse ->
                        if (baseResponse.success) {
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                            mRepository.me.onNext(baseResponse.data)
                        } else {
                            Toast.makeText(context, baseResponse.error.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                    { throwable ->
                        Toast.makeText(context, "Could not update profile", Toast.LENGTH_LONG)
                            .show()
                    })
        }
    }

}