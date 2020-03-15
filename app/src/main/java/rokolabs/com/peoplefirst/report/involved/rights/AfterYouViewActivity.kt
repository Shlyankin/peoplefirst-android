package rokolabs.com.peoplefirst.report.involved.rights

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_collegue_beleives.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityAfteryouViewBinding
import rokolabs.com.peoplefirst.databinding.ActivityCollegueBeleivesBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.involved.victim.CollegueBelievesModel
import javax.inject.Inject

class AfterYouViewActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: AfterYouViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AfterYouViewModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityAfteryouViewBinding>(
            this,
            R.layout.activity_afteryou_view
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