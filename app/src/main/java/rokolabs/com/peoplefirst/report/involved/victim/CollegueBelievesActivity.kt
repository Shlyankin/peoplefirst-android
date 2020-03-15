package rokolabs.com.peoplefirst.report.involved.victim

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_collegue_beleives.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityCollegueBeleivesBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class CollegueBelievesActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: CollegueBelievesModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CollegueBelievesModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityCollegueBeleivesBinding>(
            this,
            R.layout.activity_collegue_beleives
        )
        binder.viewModel = viewModel
        val text1 =
            "If no action is taken, we will remind you in <b>5 days</b>. You may respond to this report at any time in My Reports."
        text.setText(Html.fromHtml(text1))
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDisposabale()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}