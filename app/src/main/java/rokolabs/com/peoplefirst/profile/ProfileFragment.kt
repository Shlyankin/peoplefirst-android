package rokolabs.com.peoplefirst.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHomeProfileBinding
import rokolabs.com.peoplefirst.databinding.FragmentProfileBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.main.ui.profile.HomeProfileViewModel
import javax.inject.Inject

class ProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ProfileModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        var binder = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileModel::class.java)
        binder.viewModel = viewModel
        return binder.root
    }


    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
    }

    fun saveUser() {
        viewModel.saveUser {}
    }

    fun saveUser(void: () -> Unit) {
        viewModel.saveUser(void)
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}