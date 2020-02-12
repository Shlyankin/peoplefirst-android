package rokolabs.com.peoplefirst.auth.password.reset.update

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.auth.login.LoginActivity
import rokolabs.com.peoplefirst.databinding.ActivitySetNewPasswordBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.model.requests.UpdatePasswordRequest
import javax.inject.Inject

class SetNewPasswordActivity : AppCompatActivity() {
    @Inject
    lateinit var mService: PeopleFirstService
    var token: String? = null
    var viewModel: SetNewPasswordViewModel = SetNewPasswordViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivitySetNewPasswordBinding>(this, R.layout.activity_set_new_password)
        binding.viewModel = viewModel
        token = intent.extras?.get("token") as String
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        viewModel.setClick.subscribe {
            if (token != null && viewModel.newPassword.get() != "") {
                mService.setNewPassword(UpdatePasswordRequest(token, viewModel.newPassword.get()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError { it.printStackTrace() }
                        .subscribeBy(onSuccess = {
                            if (it.success) {
                                Toast.makeText(this, "Password has been updated", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        },onError = {it.printStackTrace()})
            }
        }
    }
}