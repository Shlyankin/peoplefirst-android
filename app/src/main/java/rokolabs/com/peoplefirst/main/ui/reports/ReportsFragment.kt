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
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class ReportsFragment : Fragment() {

    private lateinit var viewModel: ReportsModel
    @Inject
    lateinit var mRepository: HarassmentRepository
    @Inject
    lateinit var mService: PeopleFirstService
    var mDisposable=CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel= ReportsModel(context!!,mRepository,mService)
        var binder=DataBindingUtil.inflate<FragmentReportsBinding>(inflater,R.layout.fragment_reports,container,false)
        binder.viewModel=viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        listActive.adapter=viewModel.activeAdapter
        listInactive.adapter=viewModel.inactiveAdapter
        viewModel.initDisposable()
        mDisposable.add(viewModel.activeAdapter.getOnClickEditSubject().subscribe{
            mRepository.currentReport.onNext(it);
//            Intent intent = new Intent(this, ReportSummaryActivity.class);
//            intent.putExtra("mode", ReportSummaryActivity.EDIT_MODE);
//            startActivity(intent);
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}