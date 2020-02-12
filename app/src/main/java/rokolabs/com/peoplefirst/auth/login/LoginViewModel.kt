package rokolabs.com.peoplefirst.auth.login

import android.view.View
import androidx.databinding.ObservableField
import io.reactivex.subjects.BehaviorSubject

class LoginViewModel {
    var email:ObservableField<String> = ObservableField()
    var pass:ObservableField<String> = ObservableField()
    var registerClick:BehaviorSubject<View> = BehaviorSubject.create()
    var loginClick:BehaviorSubject<View> = BehaviorSubject.create()
    var forgotClick:BehaviorSubject<View> = BehaviorSubject.create()
}