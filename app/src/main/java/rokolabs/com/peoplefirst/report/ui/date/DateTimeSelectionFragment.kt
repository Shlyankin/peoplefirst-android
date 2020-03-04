package rokolabs.com.peoplefirst.report.ui.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentDateTimeSelectionBinding
import rokolabs.com.peoplefirst.databinding.FragmentWereAnyWitnessesBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.ui.witness.WereAnyWitnessModel
import javax.inject.Inject

class DateTimeSelectionFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: DateTimeSelectionViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(DateTimeSelectionViewModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentDateTimeSelectionBinding>(
            inflater,
            R.layout.fragment_date_time_selection,
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