package rokolabs.com.peoplefirst.report.ui.notification.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentMainQuestionsBinding
import rokolabs.com.peoplefirst.databinding.FragmentReportNotificationResultBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.ui.home.MainQuestionsModel
import javax.inject.Inject

class ResultNotificationFragment : Fragment() {

    private lateinit var resultNotificationModel: ResultNotificationModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        resultNotificationModel =
            ViewModelProviders.of(this, viewModelFactory).get(ResultNotificationModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentReportNotificationResultBinding>(
            inflater,
            R.layout.fragment_report_notification_result, container, false
        )
        binder.viewModel = resultNotificationModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        resultNotificationModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        resultNotificationModel.dispose()
    }
}