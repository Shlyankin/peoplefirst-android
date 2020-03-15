package rokolabs.com.peoplefirst.report.involved.named

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_named.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityNamedBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NamedActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var mRepository: HarassmentRepository
    lateinit var viewModel: NamedModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(NamedModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityNamedBinding>(
            this,
            R.layout.activity_named
        )
        binder.viewModel = viewModel
        val cal = Calendar.getInstance()
        var date = ""
        try {
            val format =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            cal.time = format.parse(mRepository.currentReport.getValue()!!.created_at)
            cal.add(Calendar.DAY_OF_MONTH, 10)
            date = format.format(cal.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (mRepository.named == HarassmentRepository.AGGRESSOR) {
            caption.setText("A colleague believes you may have been involved in a harassment incident")
            val text1 =
                "Please review their report. Their report was submitted to <b>" + mRepository.me.getValue()!!.company.name + "</b> on <b>" + date + "</b>"
            lightText.setText(Html.fromHtml(text1))
            text.setVisibility(View.GONE)
        } else if (mRepository.named == HarassmentRepository.WITNESS) {
            caption.setText("A colleague believes you may have witnessed a harassment")
            var text1 =
                "Your identity can remain anonymous.<br><br>Please review their report. Their report will be submitted to <b>" + mRepository.me.getValue()!!.company.name + "</b> on <b>" + date + "</b>."
            lightText.setText(Html.fromHtml(text1))
            text1 = "If no action is taken, we will ask you again in <b>5 days</b>."
            text.setText(Html.fromHtml(text1))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }

    companion object {
        fun showForWitness(from: Context) {
            val intent = Intent(from, NamedActivity::class.java)
            intent.putExtra("who", "witness")
            from.startActivity(intent)
        }

        fun showForTransgressor(from: Context) {
            val intent = Intent(from, NamedActivity::class.java)
            intent.putExtra("who", "aggressor")
            from.startActivity(intent)
        }
    }
}