package rokolabs.com.peoplefirst.report.ui.profile.confirmation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentProfileConfirmationBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.profile.ProfileFragment
import rokolabs.com.peoplefirst.report.ui.notification.result.ResultNotificationActivity
import javax.inject.Inject

class ProfileConfirmationFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ProfileConfirmationModel
    var mDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ProfileConfirmationModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentProfileConfirmationBinding>(
            inflater,
            R.layout.fragment_profile_confirmation,
            container,
            false
        )
        binder.viewModel = viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
        mDisposable.addAll(
            viewModel.submitClick.subscribe {
                var fragment =
                    childFragmentManager.findFragmentById(R.id.profileFragment) as ProfileFragment
                goResultNotification()
                fragment.saveUser {
                    goResultNotification()
                }
            }
        )

    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
        mDisposable.clear()
    }

    fun goResultNotification() {
        activity?.startActivity(
            Intent(
                activity,
                ResultNotificationActivity::class.java
            )
        )
        activity?.finish()
    }
}