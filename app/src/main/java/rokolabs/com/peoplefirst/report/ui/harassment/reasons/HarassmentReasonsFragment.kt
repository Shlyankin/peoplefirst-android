package rokolabs.com.peoplefirst.report.ui.harassment.reasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_harassment_reason.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHarassmentReasonsBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class HarassmentReasonsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel:HarassmentReasonsModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel=ViewModelProviders.of(this,viewModelFactory).get(HarassmentReasonsModel::class.java)
        var binder=DataBindingUtil.inflate<FragmentHarassmentReasonsBinding>(inflater, R.layout.fragment_harassment_reasons,container,false)
        binder.viewModel=viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
        list.setLayoutManager(LinearLayoutManager(context))
        list.setAdapter(viewModel.mAdapter)

    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}