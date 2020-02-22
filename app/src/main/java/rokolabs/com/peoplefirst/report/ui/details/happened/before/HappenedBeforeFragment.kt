package rokolabs.com.peoplefirst.report.ui.details.happened.before

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
import rokolabs.com.peoplefirst.databinding.FragmentHappenedBeforeBinding
import rokolabs.com.peoplefirst.databinding.FragmentHarassmentReasonsBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.ui.harassment.reasons.HarassmentReasonsModel
import javax.inject.Inject

class HappenedBeforeFragment :Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: HappenedBeforeModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel= ViewModelProviders.of(this,viewModelFactory).get(HappenedBeforeModel::class.java)
        var binder= DataBindingUtil.inflate<FragmentHappenedBeforeBinding>(inflater, R.layout.fragment_happened_before,container,false)
        binder.viewModel=viewModel
        return binder.root
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