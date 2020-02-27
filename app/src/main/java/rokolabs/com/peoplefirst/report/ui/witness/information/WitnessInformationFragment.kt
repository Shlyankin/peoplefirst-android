package rokolabs.com.peoplefirst.report.ui.witness.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentWereAnyWitnessesBinding
import rokolabs.com.peoplefirst.databinding.FragmentWitnessInformationBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersFragment
import rokolabs.com.peoplefirst.report.ui.witness.WereAnyWitnessModel
import javax.inject.Inject

class WitnessInformationFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: WitnessInformationModel

    lateinit var mSelectedUsers: SelectedUsersFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(WitnessInformationModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentWitnessInformationBinding>(
            inflater,
            R.layout.fragment_witness_information,
            container,
            false
        )

        binder.viewModel = viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        mSelectedUsers =
            childFragmentManager.findFragmentById(R.id.selectedUsers) as SelectedUsersFragment
        mSelectedUsers.viewModel.setButtonText("Add another witness")
        viewModel.mSelectedUsers=mSelectedUsers
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}