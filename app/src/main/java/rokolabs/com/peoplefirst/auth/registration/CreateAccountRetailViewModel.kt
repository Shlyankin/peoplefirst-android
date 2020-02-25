package rokolabs.com.peoplefirst.auth.registration

import android.content.Context
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
import rokolabs.com.peoplefirst.model.RetailRegistrationRequest
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import javax.inject.Inject

class CreateAccountRetailViewModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    lateinit var activity: CreateAccountRetailActivity
    var mDisposable = CompositeDisposable()
    var popularDomains = listOf<String>(
        "gmail.com",
        "yahoo.com",
        "outlook.com",
        " aol.com",
        "icloud.com",
        " me.com",
        " Comcast.com"
    )

    var passwordError: ObservableField<String> = ObservableField()
    var firstNameError: ObservableField<String> = ObservableField()
    var secondNameError: ObservableField<String> = ObservableField()
    var corporateEmailError: ObservableField<String> = ObservableField()

    var password: ObservableField<String> = ObservableField()
    var firstName: ObservableField<String> = ObservableField()
    var secondName: ObservableField<String> = ObservableField()
    var corporateEmail: ObservableField<String> = ObservableField()
    var registerClicks: Subject<View> = PublishSubject.create()

    init {
        activity = context as CreateAccountRetailActivity

    }

    fun initDisposable() {
        mDisposable.addAll(
            registerClicks.subscribe {
                if (checkConditions()) {
                    mDisposable.add(
                        mService.retailRegistration(getRetailRegistrationRequest())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(onError = { it.printStackTrace() },
                                onSuccess = {
                                    if (it.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Registration successful",
                                            Toast.LENGTH_LONG
                                        ).show()
//                                        startActivity(Intent(this,WelcomeRetailActivity::class.java))
                                        activity.finish()
                                    }
                                })
                    )
                }
            },
            firstName.addOnPropertyChanged {
                if (it.get()?.length == 0) {
                    firstNameError.set("First name is required")
                } else {
                    firstNameError.set(null)
                }
            },
            secondName.addOnPropertyChanged {
                if (it.get()?.length == 0) {
                    secondNameError.set("First name is required")
                } else {
                    secondNameError.set(null)
                }
            },
            corporateEmail.addOnPropertyChanged {
                var email = it.get()!!
                if (email.length == 0) {
                    corporateEmailError.set("First name is required")
                } else {
                    popularDomains.forEach {
                        if (email.endsWith(it, true)) {
                            corporateEmailError.set("Public domains are forbidden")
                            return@addOnPropertyChanged
                        }
                    }
                    corporateEmailError.set(null)
                }
            },
            password.addOnPropertyChanged {
                if (it.get()?.length == 0) {
                    secondNameError.set("Password name is required")
                } else {
                    secondNameError.set(null)
                }
            }
        )
    }

    fun dispose() {

    }

    fun getRetailRegistrationRequest(): RetailRegistrationRequest {
        var t = RetailRegistrationRequest(
            firstName.get(),
            secondName.get(),
            corporateEmail.get(),
            password.get()
        )
        return t
    }

    fun checkConditions(): Boolean {
        return password.get() != "" &&
                firstName.get() != "" &&
                secondName.get() != "" &&
                passwordError.get() != "" &&
                corporateEmail.get() != "" &&
                firstNameError.get() != "" &&
                secondNameError.get() != "" &&
                corporateEmailError.get() != ""

    }
}