package rokolabs.com.peoplefirst.report.involved.verify.agressor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityVerifyAgressorBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class VerifyAggressorActivity :AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewAggressorModel: VerifyAggressorModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewAggressorModel =
            ViewModelProviders.of(this, viewModelFactory).get(VerifyAggressorModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityVerifyAgressorBinding>(
            this,
            R.layout.activity_verify_agressor
        )
        binder.viewModel = viewAggressorModel
    }

    override fun onResume() {
        super.onResume()
        viewAggressorModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewAggressorModel.dispose()
    }
}