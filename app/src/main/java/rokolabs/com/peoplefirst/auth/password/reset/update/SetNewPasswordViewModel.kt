package rokolabs.com.peoplefirst.auth.password.reset.update

import android.view.View
import androidx.databinding.ObservableField
import io.reactivex.subjects.BehaviorSubject

class SetNewPasswordViewModel {
    var newPassword: ObservableField<String> = ObservableField()
    var setClick: BehaviorSubject<View> = BehaviorSubject.create()
}