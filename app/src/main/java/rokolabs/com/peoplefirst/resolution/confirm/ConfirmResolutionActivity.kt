package rokolabs.com.peoplefirst.resolution.confirm

import android.os.Bundle
import android.text.TextUtils
import android.util.Pair
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_confirm_resolution.*
import kotlinx.android.synthetic.main.listitem_report.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityConfirmResolutionBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.report.ui.summary.ReportSummaryActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import javax.inject.Inject

class ConfirmResolutionActivity : AppCompatActivity() {
    @Inject
    lateinit var mRepository: HarassmentRepository
    @Inject
    lateinit var factory: ViewModelFactory
    var mDisposable = CompositeDisposable()
    lateinit var confirmResolutionModel: ConfirmResolutionModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        confirmResolutionModel =
            ViewModelProviders.of(this, factory).get(ConfirmResolutionModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityConfirmResolutionBinding>(
            this,
            R.layout.activity_confirm_resolution
        )
        binder.viewModel = confirmResolutionModel
    }

    override fun onResume() {
        super.onResume()
        confirmResolutionModel.initDisposable()
        details.setOnClickListener {
            ReportSummaryActivity.showReadOnly(this)
        }
        mDisposable.add(
            Observable.combineLatest(mRepository.currentReport,
                mRepository.me,
                BiFunction<Report, User, Pair<*, *>> { report: Report?, user: User? ->
                    Pair<Any?, Any?>(
                        report,
                        user
                    )
                }
            ).subscribe { pair: Pair<*, *> ->
                val report =
                    pair.first as Report
                val user =
                    pair.second as User
                text.setText(
                    text.getText().toString().replace(
                        "{Company}",
                        user.company.name
                    )
                )
                if (report !== Report.EMPTY) {
                    dateTime.setText(Utils.dateFormat(report.datetime))
                    location.setText(
                        (if (report.location_city == null) "" else report.location_city) + " "
                                + if (report.location_details == null) "" else report.location_details
                    )
                    checkboxWrapper.removeAllViews()
                    for (ht in report.harassment_types.subList(
                        0,
                        Math.min(report.harassment_types.size, 3)
                    )) {
                        val cb = CheckBox(this)
                        cb.text = ht.title
                        cb.isChecked = true
                        cb.textSize = 12f
                        cb.layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        cb.setButtonDrawable(R.drawable.selector_checkbox)
                        cb.isEnabled = false
                        val scale = resources.displayMetrics.density
                        cb.setPadding(
                            cb.paddingLeft + (6.0f * scale + 0.5f).toInt(),
                            cb.paddingTop,
                            cb.paddingRight,
                            cb.paddingBottom
                        )
                        cb.maxLines = 1
                        cb.ellipsize = TextUtils.TruncateAt.END
                        checkboxWrapper.addView(cb)
                    }
                    checkboxWrapper.requestLayout()
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        confirmResolutionModel.dispose()
    }
}