package rokolabs.com.peoplefirst.report.involved.verify.witness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityVerifyWitnessBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class VerifyWitnessActivity :AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewWitnessModel: VerifyWitnessModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewWitnessModel =
            ViewModelProviders.of(this, viewModelFactory).get(VerifyWitnessModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityVerifyWitnessBinding>(
            this,
            R.layout.activity_verify_witness
        )
        binder.viewModel = viewWitnessModel
    }

    override fun onResume() {
        super.onResume()
        viewWitnessModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewWitnessModel.dispose()
    }
}