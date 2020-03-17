package rokolabs.com.peoplefirst.report.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_report_summary.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHowResolvedBinding
import rokolabs.com.peoplefirst.databinding.FragmentReportSummaryBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.ui.resolution.how.HowResolvedModel
import javax.inject.Inject

class ReportSummaryFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ReportSummaryModel
    var mDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ReportSummaryModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentReportSummaryBinding>(
            inflater,
            R.layout.fragment_report_summary,
            container,
            false
        )
        binder.viewModel = viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
        val mod = activity?.intent?.extras?.get("mode") as Int?
        if (mod != null) {
            textView6.visibility=View.GONE
            if (mod == EditReportActivity.MODE_VERIFY_AGGRESSOR) {

            } else if (mod == EditReportActivity.MODE_VERIFY_AGGRESSOR) {

            } else if (mod == EditReportActivity.MODE_VERIFY_AGGRESSOR) {

            } else if (mod == EditReportActivity.MODE_VERIFY_AGGRESSOR) {

            }
        }else{

        }

    }

    fun showReadOnly(boolean: Boolean) {
        viewModel.dispose()
        viewModel.readOnly.set(boolean)
        viewModel.doneVisibility.set(if (boolean) View.GONE else View.VISIBLE)
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
        mDisposable.clear()
    }
}