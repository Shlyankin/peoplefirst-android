package rokolabs.com.peoplefirst.report.involved.verify.victim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityVerifyVictimBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class VerifyVictimActivity :AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: VerifyVictimModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(VerifyVictimModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityVerifyVictimBinding>(
            this,
            R.layout.activity_verify_victim
        )
        binder.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}