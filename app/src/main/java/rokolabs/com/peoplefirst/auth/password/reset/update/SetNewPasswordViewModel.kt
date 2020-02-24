package rokolabs.com.peoplefirst.auth.password.reset.update

import android.view.View
import androidx.databinding.ObservableField
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class SetNewPasswordViewModel {
    var newPassword: ObservableField<String> = ObservableField()
    var setClick: Subject<View> = PublishSubject.create()
}