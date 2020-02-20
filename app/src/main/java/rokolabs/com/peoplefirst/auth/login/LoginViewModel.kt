package rokolabs.com.peoplefirst.auth.login

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class LoginViewModel :ViewModel(){
    var email:ObservableField<String> = ObservableField()
    var pass:ObservableField<String> = ObservableField()
    var registerClick: Subject<View> = PublishSubject.create()
    var loginClick:Subject<View> = PublishSubject.create()
    var forgotClick:Subject<View> = PublishSubject.create()
}