package rokolabs.com.peoplefirst.profile

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import javax.inject.Inject

class ProfileModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var name: ObservableField<String> = ObservableField<String>()
    var email: ObservableField<String> = ObservableField<String>()
    var secondaryEmail: ObservableField<String> = ObservableField<String>()
    var birthDate: ObservableField<String> = ObservableField<String>()
    var mailingAddress: ObservableField<String> = ObservableField<String>()
    var unitSuite: ObservableField<String> = ObservableField<String>()
    var telephone: ObservableField<String> = ObservableField<String>()
    var editeable = ObservableField<Boolean>()

    var nameError: ObservableField<String> = ObservableField<String>()
    var emailError: ObservableField<String> = ObservableField<String>()
    var secondaryEmailError: ObservableField<String> = ObservableField<String>()
    var birthDateError: ObservableField<String> = ObservableField<String>()
    var mailingAddressError: ObservableField<String> = ObservableField<String>()
    var unitSuiteError: ObservableField<String> = ObservableField<String>()
    var telephoneError: ObservableField<String> = ObservableField<String>()

    var mDisposable = CompositeDisposable()

    init {
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
        telephone.addOnPropertyChanged {
            if (it.get()?.length == 0) {
                telephoneError.set("Phone number is required")
            } else {
                telephoneError.set(null)
            }
        }
    }

    fun initDisposable() {
        mDisposable.addAll(
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

    fun dispose() {
        mDisposable.clear()
    }

    fun disableEditing() {
        editeable.set(false)
    }

    fun enableEditing() {
        editeable.set(true)
        editeable.notifyChange()
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

    fun saveUser(void: () -> Unit) {
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