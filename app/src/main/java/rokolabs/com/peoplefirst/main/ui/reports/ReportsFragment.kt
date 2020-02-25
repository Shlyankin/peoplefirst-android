package rokolabs.com.peoplefirst.main.ui.reports

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_reports.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.databinding.FragmentReportsBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.model.CounsellingService
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject
import android.util.TypedValue
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.model.Report


class ReportsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory:ViewModelFactory
    private lateinit var viewModel: ReportsModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =ViewModelProviders.of(this,viewModelFactory).get(ReportsModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentReportsBinding>(
            inflater,
            R.layout.fragment_reports,
            container,
            false
        )
        binder.viewModel = viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()


        listActive.adapter = viewModel.activeAdapter
        listInactive.adapter = viewModel.inactiveAdapter
        viewModel.initDisposable()

    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}