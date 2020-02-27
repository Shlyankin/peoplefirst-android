package rokolabs.com.peoplefirst.report.ui.witness

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
import rokolabs.com.peoplefirst.databinding.FragmentWereAnyWitnessesBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class WereAnyWitnessesFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: WereAnyWitnessModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(WereAnyWitnessModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentWereAnyWitnessesBinding>(
            inflater,
            R.layout.fragment_were_any_witnesses,
            container,
            false
        )
        binder.viewModel = viewModel
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