package rokolabs.com.peoplefirst.auth.login

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityLoginBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var viewmodelFactory: ViewModelFactory

    lateinit var viewModel: LoginViewModel
    var mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewModel = ViewModelProviders.of(this, viewmodelFactory).get(LoginViewModel::class.java)
        var binder =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binder.viewModel = viewModel
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }

}