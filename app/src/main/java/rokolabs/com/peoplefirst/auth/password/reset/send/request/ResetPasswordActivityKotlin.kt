package rokolabs.com.peoplefirst.auth.password.reset.send.request

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.databinding.ActivityResetPasswordBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import javax.inject.Inject

class ResetPasswordActivityKotlin : AppCompatActivity() {
    var resetPawwordViewModel: ResetPasswordViewModel = ResetPasswordViewModel()
    @Inject
    lateinit var mService: PeopleFirstService
    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        var binding =DataBindingUtil.setContentView<ActivityResetPasswordBinding>(this, R.layout.activity_reset_password)
        binding.viewModel=resetPawwordViewModel
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        resetPawwordViewModel.resetClick.subscribe {
            mService.resetPassword(resetPawwordViewModel.email?.get())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ baseResponse ->
                        if (baseResponse.success) {
                            Toast.makeText(this, "Reset email has been sent", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, baseResponse.error.message, Toast.LENGTH_LONG).show()
                        }
                    }, { throwable -> Toast.makeText(this, "Could not reset password", Toast.LENGTH_LONG).show() })
        }
    }
}