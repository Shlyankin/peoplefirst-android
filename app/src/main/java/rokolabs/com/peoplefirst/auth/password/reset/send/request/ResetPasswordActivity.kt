package rokolabs.com.peoplefirst.auth.password.reset.send.request

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.databinding.ActivityResetPasswordBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class ResetPasswordActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var resetPawwordViewModel: ResetPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        var binding = DataBindingUtil.setContentView<ActivityResetPasswordBinding>(
            this,
            R.layout.activity_reset_password
        )
        resetPawwordViewModel=ViewModelProviders.of(this,viewModelFactory).get(ResetPasswordViewModel::class.java)
        binding.viewModel = resetPawwordViewModel
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        resetPawwordViewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        resetPawwordViewModel.dispose()
    }
}