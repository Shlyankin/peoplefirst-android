package rokolabs.com.peoplefirst.report.ui.victim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_who_being_harassed.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentSelectedUsersBinding
import rokolabs.com.peoplefirst.databinding.FragmentWhoBeingHarassedBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersFragment
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersModel
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class WhoBeingHarassedFragment : Fragment() {

    @Inject
    lateinit var mRepository: HarassmentRepository
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: WhoBeingHarassedModel
    lateinit var mSelectedUsers: SelectedUsersFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(WhoBeingHarassedModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentWhoBeingHarassedBinding>(
            inflater,
            R.layout.fragment_who_being_harassed,
            container,
            false
        )
        mSelectedUsers =
            childFragmentManager.findFragmentById(R.id.selectedUsers) as SelectedUsersFragment
        viewModel.mSelectedUsers = mSelectedUsers
        mSelectedUsers.viewModel.onUserSelectedCallback = object :
            SelectedUsersModel.OnUserSelectedCallback {
            override fun onUserSelected(user: User) {
                viewModel?.selfReporting.set(user.id === mRepository.me.getValue()!!.id)
            }
        }
        mSelectedUsers.viewModel.disableMultiselect()
        mSelectedUsers.viewModel.mRetailMode = mRepository.me.getValue()!!.is_retail
        mSelectedUsers.viewModel.mRetailMeEmail = mRepository.me.getValue()!!.email

        binder.viewModel = viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        selfReporting.setTypeface(ResourcesCompat.getFont(context!!, R.font.ratio_regular))
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }

}