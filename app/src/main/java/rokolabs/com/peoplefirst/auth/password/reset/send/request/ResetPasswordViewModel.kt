package rokolabs.com.peoplefirst.auth.password.reset.send.request

import android.view.View
import androidx.databinding.ObservableField
import io.reactivex.subjects.BehaviorSubject
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged

class ResetPasswordViewModel {
    var email:ObservableField<String> = ObservableField()
    var resetClick:BehaviorSubject<View> = BehaviorSubject.create()
    init {
        email.addOnPropertyChanged {

        }
    }
}