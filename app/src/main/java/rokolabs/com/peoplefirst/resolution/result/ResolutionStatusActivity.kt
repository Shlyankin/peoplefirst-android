package rokolabs.com.peoplefirst.resolution.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Pair
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_confirm_resolution.text
import kotlinx.android.synthetic.main.activity_resolution_status.*
import kotlinx.android.synthetic.main.fragment_resources.caption
import kotlinx.android.synthetic.main.listitem_report.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityResolutionStatusBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.report.ui.summary.ReportSummaryActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import javax.inject.Inject

class ResolutionStatusActivity : AppCompatActivity() {
    @Inject
    lateinit var mRepository: HarassmentRepository
    @Inject
    lateinit var factory: ViewModelFactory
    var mDisposable = CompositeDisposable()
    lateinit var resolutionStatusModel: ResolutionStatusModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        resolutionStatusModel =
            ViewModelProviders.of(this, factory).get(ResolutionStatusModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityResolutionStatusBinding>(
            this,
            R.layout.activity_resolution_status
        )
        binder.viewModel = resolutionStatusModel
        if (intent.hasExtra("rejected")) {
            resolutionStatusModel.rejected.set(true)
        }

        if (intent.hasExtra("no_resolution")) {
            resolutionStatusModel.noResolution.set(true)
        }

        if (resolutionStatusModel.rejected.get()!!) {
            caption.setText("Resolution rejected")
            text.setVisibility(View.GONE)
            textRejected.setVisibility(View.VISIBLE)
            textRejectedDeadline.setVisibility(View.VISIBLE)
        }

        if (resolutionStatusModel.noResolution.get()!!) {
            caption.setText("No resolution has occured")
            daysRemained.setVisibility(View.VISIBLE)
            text.setVisibility(View.GONE)
            notification.setVisibility(View.VISIBLE)
            exceeded.setVisibility(View.VISIBLE)
        }
    }

    override fun onResume() {
        super.onResume()
        resolutionStatusModel.initDisposable()
        details.setOnClickListener {
            ReportSummaryActivity.showReadOnly(this)
        }
        mDisposable.addAll(
            Observable.combineLatest(mRepository.currentReport,
                mRepository.me,
                BiFunction<Report, User, Pair<Report, User>> { report, user ->
                    return@BiFunction Pair(report, user)
                }).subscribe {
                val report =
                    it.first as Report
                val user =
                    it.second as User
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


    override fun onBackPressed() {
        super.onBackPressed()
        mRepository.currentReport.onNext(Report.EMPTY)
    }

    override fun onPause() {
        super.onPause()
        resolutionStatusModel.dispose()
    }

    companion object {
        fun showAccepted(context: Context) {
            val intent = Intent(context, ResolutionStatusActivity::class.java)
            context.startActivity(intent)
        }

        fun showRejected(context: Context) {
            val intent = Intent(context, ResolutionStatusActivity::class.java)
            intent.putExtra("rejected", true)
            context.startActivity(intent)
        }


        fun showNpResolution(context: Context) {
            val intent = Intent(context, ResolutionStatusActivity::class.java)
            intent.putExtra("no_resolution", true)
            context.startActivity(intent)
        }
    }
}