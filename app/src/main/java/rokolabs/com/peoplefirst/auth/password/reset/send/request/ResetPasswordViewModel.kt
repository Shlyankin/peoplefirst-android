package rokolabs.com.peoplefirst.auth.password.reset.send.request

import android.view.View
import androidx.databinding.ObservableField
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged

class ResetPasswordViewModel {
    var email:ObservableField<String> = ObservableField()
    var resetClick: Subject<View> = PublishSubject.create()
    init {
        email.addOnPropertyChanged {

        }
    }
}