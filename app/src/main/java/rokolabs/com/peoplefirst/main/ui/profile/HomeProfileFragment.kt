package rokolabs.com.peoplefirst.main.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home_profile.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHomeProfileBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.profile.ProfileFragment
import javax.inject.Inject

class HomeProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: HomeProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        var binder = DataBindingUtil.inflate<FragmentHomeProfileBinding>(
            inflater,
            R.layout.fragment_home_profile,
            container,
            false
        )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(HomeProfileViewModel::class.java)
        binder.viewModel = viewModel
        return binder.root
    }


    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
        activity?.findViewById<ImageView>(R.id.save)?.visibility = View.VISIBLE
        activity?.findViewById<ImageView>(R.id.save)?.setOnClickListener {
            (childFragmentManager.findFragmentById(R.id.profileFragment) as ProfileFragment)?.saveUser()
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<ImageView>(R.id.save)?.visibility = View.GONE
        viewModel.dispose()
    }
}